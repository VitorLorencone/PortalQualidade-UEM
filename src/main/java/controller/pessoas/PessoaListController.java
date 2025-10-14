package controller.pessoas;

import dao.PessoaDAO;
import factory.PessoaFactory;
import factory.PessoaFactory.TipoPessoa;
import model.Pessoa;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.Event;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import java.text.Normalizer;
import java.util.List;

public class PessoaListController extends Window {

    private Window win;

    private Listbox vlCampo;
    private Listbox vlTipoPessoa;
    private Textbox vlPesquisa;
    private Grid resultados;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final PessoaDAO pessoaDao = new PessoaDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winPessoaList");

        //pegamos os campos definidos no .zul e vinculamos à uma variável 
        //para pegarmos ou pularmos com valores
        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlCampo = (Listbox) getFellow("vlCampo");
        this.vlTipoPessoa = (Listbox) getFellow("vlTipoPessoa");
        this.resultados = (Grid) getFellow("resultados");

        filtrar();
    }

    public void filtrar() {
        String filtro = " 1=1 ";
        String ordem = " order by t.nome ";
        String campoSelecionado = this.vlCampo.getSelectedItem() != null ? (String) this.vlCampo.getSelectedItem().getValue() : "";
        String tipoPessoaSelecionado = this.vlTipoPessoa.getSelectedItem() != null ? (String) this.vlTipoPessoa.getSelectedItem().getValue() : "";
        String textoDaPesquisa = this.vlPesquisa.getValue();

        // Filtro por tipo de pessoa
        TipoPessoa tipo = TipoPessoa.fromCodigo(tipoPessoaSelecionado);
        if (tipo != TipoPessoa.TODOS) {
            filtro += gerarFiltroTipo(tipo);
        }

        // Filtro por texto de pesquisa
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();

            if ("nome".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.nome) LIKE '%" + textoTratado + "%'";
            } else if ("email".equals(campoSelecionado)) {
                filtro += " AND UPPER(t.email) LIKE '%" + textoTratado + "%'";
            } else if ("celular".equals(campoSelecionado)) {
                String celularTratado = textoDaPesquisa != null ? textoTratado.replaceAll("\\D", "") : "";
                filtro += " AND REGEXP_REPLACE(t.celular, '[^0-9]', '') LIKE '%" + celularTratado + "%'";
            } else {
                // Se "Todos os campos" estiver selecionado
                String celularTratado = textoDaPesquisa != null ? textoTratado.replaceAll("\\D", "") : "";
                filtro += " AND ( UPPER(t.nome) LIKE '%" + textoTratado + "%'"
                        + " OR UPPER(t.email) LIKE '%" + textoTratado + "%'"
                        + (celularTratado.isEmpty() ? "" : " OR REGEXP_REPLACE(t.celular, '[^0-9]', '') LIKE '%" + celularTratado + "%'")
                        + " )";
            }
        }

        List<Pessoa> pessoas = pessoaDao.listar(filtro, ordem);
        if (pessoas != null) {
            SimpleListModel listModel = new SimpleListModel(pessoas);
            this.resultados.setModel(listModel);
        }
    }

    /**
     * Gera o filtro SQL baseado no tipo de pessoa selecionado.
     * Usa a anotação @Entity para determinar a tabela específica.
     */
    private String gerarFiltroTipo(TipoPessoa tipo) {
        // Define as subqueries HQL que buscam o ID (cdPessoa) nas tabelas filhas.
        // É crucial que 'cdPessoa' seja o nome da sua chave primária na classe Pessoa.
        final String HU_QUERY = "(SELECT f.cdPessoa FROM FuncionarioHU f)";
        final String QUALIDADE_QUERY = "(SELECT q.cdPessoa FROM FuncionarioQualidade q)";

        switch (tipo) {
            case FUNCIONARIO_HU:
                // FUNCIONARIO_HU: Filtra Pessoas (t) cujos IDs existem na tabela FuncionarioHU
                return " AND t.cdPessoa IN " + HU_QUERY;
                
            case FUNCIONARIO_QUALIDADE:
                // FUNCIONARIO_QUALIDADE: Filtra Pessoas (t) cujos IDs existem na tabela FuncionarioQualidade
                return " AND t.cdPessoa IN " + QUALIDADE_QUERY;
                
            case PESSOA_GENERICA:
                // PESSOA GENÉRICA (SOMENTE):
                // Filtra Pessoas (t) cujos IDs NÃO existam nas tabelas filhas (é uma Pessoa que não é subclass).
                return " AND t.cdPessoa NOT IN " + HU_QUERY
                    + " AND t.cdPessoa NOT IN " + QUALIDADE_QUERY;
                
            case TODOS:
            default:
                // TODOS / Sem Filtro: Retorna vazio.
                return "";
        }
    }

    /**
     * Executado quando é clicado no botão "nova pessoa". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */
    public void novaPessoa() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "ver pessoa". Abre a tela de cadastro passando
     * como parâmetro a ação "ler" e o código da pessoa. Essa ação fará com que
     * os botões de Salvar e Excluir NÃO apareça.
     */
    public void onClickedVerPessoa(Event event) throws Exception {
        Integer cdPessoa = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("pessoa", cdPessoa);
        zkUtils.setParametro("ação", "ler");
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "editar pessoa". Abre a tela de cadastro
     * passando como parâmetro a ação "editar" e o código da pessoa. Essa ação
     * fará com que os botões de Salvar e Excluir apareça.
     */
    public void onClickedAlterarPessoa(Event event) throws Exception {
        Integer cdPessoa = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("pessoa", cdPessoa);
        zkUtils.setParametro("ação", "editar");
        this.redirecionar();
    }

    /**
     * Redireciona para a página de cadastro da Pessoa. É passado como parâmetro
     * a url de retorno, ou seja, a url que aparecerá no botão "Voltar" e a url
     * que será redirecionado automaticamente após salvar ou excluir os dados.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent(); //pega o "pai" desta tela, o qual é um "include"
        String urlOrigem = include.getSrc(); //o source do include é a página atual
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem); //a página atual será a url de retorno
        include.setSrc("dados/pessoas/pessoa.zul"); //vai para a página de cadastro de pessoa (arquivo pessoa.zul)
    }
}