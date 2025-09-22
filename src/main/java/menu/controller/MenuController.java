package menu.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.util.Clients;

public class MenuController extends Window {

    private Window winMenu;
    private Include conteudo;

    public void onCreate() {
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
        Clients.evalJavaScript("window.history.pushState(null, '', '/regras');");
    }

    public void abrirPaginaRelatorios() {
        this.conteudo.setSrc("/dados/relatorios/rel.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/relatorios');");
    }

    public void abrirPaginaPerfil() {
        this.conteudo.setSrc("/dados/profile/profile.zul");
        Clients.evalJavaScript("window.history.pushState(null, '', '/perfil');");
    }
    
    // FUNÇÕES ANTIGAS
    // Deixando aqui para compatibilidade com as páginas antigas que o NPD disponibilizou

    public void abrirLogin() {
        Executions.sendRedirect("/oldNPD/login");
    }
    
    public void abrirComponentes() {
        this.conteudo.setSrc("/paginas/zkbootstrap.zul");
    }

    public void abrirCurso() {
        this.conteudo.setSrc("/oldNPD/cadastros/curso/cursoList.zul");
    }

    public void abrirInscricao() {
        this.conteudo.setSrc("/oldNPD/cadastros/inscricao/inscricaoList.zul");
    }

    public void abrirEndereco() {
        this.conteudo.setSrc("/oldNPD/exemplos/endereco/endereco.zul");
    }

    public void abrirAnexos() {
        this.conteudo.setSrc("/oldNPD/exemplos/anexo/anexo.zul");
    }

    public void abrirThread() {
        this.conteudo.setSrc("/oldNPD/exemplos/thread/thread.zul");
    }

    public void abrirEmail() {
        this.conteudo.setSrc("/oldNPD/exemplos/email/email.zul");
    }

    public void abrirGraficos() {
        this.conteudo.setSrc("/oldNPD/exemplos/graficos/graficos.zul");
    }

    public void abrirServerpush() {
        this.conteudo.setSrc("/oldNPD/exemplos/serverpush/serverpush.zul");
    }

    public void abrirWebcam() {
        this.conteudo.setSrc("/oldNPD/exemplos/webcam/webcam.zul");
    }

    public void abrirQrcode() {
        this.conteudo.setSrc("/oldNPD/exemplos/qrcode/qrcode.zul");
    }

    public void abrirMascara() {
        this.conteudo.setSrc("/oldNPD/exemplos/mascara/mascara.zul");
    }
    
    public void abrirCarrossel() {
        this.conteudo.setSrc("/oldNPD/exemplos/carrossel/carrossel.zul");
    }
    
    public void abrirRest() {
        this.conteudo.setSrc("/oldNPD/exemplos/rest/rest.zul");
    }
    
    public void abrirCaptcha() {
        this.conteudo.setSrc("/oldNPD/exemplos/captcha/captcha.zul");
    }
    
    public void abrirModal() {
        this.conteudo.setSrc("/oldNPD/exemplos/modal/modalIndex.zul");
    }

    public void abrirCursoRelatorio() {
        this.conteudo.setSrc("/oldNPD/exemplos/relatorio/relatorioCurso.zul");
    }
}
