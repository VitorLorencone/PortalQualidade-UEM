/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */
package controller.mensagens;

import dao.MensagemDAO;
import model.Mensagem;

// ELEMENTOS GRÁFICOS E OUTROS DO ZK
import org.zkoss.zul.Label;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Button;
import org.zkoss.zk.ui.event.Event;

import utilitarios.Utils;
import utilitarios.ZkUtils;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class MsgListController extends Window {

    private Window win;
    private Include conteudo;
    private Include menubar;

    // ELEMENTOS GRÁFICOS
    private Label LBLtituloPagina;
    private Listbox vlCampo;
    private Textbox vlPesquisa;
    private Grid resultados;
    private Button btnVoltar;

    // PARAMETROS ZK - recebidos
    private List<Integer> docSelecionados = new ArrayList<>();
    private String intencao;

    // Outras informações
    private String urlRetorno;
    private String urlAtual;
    private Include telaMae;


    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final MensagemDAO mensagemDao = new MensagemDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winMensagemList");
        this.conteudo = (Include) getFellowIfAny("conteudo", true);
        this.menubar = (Include) getFellowIfAny("menubar", true);
        System.out.println(">> DEBUG msgListcontroller");
        System.out.println("src menubar");
        System.out.println(this.menubar.getSrc());

        // Pegamos os campos definidos no .zul e vinculamos à uma variável 
        // para pegarmos ou popularmos com valores
        this.LBLtituloPagina = (Label) getFellow("LBLtituloPagina");
        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlCampo = (Listbox) getFellow("vlCampo");
        this.resultados = (Grid) getFellow("resultados");
        this.btnVoltar = (Button) getFellow("btnvoltar");
        this.btnVoltar.setVisible(false);

        // PARAMETROS ZK
        this.docSelecionados = (List<Integer>) zkUtils.getParametroSessao("docSelecionados");
        zkUtils.setParametroSessao("docSelecionados", this.docSelecionados);
        this.intencao = (String) zkUtils.getParametroSessao("intencao");
        zkUtils.setParametroSessao("intencao", this.intencao);
        this.urlRetorno = (String) zkUtils.getParametro("url_retorno");

        // Outras informações
        this.telaMae = (Include) win.getParent();
        this.urlAtual = telaMae.getSrc();

        // Atualizando a interface
        if (this.intencao.equalsIgnoreCase("enviar")) {
            this.LBLtituloPagina.setValue("Escolha sua mensagem");
            this.btnVoltar.setVisible(true);
        } 

        // debug
        System.out.println(">>> DEBUG MSGLISTCONTROLLER");
        System.out.println("parametro intencao");
        System.out.println(this.intencao);
        System.out.println("parametro docSelecionados");
        System.out.println(this.docSelecionados);

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

    public void voltar() {
        System.out.println(">>> DEBUG btn Voltar : funcao voltar chamada");
        System.out.printf("url de retorno : ");
        System.out.println(this.urlRetorno);
        this.menubar.setSrc("menubar.zul");
        this.conteudo.setSrc(this.urlRetorno);
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
        include.setSrc("dados/mensagens/msg.zul"); // Vai para a página de cadastro de mensagem (arquivo msg.zul)
    }
}