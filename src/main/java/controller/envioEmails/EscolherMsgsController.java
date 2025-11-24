package controller.envioEmails;

import java.text.Normalizer;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import dao.MensagemDAO;
import model.Documento;
import model.FuncionarioHU;
import model.Mensagem;
import utilitarios.Email;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

public class EscolherMsgsController extends Window {
    private Window win;

    private Listbox vlCampo;
    private Textbox vlPesquisa;
    private Grid resultados;

    private String urlRetorno;

    private List<Integer> docsSelecionadosIds;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final MensagemDAO mensagemDao = new MensagemDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winEscolherMsg");

        // Pegamos os campos definidos no .zul e vinculamos à uma variável 
        // para pegarmos ou popularmos com valores
        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlCampo = (Listbox) getFellow("vlCampo");
        this.resultados = (Grid) getFellow("resultados");

        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");
        this.docsSelecionadosIds = (List<Integer>) zkUtils.getParametro("documentosSelecionados");

        filtrar();
    }

    public void filtrar() {
        String filtro = " 1=1 ";
        String ordem = " order by case when t.deCategoria = 'Padrao' then 0 else 1 end, t.deNome ";
        String campoSelecionado = this.vlCampo.getSelectedItem() != null ? (String) this.vlCampo.getSelectedItem().getValue() : "";
        String textoDaPesquisa = this.vlPesquisa.getValue();

        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            // Se for texto, fazemos a normalização
            String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();

            if ("nome".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.deNome) LIKE '%" + textoTratado + "%'";
            } else if ("corpo".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.deCorpo) LIKE '%" + textoTratado + "%'";
            } else if ("frequencia".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.deFrequencia) LIKE '%" + textoTratado + "%'";
            } else {
                // Se "Todos os campos" estiver selecionado
                filtro += " AND ( UPPER(t.deNome) LIKE '%" + textoTratado + "%'"
                        + " OR UPPER(t.deCorpo) LIKE '%" + textoTratado + "%'"
                        + " OR UPPER(t.deFrequencia) LIKE '%" + textoTratado + "%'"
                        + " )";
            }
        }
        
        List<Mensagem> mensagens = mensagemDao.listar(filtro, ordem);
        if (mensagens != null) {
            SimpleListModel listModel = new SimpleListModel(mensagens);
            this.resultados.setModel(listModel);
        }
    }

    /**
     * Volta para a página inicial do envio de mensagens
     */
    public void voltar() {
        Include include = (Include) win.getParent();
        include.setSrc("dados/envioEmail/envio.zul");
    }

    /**
     * Executado quando é clicado no botão "nova mensagem". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */
    public void novaMensagem() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "ver mensagem". Abre a tela de cadastro passando
     * como parâmetro a ação "ler" e o código da mensagem. Essa ação fará com que
     * os botões de Salvar e Excluir NÃO apareça.
     */
    public void onClickedVerMensagem(Event event) throws Exception {
        Integer cdMensagem = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("mensagem", cdMensagem);
        zkUtils.setParametro("ação", "ler");
        zkUtils.setParametro("documentosSelecionados", this.docsSelecionadosIds);
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "editar mensagem". Abre a tela de cadastro
     * passando como parâmetro a ação "editar" e o código da mensagem. Essa ação
     * fará com que os botões de Salvar e Excluir apareça.
     */
    public void onClickedAlterarMensagem(Event event) throws Exception {
        Integer cdMensagem = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("mensagem", cdMensagem);
        zkUtils.setParametro("ação", "editar");
        zkUtils.setParametro("categoria", mensagemDao.getCategoriaById(cdMensagem));
        this.redirecionar();
    }

    /**
     * Redireciona para a página de cadastro da Mensagem. É passado como parâmetro
     * a url de retorno, ou seja, a url que aparecerá no botão "Voltar" e a url
     * que será redirecionado automaticamente após salvar ou excluir os dados.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent(); // Pega o "pai" desta tela, o qual é um "include"
        String urlOrigem = include.getSrc(); // O source do include é a página atual
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem); // A página atual será a url de retorno
        include.setSrc("dados/envioEmail/enviarMsg.zul"); // Vai para a página de cadastro de mensagem (arquivo msg.zul)
    }

}
