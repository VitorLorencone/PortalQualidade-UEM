package controller.menuBar;

import org.zkoss.zul.Div;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.util.Clients;
import application.service.Sessao;

public class MenuController extends Window {

    private Window winMenu;
    private Include conteudo;
    private Div mensagemInicial;

    public void onCreate() {
        Sessao.getInstance().validarSessao(); // Validar Sessão

        this.winMenu = (Window) getFellow("winMenu");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);
        this.mensagemInicial = (Div) getFellowIfAny("mensagemInicial", true);
    }

    // FUNÇÕES ATUAIS

    public void abrirPaginaLogin() {
        Executions.sendRedirect("/login");
    }

    public void voltarPortal() {
        Executions.sendRedirect("/");
    }

    public void abrirPaginaDocumentos() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("/dados/documentos/docs.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/documentos');");
    }

    public void abrirPaginaPessoas() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("/dados/pessoas/pessoas.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/pessoas');");
    }

    public void abrirPaginaMensagens() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("/dados/mensagens/msgs.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/mensagens');");
    }

    public void abrirPaginaRegras() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("/dados/regras/regras.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/regras');");
    }

    public void abrirPaginaOrgaos() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("/dados/orgaos/orgaos.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/orgaos');");
    }

    public void abrirPaginaPerfil() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("/dados/profile/profile.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/perfil');");
    }

    // BEGIN TESTE - ADD 04/11/2025
    public void abrirPaginaEnvioEmails() {
        this.mensagemInicial.setVisible(false);
        this.conteudo.setSrc("dados/envioEmail/envio.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/envio');");
    }
}
