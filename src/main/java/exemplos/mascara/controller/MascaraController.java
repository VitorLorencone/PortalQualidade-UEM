package exemplos.mascara.controller;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import org.zkoss.zul.Textbox;
import utilitarios.Utils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class MascaraController extends Window {

    private Window win;
    private Textbox url;
    private Textbox email;
    private Textbox celular;
    private Textbox cep;
    private Textbox cpf;

    private final Utils utils = new Utils();

    public void onCreate() {
        this.win = (Window) getFellow("winMascara");
        this.url = (Textbox) getFellow("url");
        this.email = (Textbox) getFellow("email");
        this.celular = (Textbox) getFellow("celular");
        this.cpf = (Textbox) getFellow("cpf");
        this.cep = (Textbox) getFellow("cep");
    }

    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.email.getValue().isBlank()) {
            Clients.showNotification("E-mail é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.email, "end_center", 0);
            gerouErro = true;
            this.email.setFocus(true);
        } else {
            if (!utils.validaEmail(this.email.getValue())) {
                Clients.showNotification("E-mail inválido!", Clients.NOTIFICATION_TYPE_ERROR, this.email, "end_center", 0);
                gerouErro = true;
                this.email.setFocus(true);
            }
        }

        if (this.url.getValue().isBlank()) {
            Clients.showNotification("URL é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.url, "end_center", 0);
            gerouErro = true;
            this.url.setFocus(true);
        } else {
            if (!utils.validaURL(this.url.getValue())) {
                Clients.showNotification("ERL inválido!", Clients.NOTIFICATION_TYPE_ERROR, this.url, "end_center", 0);
                gerouErro = true;
                this.url.setFocus(true);
            }
        }

        if (this.cpf.getValue().isBlank()) {
            Clients.showNotification("CPF é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.cpf, "end_center", 0);
            gerouErro = true;
            this.cpf.setFocus(true);
        } else {
            if (!utils.validaCPF(this.cpf.getValue())) {
                Clients.showNotification("CPF inválido!", Clients.NOTIFICATION_TYPE_ERROR, this.cpf, "end_center", 0);
                gerouErro = true;
                this.cpf.setFocus(true);
            }
        }

        if (this.celular.getValue().isBlank()) {
            Clients.showNotification("Celular é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.celular, "end_center", 0);
            gerouErro = true;
        } else {
            if (utils.soNumeros(this.celular.getValue()).length() < 11) {
                Clients.showNotification("Celular inválido!", Clients.NOTIFICATION_TYPE_ERROR, this.celular, "end_center", 0);
                gerouErro = true;
                this.celular.setFocus(true);
            }
        }

        if (this.cep.getValue().isBlank()) {
            Clients.showNotification("CEP é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.cep, "end_center", 0);
            gerouErro = true;
        } else {
            if (utils.soNumeros(this.cep.getValue()).length() < 8) {
                Clients.showNotification("CEP é inválido!", Clients.NOTIFICATION_TYPE_ERROR, this.cep, "end_center", 0);
                gerouErro = true;
            }
        }

        return !gerouErro;
    }

    public void validar() {
        if (this.validarCampos()) {
            Toast.show("Dados validados com sucesso!", "Sucesso", Toast.Type.SUCCESS);
        }
    }

}
