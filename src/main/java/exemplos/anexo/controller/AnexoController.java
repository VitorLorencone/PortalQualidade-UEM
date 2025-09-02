package exemplos.anexo.controller;

import exemplos.anexo.dao.AnexoDAO;
import exemplos.anexo.model.Anexo;
import application.service.Sessao;
import org.zkoss.zul.Window;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class AnexoController extends Window {

    private Window win;

    private Vlayout mailAnexosListLayout;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final AnexoDAO anexoDAO = new AnexoDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winAnexo");

        this.mailAnexosListLayout = (Vlayout) getFellow("listAnexos");

    }

    public void anexarArquivo(Media arquivo) {
        Long tamanhoMaximoEmMegaBytes = 5L;
        String[] extensoesPermitidas = {
            "jpg", "jpeg", "png", "gif", "bmp",
            "pdf",
            "doc", "docx", "odt",
            "xls", "xlsx", "ods", "ots", "csv",
            "ppt", "pptx", "pps", "odp",
            "xml",
            "zip", "rar", "gz", "tarz", "tar.gz"
        };
        if (zkUtils.validarArquivo(arquivo, this.mailAnexosListLayout, tamanhoMaximoEmMegaBytes, extensoesPermitidas)) {
            this.inserirArquivoNaGrid(arquivo);
        }
    }

    private void inserirArquivoNaGrid(Media arquivo) {

        final Hlayout hl = new Hlayout();

        //O arquivo em si
        hl.setAttribute("arquivo", arquivo);

        //Nome do arquivo
        A baixar = new A(arquivo.getName());
        baixar.addEventListener(Events.ON_CLICK, (Event event) -> {
            Filedownload.save((Media) hl.getAttribute("arquivo"));
        });
        hl.appendChild(baixar);

        //Tamanho do arquivo
        try {
            int tamanhoInt = IOUtils.toByteArray(arquivo.getStreamData()).length;
            String tamanhoString = FileUtils.byteCountToDisplaySize(Long.parseLong(String.valueOf(tamanhoInt)));
            hl.appendChild(new Label("(" + tamanhoString + ")"));
        } catch (IOException ex) {
            hl.appendChild(new Label("(???)"));
        }

        //Botão remover
        Button botaoRemover = new Button();
        botaoRemover.setSclass("btn btn-transition btn btn-outline-link bg-transparent pb-o pt-0");
        botaoRemover.setIconSclass("z-icon-times");
        botaoRemover.setTooltiptext("Remover");
        botaoRemover.addEventListener(Events.ON_CLICK, (Event event) -> {
            hl.detach();
        });
        hl.appendChild(botaoRemover);

        this.mailAnexosListLayout.appendChild(hl);

    }

    public void salvar() {
        List<Hlayout> listaAnexosHlayout = mailAnexosListLayout.getChildren();
        for (Hlayout h : listaAnexosHlayout) {
            Media arquivo = (Media) h.getAttribute("arquivo");

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String dataHora = dateFormat.format(new Date());
            String diretorioArquivoDisco = "/home/sistemas/SISTEMALEGAL/anexos/CODIGOPESSOA/" + Calendar.getInstance().get(Calendar.YEAR) + "/"; //verificar qual a melhor estrutura de árvore para salvar, dependendo da seu caso, tente criar uma estrutura de modo que tenha poucos arquivos em cada pasta, para assim maior obter um acesso mais rapido
            String nomeArquivoDisco = "CDPESSOA123" + "-" + "TIPOCPF" + "-" + dataHora + "." + arquivo.getFormat(); //verificar qual o melhor nome do arquivo, dependendo do seu caso, tente colocar um codigo do dono do arquivo e uma data e hora, prevendo nao ter nomes iguais em um mesmo diretorio
            String nomeArquivoOriginal = arquivo.getName();

            Anexo anexo = new Anexo();
            anexo.setTpDocumento("TIPOCPF"); //defina o tipo do arquivo, se será string ou será uma tabela de relacionamento do id do tipo 
            anexo.setDsUsuarioInserido(Sessao.getInstance().getUsuario() != null ? Sessao.getInstance().getUsuario().getUsername() : ""); //verificar se está preenchido corretamente
            anexo.setDhInserido(new Date()); //a data e hora de insercao
            anexo.setDsDirArquivo(diretorioArquivoDisco); //o diretorio que sera salvo o arquivo, atente-se que este valor nao precisa estar salvo no banco se voce pode deduzir qual é o caminho (recomendavel)
            anexo.setNmArquivoServidor(nomeArquivoDisco); //nome do arquivo em disco tem que ser unico para nao correr o risco de ter arquivo sobrescrito (por exemplo, 1234_CPF_20240101_14h35m55s.jpg), e este nome sera usado somente para savar ou buscar o arquivo em disco
            anexo.setNmArquivoOriginal(nomeArquivoOriginal); //nome do arquivo original é o nome original do arquivo (por exemplo, foto_do_meu_cpf.jpg) e sera este nome que sera usado para mostrar ao usuario

            try {
                Toast.show("Salvando arquivo em disco em " + diretorioArquivoDisco + nomeArquivoDisco + "!", "Salvando", Toast.Type.INFO);
                zkUtils.gravarArquivo(arquivo, diretorioArquivoDisco + nomeArquivoDisco);

                Toast.show("Salvando arquivo " + nomeArquivoOriginal + " dados no arquivo no banco de dados!", "Salvando", Toast.Type.INFO);
                //anexoDAO.incluirAutoincrementando(anexo); //deve-se fazer o mapeamento do model para funcionar a inserção no banco de dados
            } catch (Exception ex) {
                zkUtils.MensagemErro("Houve um erro ao salvar o arquivo: " + ex.getMessage());
            }

        }
    }

}
