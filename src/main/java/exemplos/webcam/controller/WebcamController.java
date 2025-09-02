package exemplos.webcam.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.zul.Window;
import org.zkoss.zul.Image;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class WebcamController extends Window {

    private Window win;
    private Image imagem;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    public void onCreate() {
        this.win = (Window) getFellow("winWebcam");
        this.imagem = (Image) getFellow("foto");
    }

    public void salvarFoto() {

        if (this.imagem.getSrc() == null) {
            zkUtils.MensagemErro("Nenhum foto foi tirada!");
            return;
        }

        if (this.imagem.getSrc().length() <= 22) {
            zkUtils.MensagemErro("A foto não é válida!");
            return;
        }

        System.out.println("CONTEÚDO DA IMAGEM EM BASE64:");
        System.out.println(this.imagem.getSrc());

        //Transforma a imagem em base64 para BufferedImage para salvar em disco
        BufferedImage image = null;
        byte[] imageByte = Base64.getDecoder().decode(this.imagem.getSrc().substring(22));
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageByte)) {
            image = ImageIO.read(bis);
        } catch (IOException ex) {
            System.out.println("Houve um erro ao converver imagem em base64 para array de bytes: " + ExceptionUtils.getStackTrace(ex));
        }

        //Salva em disco
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String dataHora = dateFormat.format(new Date());
            String diretorioArquivoDisco = "/home/sistemas/SISTEMALEGAL/anexos/CODIGOPESSOA/" + Calendar.getInstance().get(Calendar.YEAR) + "/"; //verificar qual a melhor estrutura de árvore para salvar, dependendo da seu caso, tente criar uma estrutura de modo que tenha poucos arquivos em cada pasta, para assim maior obter um acesso mais rapido
            String nomeArquivoDisco = "CDPESSOA123" + "-" + "TIPOCPF" + "-" + dataHora + ".png"; //verificar qual o melhor nome do arquivo, dependendo do seu caso, tente colocar um codigo do dono do arquivo e uma data e hora, prevendo nao ter nomes iguais em um mesmo diretorio

            Toast.show("Salvando arquivo em disco em " + diretorioArquivoDisco + nomeArquivoDisco + "!", "Salvando", Toast.Type.INFO);
            ImageIO.write(image, "PNG", new File(diretorioArquivoDisco + nomeArquivoDisco));
        } catch (IOException ex) {
            System.out.println("Houve um erro ao salvar imagem em disco: " + ExceptionUtils.getStackTrace(ex));
        }
    }
}
