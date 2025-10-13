/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.pessoas;

import dao.PessoaDAO;
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
import org.apache.commons.lang3.math.NumberUtils;

public class PessoaListController extends Window {

    private Window win;

    private Listbox vlCampo;
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
        this.resultados = (Grid) getFellow("resultados");

        filtrar();
    }

    public void filtrar() {
        String filtro = " 1=1 ";
        String ordem = " order by t.nome ";  // ✅ Usa o atributo da classe com alias 't'
        
        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            if (NumberUtils.isCreatable(textoDaPesquisa)) {
                // Se for número filtramos por código
                filtro += " AND t.cdPessoa = " + textoDaPesquisa;  // ✅ Adiciona alias 't'
            } else {
                // Se for texto, fazemos a normalização
                String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
                
                // ✅ Usa o atributo da classe com alias 't'
                filtro += " AND UPPER(t.nome) like '%" + textoTratado + "%'";
            }
        }
        
        List<Pessoa> pessoas = pessoaDao.listar(filtro, ordem);
        if (pessoas != null) {
            SimpleListModel listModel = new SimpleListModel(pessoas);
            this.resultados.setModel(listModel);
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