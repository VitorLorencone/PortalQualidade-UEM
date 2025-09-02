package utilitarios;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.*;

/**
 * Envio de e-mail usando o SMTP do NPD/UEM. O SMTP do NPD/UEM não precisa de
 * autenticação para ser utilizado, porém seu acesso é liberado somente para
 * sistemas rodando dentro da rede da UEM.
 *
 * @author alison
 */
public class Email {

    private final String HOST = "smtp.uem.br";
    private final Integer PORT = 25;

    private String deEmail = "";
    private String deNome = "";
    private String paraEmail = "";
    private String assunto = "";
    private String mensagem = "";
    private String pathAnexo = "";

    /**
     * Envia e-mail com texto puro como mensagem.
     *
     * @return true caso sucesso ou false caso erro
     */
    public boolean enviarEmailTexto() {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(getParaEmail(), getParaEmail());
            email.setFrom(getDeEmail(), getDeNome());
            email.setSubject(getAssunto());
            email.setMsg(getMensagem());

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    /**
     * Envia e-mail com mensagem em HTML.
     *
     * @return true caso sucesso ou false caso erro
     */
    public boolean enviarEmailHtml() {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(getParaEmail(), getParaEmail());
            email.setFrom(getDeEmail(), getDeNome());
            email.setSubject(getAssunto());
            email.setHtmlMsg(getMensagem());

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    private boolean ExemploEnviaEmailComAnexo() {
        try {

            // configura o email
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            // popula os dados básicos
            email.addTo(getParaEmail(), getParaEmail()); //destinatário
            email.setFrom(getDeEmail(), getDeNome()); // remetente           
            email.setSubject(getAssunto()); // assunto do e-mail  
            email.setMsg(getMensagem()); //conteudo do e-mail           

            // cria o anexo 
            EmailAttachment anexo1 = new EmailAttachment();
            anexo1.setPath(getPathAnexo());
            anexo1.setDisposition(EmailAttachment.ATTACHMENT);
            anexo1.setDescription("Email que envia anexo");
            anexo1.setName(getPathAnexo());
            email.attach(anexo1);

            // envia o email
            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    private boolean ExemploEnviaEmailFormatoHtml() {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setCharset("UTF-8");

            email.addTo(getParaEmail(), getParaEmail()); //destinatário
            email.setFrom(getDeEmail(), getDeNome()); // remetente           
            email.setSubject(getAssunto()); // assunto do e-mail  
            email.setMsg(getMensagem()); //conteudo do e-mail     

            // adiciona uma imagem ao corpo da mensagem e retorna seu id
            URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
            String cid = email.embed(url, "Apache logo");

            // configura a mensagem para o formato HTML
            email.setHtmlMsg("<html>Logo do Apache - <img ></html>");

            // configure uma mensagem alternativa caso o servidor não suporte HTML
            email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

            String messagaID = email.send();

            return true;
        } catch (EmailException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        } catch (MalformedURLException ex) {
            System.out.println("Houve um erro ao enviar e-mail: " + ExceptionUtils.getStackTrace(ex));
            return false;
        }

    }

    public String getDeEmail() {
        return deEmail;
    }

    public void setDeEmail(String deEmail) {
        this.deEmail = deEmail;
    }

    public String getDeNome() {
        return deNome;
    }

    public void setDeNome(String deNome) {
        this.deNome = deNome;
    }

    public String getParaEmail() {
        return paraEmail;
    }

    public void setParaEmail(String paraEmail) {
        this.paraEmail = paraEmail;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getPathAnexo() {
        return pathAnexo;
    }

    public void setPathAnexo(String pathAnexo) {
        this.pathAnexo = pathAnexo;
    }

}
