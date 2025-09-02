package exemplos.serverpush.controller;

import net.datafaker.Faker;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.zhtml.P;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 *
 * @author alison
 */
public class ServerpushController extends Window implements DesktopCleanup {

    private Window win;
    private Div divChat;
    private Label lbContador;

    private Desktop desktop = null;
    private Integer contador = 0;

    public void onCreate() {
        this.win = (Window) getFellow("winWebsocket");
        this.divChat = (Div) getFellow("chat");
        this.lbContador = (Label) getFellow("contador");

        this.iniciarServicoPush();
    }

    @Override
    public void onClose() {
        System.out.println("A Window foi fechada");
        desktop.enableServerPush(false);
    }

    @Override
    public void onPageDetached(Page page) {
        System.out.println("O Desktop " + page.getDesktop().getId() + " teve sua Page " + page.getId() + page.getRequestPath() + " trocada");
        desktop.enableServerPush(false);
    }

    @Override
    public void cleanup(Desktop desktop) throws Exception {
        System.out.println("O Desktop " + desktop.getId() + " foi encerrado");
        desktop.enableServerPush(false);
    }

    public void iniciarServicoPush() {
        desktop = Executions.getCurrent().getDesktop();

        if (!desktop.isServerPushEnabled()) {

            desktop.enableServerPush(true);

            new Thread(() -> {

                while (desktop.isServerPushEnabled()) {
                    try {

                        Executions.schedule(desktop, (Event event) -> {
                            this.executaGeradorChatFake();
                            System.out.println("O Desktop " + desktop.getId() + " recebeu atualização");
                        }, null);

                        Thread.sleep(3000);
                    } catch (Exception ex) {
                        System.out.println("Houve um erro ao executar PushEvent" + ExceptionUtils.getStackTrace(ex));
                    }
                }

            }).start();
        }
    }

    private void executaGeradorChatFake() {

        Faker f = new Faker();

        contador++;
        this.lbContador.setValue("+" + contador.toString() + (contador <= 1 ? " mensagem" : " mensagens"));

        int random_int = (int) Math.floor(Math.random() * 2 + 1);
        if (random_int == 1) {
            Div div1 = new Div();
            div1.setSclass("d-flex flex-row justify-content-start mb-4");
            div1.setParent(divChat);
            Image img = new Image();
            img.setStyle("width: 45px; height: 100%;");
            img.setSrc("https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp");
            img.setParent(div1);
            Div div2 = new Div();
            div2.setSclass("p-3 ms-3");
            div2.setStyle("border-radius: 15px; background-color: rgba(57, 192, 237,.2);");
            div2.setParent(div1);
            P p = new P();
            p.setSclass("small mb-0");
            p.setParent(div2);
            Label l = new Label();
            l.setValue(f.lorem().sentence());
            l.setParent(p);
        } else {
            Div div1 = new Div();
            div1.setSclass("d-flex flex-row justify-content-end mb-4");
            div1.setParent(divChat);
            Div div2 = new Div();
            div2.setSclass("p-3 ms-3");
            div2.setStyle("border-radius: 15px; background-color: #fbfbfb;");
            div2.setParent(div1);
            Image img = new Image();
            img.setStyle("width: 45px; height: 100%;");
            img.setSrc("https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava2-bg.webp");
            img.setParent(div1);
            P p = new P();
            p.setSclass("small mb-0");
            p.setParent(div2);
            Label l = new Label();
            l.setValue(f.lorem().sentence());
            l.setParent(p);
        }

    }

}
