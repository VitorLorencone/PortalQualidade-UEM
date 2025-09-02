package exemplos.modal.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class ModalIndexController extends Window {

    private Window win;

    public void onCreate() {

        this.win = ((Window) getFellow("winModalIndex"));

    }

    public void abrirModal() {
        Object o;
        try {
            //voce primeiro cria um modal que na verdade é uma window
            Window winModal = (Window) Executions.createComponents("exemplos/modal/modal.zul", null, null);

            //voce pode passar parametros para o modal 
            winModal.setAttribute("chave_exemplo", "valor exemplo");

            //voce pode definir um titulo para o modal
            winModal.setTitle("Modal Exemplo");

            //voce pode escolher se mostra ou nao um icone de fechar no canto superior direito
            winModal.setClosable(true);

            //aqui voce abre a window como sendo um modal
            winModal.doModal();

            //daqui em diante será executado somente apos o modal fechar, 
            //entao aqui em baixo voce pode chamar algum metodo para atualizar alguma informacao na tela
            if (winModal.getAttribute("atualiza").equals("S")) {
                this.atualizarListagem();
            }

            //outro exemplo pegando parâmetros/valores recebidos do modal
            String atributoRecebido = winModal.getAttribute("campo_texto") != null ? winModal.getAttribute("campo_texto").toString() : "";
            Toast.show(atributoRecebido, "Resposta", Toast.Type.INFO);

        } catch (Exception ex) {
            System.out.println("Houve um erro e não foi possível abrir o modal: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private void atualizarListagem() {
        System.out.println("Listagem atualizada");
    }
}
