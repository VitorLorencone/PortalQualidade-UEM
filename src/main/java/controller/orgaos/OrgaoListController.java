package controller.orgaos;
// ... (imports) ...
import dao.OrgaoDAO;
import factory.OrgaoFactory.TipoOrgao;
import model.Orgao;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.Event;
import utilitarios.ZkUtils;
import java.text.Normalizer;
import java.util.List;

public class OrgaoListController extends Window {

    private Window win;

    private Listbox vlCampo;
    private Listbox vlTipoOrgao;
    private Textbox vlPesquisa;
    private Grid resultados;

    private final ZkUtils zkUtils = new ZkUtils();

    private final OrgaoDAO orgaoDao = new OrgaoDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winOrgaoList");

        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlCampo = (Listbox) getFellow("vlCampo");
        this.vlTipoOrgao = (Listbox) getFellow("vlTipoOrgao");
        this.resultados = (Grid) getFellow("resultados");

        filtrar();
    }

    public void filtrar() {
        String filtro = " 1=1 ";
        String ordem = " order by t.nmOrgao ";
        String campoSelecionado = this.vlCampo.getSelectedItem() != null ? (String) this.vlCampo.getSelectedItem().getValue() : "";
        String tipoOrgaoSelecionado = this.vlTipoOrgao.getSelectedItem() != null ? (String) this.vlTipoOrgao.getSelectedItem().getValue() : "";
        String textoDaPesquisa = this.vlPesquisa.getValue();

        // Filtro por tipo de órgão (Usando a lógica de subconsulta para herança JOINED)
        TipoOrgao tipo = TipoOrgao.fromCodigo(tipoOrgaoSelecionado);
        if (tipo != TipoOrgao.TODOS) {
            filtro += gerarFiltroTipo(tipo);
        }

        // Filtro por texto de pesquisa
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();

            if ("nome".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.nmOrgao) LIKE '%" + textoTratado + "%'";
            } else if ("codigo".equals(campoSelecionado)) {
                 // Busca por código, que é um inteiro.
                String codigoTratado = textoTratado.replaceAll("\\D", ""); 
                if (!codigoTratado.isEmpty()) {
                    filtro += " AND t.cdOrgao = " + codigoTratado; 
                }
            } else {
                // Se "Todos os campos" estiver selecionado (busca por nome e código)
                String codigoTratado = textoTratado.replaceAll("\\D", "");
                
                filtro += " AND ( UPPER(t.nmOrgao) LIKE '%" + textoTratado + "%'";
                if (!codigoTratado.isEmpty()) {
                     filtro += " OR t.cdOrgao = " + codigoTratado;
                }
                filtro += " )";
            }
        }

        List<Orgao> orgaos = orgaoDao.listar(filtro, ordem);
        if (orgaos != null) {
            SimpleListModel listModel = new SimpleListModel(orgaos);
            this.resultados.setModel(listModel);
        }
    }

    /**
     * Gera o filtro HQL baseado no tipo de órgão selecionado.
     * Usa subconsultas IN/NOT IN na chave primária (cdOrgao) para maior robustez com JOINED.
     */
    private String gerarFiltroTipo(TipoOrgao tipo) {
        
        final String DIRETORIA_QUERY = "(SELECT d.cdOrgao FROM Diretoria d)";
        final String SETOR_QUERY = "(SELECT s.cdOrgao FROM Setor s)";
        
        switch (tipo) {
            case DIRETORIA:
                return " AND t.cdOrgao IN " + DIRETORIA_QUERY;
            case SETOR:
                return " AND t.cdOrgao IN " + SETOR_QUERY;
            case ORGAO_GENERICO:
                // Filtra Órgãos (t) cujos IDs NÃO existam nas tabelas filhas
                return " AND t.cdOrgao NOT IN " + DIRETORIA_QUERY
                     + " AND t.cdOrgao NOT IN " + SETOR_QUERY;
            case TODOS:
            default:
                return "";
        }
    }

    /**
     * Executado quando é clicado no botão "novo órgão".
     */
    public void novoOrgao() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "ver órgão".
     */
    public void onClickedVerOrgao(Event event) throws Exception {
        Integer cdOrgao = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("orgao", cdOrgao);
        zkUtils.setParametro("ação", "ler");
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "editar órgão".
     */
    public void onClickedAlterarOrgao(Event event) throws Exception {
        Integer cdOrgao = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("orgao", cdOrgao);
        zkUtils.setParametro("ação", "editar");
        this.redirecionar();
    }

    /**
     * Redireciona para a página de cadastro do Órgão.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem);
        include.setSrc("dados/orgaos/orgao.zul"); // vai para a página de cadastro de órgão
    }
}