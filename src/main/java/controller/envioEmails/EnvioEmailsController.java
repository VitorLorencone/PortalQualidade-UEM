package controller.envioEmails;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import application.service.Sessao;
import dao.DocumentoDAO;
import model.Documento;
import utilitarios.Utils;
import utilitarios.ZkUtils;


public class EnvioEmailsController extends Window {
    
    private Window winEnvio;
    private Include conteudo;

    private Listbox vlCampo;
    private Listbox vlTipoDocumento;
    private Listbox vlEstado;
    private Textbox vlPesquisa;
    private Grid resultados;

    private List<Documento> preResultados;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final DocumentoDAO documentoDao = new DocumentoDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void onCreate() {
        Sessao.getInstance().validarSessao(); // Validar Sessão

        this.winEnvio = (Window) getFellow("winEnvioEmail");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);

        // Pegamos os campos definidos no .zul e vinculamos à uma variável
        // para pegarmos ou popular com valores
        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlCampo = (Listbox) getFellow("vlCampo");
        this.vlTipoDocumento = (Listbox) getFellow("vlTipoDocumento");
        this.vlEstado = (Listbox) getFellow("vlEstado");
        this.resultados = (Grid) getFellow("resultados");

        filtrar();
    }

    public void filtrar() {
        String filtro = " 1=1 ";
        String ordem = " order by t.nmDocumento ";
        String campoSelecionado = this.vlCampo.getSelectedItem() != null ? 
            (String) this.vlCampo.getSelectedItem().getValue() : "";
        String tipoDocumentoSelecionado = this.vlTipoDocumento.getSelectedItem() != null ? 
            (String) this.vlTipoDocumento.getSelectedItem().getValue() : "";
        String estadoSelecionado = this.vlEstado.getSelectedItem() != null ?
            (String) this.vlEstado.getSelectedItem().getValue() : "";
        String textoDaPesquisa = this.vlPesquisa.getValue();

        // Filtro por tipo de documento
        if (tipoDocumentoSelecionado != null && !tipoDocumentoSelecionado.isEmpty()) {
            filtro += " AND t.tpDocumento = '" + tipoDocumentoSelecionado + "'";
        }

        // Filtro por estado
        if (estadoSelecionado != null && !estadoSelecionado.isEmpty()) {
            filtro += " AND t.estado = '" + estadoSelecionado + "'";
        }

        // Filtro por texto de pesquisa
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();

            if ("nome".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.nmDocumento) LIKE '%" + textoTratado + "%'";
            } else if ("descricao".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.deDescricao) LIKE '%" + textoTratado + "%'";
            } else {
                // Se "Todos os campos" estiver selecionado
                filtro += " AND ( UPPER(t.nmDocumento) LIKE '%" + textoTratado + "%'"
                        + " OR UPPER(t.deDescricao) LIKE '%" + textoTratado + "%'"
                        + " )";
            }
        }

        // IMPORTANTE: Usar listarComFetch para carregar relacionamentos LAZY
        List<Documento> documentos = new ArrayList<>();
        documentos = documentoDao.listarComFetch(filtro, ordem);
        this.preResultados = documentos;
        
        if (documentos != null) {
            SimpleListModel listModel = new SimpleListModel(documentos);
            this.resultados.setModel(listModel);
        }
    } // filtrar

    /**
     * Executado quando é clicado no botão "novo documento". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */
    public void novoDocumento() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar("dados/documentos/doc.zul");
    }

    /**
     * Executado quando é clicado em "ver documento". Abre a tela de cadastro passando
     * como parâmetro a ação "ler" e o código do documento. Essa ação fará com que
     * os botões de Salvar e Excluir NÃO apareça.
     */
    public void onClickedVerDocumento(Event event) throws Exception {
        Integer cdDocumento = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("documento", cdDocumento);
        zkUtils.setParametro("ação", "ler");
        this.redirecionar("dados/documentos/doc.zul");
    }

    /**
     * Executado quando é clicado em "editar documento". Abre a tela de cadastro
     * passando como parâmetro a ação "editar" e o código do documento. Essa ação
     * fará com que os botões de Salvar e Excluir apareça.
     */
    public void onClickedAlterarDocumento(Event event) throws Exception {
        Integer cdDocumento = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("documento", cdDocumento);
        zkUtils.setParametro("ação", "editar");
        this.redirecionar("dados/documentos/doc.zul");
    }

    private void redirecionar(String caminho) {
        Include include = (Include) winEnvio.getParent(); // pega o "pai" desta tela, o qual é um "include"
        String urlOrigem = include.getSrc(); // o source do include é a página atual
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem); // a página atual será a url de retorno
        include.setSrc(caminho); // vai para a página de escolha da mensagem
    }

    public void escolherMensagem() {
        List<Integer> docsIds = new ArrayList<>();

        for (Documento doc : this.preResultados) {
            Integer id = doc.getCdDocumento();
            docsIds.add(id);
        }

        zkUtils.setParametro("documentosSelecionados", docsIds);
        this.redirecionar("dados/envioEmail/escolherMsg.zul");
    }

}
