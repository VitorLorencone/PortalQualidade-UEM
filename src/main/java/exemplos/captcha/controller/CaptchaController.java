package exemplos.captcha.controller;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import zk.custom.Recaptcha;

/**
 * Exemplo de reCAPTCHA v2 do Google. Arquivos necessários para funcionar:
 * recaptcha.zul, Recaptcha.java e necessário também as configurações em
 * /WEB-INF/lang-addon.xml com o caminho corretos dos arquivos.
 *
 * @author alison
 */
public class CaptchaController extends Window {

    private Window win;
    private Recaptcha captcha;

    public void onCreate() {

        this.win = (Window) getFellow("winCaptcha");
        this.captcha = (Recaptcha) getFellow("captcha");
    }

    public void enviar() {

        if (captcha.isValid()) {
            Clients.showNotification("reCAPTCHA validado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, captcha, "end_center", 0);
        } else {
            Clients.showNotification("reCAPTCHA inválido, por favor clique no checkbox!", Clients.NOTIFICATION_TYPE_ERROR, captcha, "end_center", 0);
        }
    }

}
