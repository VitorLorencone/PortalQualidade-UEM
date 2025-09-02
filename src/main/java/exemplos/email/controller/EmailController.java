package exemplos.email.controller;

import org.zkoss.zul.Window;
import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import utilitarios.Email;
import utilitarios.Utils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class EmailController extends Window {

    private Textbox remetenteEmail;
    private Textbox remetenteNome;
    private Textbox destinatarioEmail;
    private Textbox assunto;
    private CKeditor mensagemHtml;

    private final Utils utils = new Utils();

    public void onCreate() {
        remetenteEmail = (Textbox) getFellow("remetente_email");
        remetenteNome = (Textbox) getFellow("remetente_nome");
        destinatarioEmail = (Textbox) getFellow("destinatario_email");
        assunto = (Textbox) getFellow("assunto");
        mensagemHtml = (CKeditor) getFellow("editor_html");
    }

    private boolean validarCampos() {
        boolean gerouErro = false;
        if (remetenteEmail.getValue().isBlank()) {
            Clients.showNotification("E-mail do rementente é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, remetenteEmail, "end_center", 0);
            gerouErro = true;
            remetenteEmail.setFocus(true);
        } else {
            if (!utils.validaEmail(remetenteEmail.getValue())) {
                Clients.showNotification("E-mail do rementente inválido!", Clients.NOTIFICATION_TYPE_ERROR, remetenteEmail, "end_center", 0);
                gerouErro = true;
                remetenteEmail.setFocus(true);
            }
        }
        if (remetenteNome.getValue().isBlank()) {
            Clients.showNotification("Nome do remetente é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, remetenteNome, "end_center", 0);
            gerouErro = true;
            remetenteNome.setFocus(true);
        }

        if (destinatarioEmail.getValue().isBlank()) {
            Clients.showNotification("E-mail do destinatário é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, destinatarioEmail, "end_center", 0);
            gerouErro = true;
            destinatarioEmail.setFocus(true);
        } else {
            if (!utils.validaEmail(destinatarioEmail.getValue())) {
                Clients.showNotification("E-mail do destinatário inválido!", Clients.NOTIFICATION_TYPE_ERROR, destinatarioEmail, "end_center", 0);
                gerouErro = true;
                destinatarioEmail.setFocus(true);
            }
        }

        if (assunto.getValue().isBlank()) {
            Clients.showNotification("Assunto é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, assunto, "end_center", 0);
            gerouErro = true;
            assunto.setFocus(true);
        }

        if (mensagemHtml.getValue().isBlank()) {
            Clients.showNotification("Mensagem é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, mensagemHtml, "end_center", 0);
            gerouErro = true;
        }
        return !gerouErro;
    }

    public void limparCampos() {
        remetenteEmail.setValue("");
        remetenteNome.setValue("");
        destinatarioEmail.setValue("");
        assunto.setValue("");
        mensagemHtml.setValue("");
    }

    public void enviarEmail() {
        if (this.validarCampos()) {
            Email email = new Email();
            email.setDeEmail(remetenteEmail.getValue());
            email.setDeNome(remetenteNome.getValue());
            email.setParaEmail(destinatarioEmail.getValue());
            email.setAssunto(assunto.getValue());
            email.setMensagem(mensagemHtml.getValue());
            if (email.enviarEmailHtml()) {
                Toast.show("E-mail enviado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                limparCampos();
            } else {
                Toast.show("Houve um erro ao enviar o e-mail!", "Erro", Toast.Type.ERROR);
            }
        }
    }

}
