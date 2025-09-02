package exemplos.qrcode.controller;

import com.google.zxing.BarcodeFormat;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.image.AImage;
import org.zkoss.zul.Window;
import org.zkoss.zul.Image;
import utilitarios.Barcode;
import utilitarios.IdenticonGenerator;

/**
 *
 * @author alison
 */
public class QrcodeController extends Window {

    private Window win;
    
    private Image imagemQrcode;
    private Image imagemBarcode;
    private Image imagemIdenticonFirstletters;
    private Image imagemIdenticonGithub;

    private final IdenticonGenerator identicon = new IdenticonGenerator();
    private final Barcode barcode = new Barcode();

    public void onCreate() {
        this.win = (Window) getFellow("winQrcode");

        this.imagemQrcode = (Image) getFellow("img_qrcode");
        this.imagemBarcode = (Image) getFellow("img_barcode");
        this.imagemIdenticonGithub = (Image) getFellow("img_identicon_github");
        this.imagemIdenticonFirstletters = (Image) getFellow("img_identicon_firstletter");

        geraQrcode();
        geraBarcode();
        geraIdenticonGithub();
        geraIdenticonFirstletters();
    }

    private void geraQrcode() {
        try {
            byte[] imageData = barcode.createByteArrayImage("Texto que vai no Qrcode.", BarcodeFormat.QR_CODE, 150, 150);
            AImage image = new AImage("", imageData);
            this.imagemQrcode.setContent(image);
        } catch (Exception ex) {
            System.out.println("Erro ao gerar QRCode: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private void geraBarcode() {
        try {
            byte[] imageData = barcode.createByteArrayImage("123456789", BarcodeFormat.CODE_93, 50, 150);
            AImage image = new AImage("", imageData);
            this.imagemBarcode.setContent(image);
        } catch (Exception ex) {
            System.out.println("Erro ao gerar QRCode: " + ExceptionUtils.getStackTrace(ex));
        }
    }

    private void geraIdenticonGithub() {
        AImage image = identicon.getAImage("Nome Pessoa Legal", IdenticonGenerator.GITHUB_STYLE);
        this.imagemIdenticonGithub.setContent(image);
    }

    private void geraIdenticonFirstletters() {
        AImage image = identicon.getAImage("Nome Pessoa Legal", IdenticonGenerator.FIRSTLETTERS_STYLE);
        this.imagemIdenticonFirstletters.setContent(image);
    }

}
