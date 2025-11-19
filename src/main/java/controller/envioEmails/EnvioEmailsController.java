package controller.envioEmails;

import org.zkoss.zul.Include;
import org.zkoss.zul.Window;
import application.service.Sessao;


public class EnvioEmailsController extends Window {
    
    private Window winEnvio;
    private Include conteudo;

    public void onCreate() {
        Sessao.getInstance().validarSessao(); // Validar Sessão

        this.winEnvio = (Window) getFellow("winEnvioEmail");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);
    }

}
