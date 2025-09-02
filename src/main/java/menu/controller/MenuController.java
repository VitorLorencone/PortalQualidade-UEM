package menu.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;

public class MenuController extends Window {

    private Window winMenu;
    private Include conteudo;

    public void onCreate() {

        this.winMenu = (Window) getFellow("winMenu");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);

    }
    
    public void abrirLogin() {
        Executions.sendRedirect("/login");
    }
    
    public void abrirComponentes() {
        this.conteudo.setSrc("/paginas/zkbootstrap.zul");
    }

    public void abrirCurso() {
        this.conteudo.setSrc("/cadastros/curso/cursoList.zul");
    }

    public void abrirInscricao() {
        this.conteudo.setSrc("/cadastros/inscricao/inscricaoList.zul");
    }

    public void abrirEndereco() {
        this.conteudo.setSrc("/exemplos/endereco/endereco.zul");
    }

    public void abrirAnexos() {
        this.conteudo.setSrc("/exemplos/anexo/anexo.zul");
    }

    public void abrirThread() {
        this.conteudo.setSrc("/exemplos/thread/thread.zul");
    }

    public void abrirEmail() {
        this.conteudo.setSrc("/exemplos/email/email.zul");
    }

    public void abrirGraficos() {
        this.conteudo.setSrc("/exemplos/graficos/graficos.zul");
    }

    public void abrirServerpush() {
        this.conteudo.setSrc("/exemplos/serverpush/serverpush.zul");
    }

    public void abrirWebcam() {
        this.conteudo.setSrc("/exemplos/webcam/webcam.zul");
    }

    public void abrirQrcode() {
        this.conteudo.setSrc("/exemplos/qrcode/qrcode.zul");
    }

    public void abrirMascara() {
        this.conteudo.setSrc("/exemplos/mascara/mascara.zul");
    }
    
    public void abrirCarrossel() {
        this.conteudo.setSrc("/exemplos/carrossel/carrossel.zul");
    }
    
    public void abrirRest() {
        this.conteudo.setSrc("/exemplos/rest/rest.zul");
    }
    
    public void abrirCaptcha() {
        this.conteudo.setSrc("/exemplos/captcha/captcha.zul");
    }
    
    public void abrirModal() {
        this.conteudo.setSrc("/exemplos/modal/modalIndex.zul");
    }

    public void abrirCursoRelatorio() {
        this.conteudo.setSrc("/exemplos/relatorio/relatorioCurso.zul");
    }
    
    

}
