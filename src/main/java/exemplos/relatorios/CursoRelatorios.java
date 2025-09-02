package exemplos.relatorios;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;
import utilitarios.JasperReportsUtil;

public class CursoRelatorios extends Window {

    private Window win;
    private Listbox tpCurso;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    JasperReportsUtil jasperReportsUtils = new JasperReportsUtil();

    public void onCreate() {
        this.win = ((Window) getFellow("win"));
        this.tpCurso = (Listbox) getFellow("tpCurso");
    }

    public void relatorioCursoLista() {

        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        String caminhoRelatorios = request.getRealPath("") + "/relatorios/"; //caminho da pasta de relatórios do sistema, no tomcat, o qual é limpo a cada deploy
        String caminhoImagens = request.getRealPath("") + "/images/"; //caminho da pasta de imagens do sistema, no tomcat, o qual é limpo a cada deploy
        String caminhoServidorDeArquivos = "/home/sistemas/exemplo/"; //caminho do Servidor de Arquivos, caso seja necessário guardar o arquivo para sempre 

        String arquivoJasper = caminhoRelatorios + "cursoLista.jrxml"; //pode ser .jrxml ou .jasper
        String arquivoPdf = "relCursoLista" + "_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf"; //gera um nome único com timestamp

        try {
            //Parâmetros para o Jasper
            HashMap parametros = new HashMap();
            parametros.put("banner", caminhoImagens + "header_relatorio.png");

            //Popula o Jasper
            JasperPrint impressao = jasperReportsUtils.populaArquivoJasper(arquivoJasper, parametros);

            //jasperReportsUtils.geraArquivoPDFDisco(impressao, caminhoRelatorios + arquivoPdf); //exemplo salvando em um diretorio do tomcat
            //jasperReportsUtils.geraArquivoPDFDisco(impressao, caminhoServidorDeArquivos + arquivoPdf); //exemplo salvando no servidor de arquivos
            //Exporta o Jasper para PDF em byte array para download
            byte[] byteArray = jasperReportsUtils.geraArquivoPDFByteArray(impressao);

            //Faz download
            Filedownload.save(byteArray, "application/pdf", arquivoPdf);

        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    public void relatorioCursoTipo() {

        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        String caminhoRelatorios = request.getRealPath("") + "/relatorios/"; //caminho da pasta de relatórios do sistema, no tomcat, o qual é limpo a cada deploy
        String caminhoImagens = request.getRealPath("") + "/images/"; //caminho da pasta de imagens do sistema, no tomcat, o qualé limpo a cada deploy
        String caminhoServidorDeArquivos = "/home/sistemas/exemplo/"; //caminho do Servidor de Arquivos, caso seja necessário guardar o arquivo para sempre 

        String arquivoJasper = caminhoRelatorios + "cursoTipo.jrxml"; //pode ser .jrxml ou .jasper
        String arquivoPdf = "relCursoTipo" + "_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf"; //gera um nome único com timestamp

        try {
            //Parâmetros para o Jasper
            HashMap parametros = new HashMap();
            parametros.put("banner", caminhoImagens + "header_relatorio.png");

            Integer filtro = Integer.valueOf(this.tpCurso.getSelectedItem().getValue().toString());
            parametros.put("tipo", filtro);

            //Popula o Jasper
            JasperPrint impressao = jasperReportsUtils.populaArquivoJasper(arquivoJasper, parametros);

            //Exporta o Jasper para PDF em byte array para download
            byte[] byteArray = jasperReportsUtils.geraArquivoPDFByteArray(impressao);

            //Faz download
            Filedownload.save(byteArray, "application/pdf", arquivoPdf);

        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

}
