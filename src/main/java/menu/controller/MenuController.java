package menu.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.util.Clients;
import application.service.Sessao;

public class MenuController extends Window {

    private Window winMenu;
    private Include conteudo;

    public void onCreate() {
        Sessao.getInstance().validarSessao(); // Validar Sessão

        this.winMenu = (Window) getFellow("winMenu");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);
    }

    // FUNÇÕES ATUAIS

    public void abrirPaginaLogin() {
        Executions.sendRedirect("/login");
    }

    public void abrirPaginaDocumentos() {
        this.conteudo.setSrc("/dados/documentos/docs.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/documentos');");
    }

    public void abrirPaginaPessoas() {
        this.conteudo.setSrc("/dados/pessoas/pessoas.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/pessoas');");
    }

    public void abrirPaginaMensagens() {
        this.conteudo.setSrc("/dados/mensagens/msgs.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/mensagens');");
    }

    public void abrirPaginaRegras() {
        this.conteudo.setSrc("/dados/regras/regras.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/orgaos');");
    }

    public void abrirPaginaOrgaos() {
        this.conteudo.setSrc("/dados/orgaos/orgaos.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/orgaos');");
    }

    public void abrirPaginaPerfil() {
        this.conteudo.setSrc("/dados/profile/profile.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/perfil');");
    }
    
    // FUNÇÕES ANTIGAS
    // Deixando aqui para compatibilidade com as páginas antigas que o NPD disponibilizou
    
    public void abrirComponentes() {
        this.conteudo.setSrc("/paginas/zkbootstrap.zul");
    }
}
