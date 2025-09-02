package zk.custom;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONValue;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;

/**
 * reCAPTCHA v2 do Google. Este reCAPTCHA utiliza os tokens da conta
 * npd-des@uem.br limitado ao domínio uem.br, sendo possível monitorar o acesso
 * em https://www.google.com/u/3/recaptcha/admin
 *
 * Para completo funcionamento, além desta classe, também é necessário ter o
 * recaptcha.zul e a configuração do componente em /WEB-INF/lang-addon.xml
 *
 * @author alison
 */
public class Recaptcha extends HtmlMacroComponent {

    private final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String SECRET_KEY = "6LdLbPEqAAAAAEQ4n_NYCj3a7bew-CZTx9iqRWXy";
    private final String PUBLIC_KEY = "6LdLbPEqAAAAAH-MOJ7GQF-QtwD83LCf_ukwRiCX";

    private boolean isValid = false;

    private Div captcha;

    public Recaptcha() {
        compose();
    }

    public void onCreate() {

        captcha = (Div) getFellow("g-recaptcha");
        captcha.setClientDataAttribute("sitekey", PUBLIC_KEY);

        captcha.addEventListener("onCaptchaResponse", (Event event) -> {
            String gRecaptchaResponse = event.getData().toString();
            verify(gRecaptchaResponse);
        });

        captcha.addEventListener("onCaptchaExpiration", (Event event) -> {
            isValid = false;
        });
    }

    private void verify(String gRecaptchaResponse) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(VERIFY_URL + "?secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> verifyResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = (JSONObject) JSONValue.parse(verifyResponse.body());

            isValid = Boolean.parseBoolean(result.get("success").toString());

            if (!isValid) {
                showCaptchaError("Não foi possível verificar o reCAPTCHA: " + result.get("error-codes").toString());
            }
        } catch (Exception e) {
            showCaptchaError("Houve um erro ao processar o reCAPTCHA: " + e.getMessage());
        }
    }

    private void showCaptchaError(String message) {

        Clients.showNotification(message, Clients.NOTIFICATION_TYPE_ERROR, captcha, "end_center", 0);
    }

    public boolean isValid() {

        return isValid;
    }

}
