package exemplos.modal.controller;

import java.util.HashMap;
import java.util.Map;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author alison
 */
public class ModalController extends Window {

    private Window winModal;

    private Intbox intExemplo;
    private Textbox txtExemplo;
    private Datebox dateExemplo;

    public void onCreate() {

        winModal = (Window) getFellow("winModal");
        intExemplo = (Intbox) getFellow("intExemplo");
        txtExemplo = (Textbox) getFellow("txtExemplo");
        dateExemplo = (Datebox) getFellow("dateExemplo");

        //pega os atributos 
        Map atributosDoModal = new HashMap();
        atributosDoModal = winModal.getAttributes();
        String valorRecebido = (String) atributosDoModal.get("chave_exemplo");

        //populando um campo com o valor recebido
        txtExemplo.setValue(valorRecebido);

    }

    public void fechar() {

        //eu posso passar atributos de volta
        winModal.setAttribute("atualiza", "S");
        winModal.setAttribute("campo_texto", txtExemplo.getValue());

        //fecho o modal
        winModal.onClose();
    }

}
