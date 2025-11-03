/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.pessoas;

import dao.PessoaDAO;
import factory.PessoaFactory;
import factory.PessoaFactory.TipoPessoa;
import model.Pessoa;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

public class PessoaController extends Window {

    private Window win;
    private Label cdPessoa;
    private Textbox nome;
    private Textbox email;
    private Textbox celular;
    private Listbox tipoFuncionario;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private PessoaDAO pessoaDao = new PessoaDAO();

    private Pessoa pessoa = new Pessoa();

    private String urlRetorno = "";
    private String acaoAtual = "";

    public void onCreate() {

        this.win = ((Window) getFellow("winPessoa"));

        this.cdPessoa = (Label) getFellow("cdPessoa");
        this.nome = (Textbox) getFellow("nome");
        this.email = (Textbox) getFellow("email");
        this.celular = (Textbox) getFellow("celular");
        this.tipoFuncionario = (Listbox) getFellow("tipoFuncionario");
        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        //pega os parâmetros que vieram da outra página
        Integer pessoaId = (Integer) zkUtils.getParametro("pessoa");
        String acao = (String) zkUtils.getParametro("ação");
        urlRetorno = (String) zkUtils.getParametro("url_retorno");
        acaoAtual = acao;

        //mostra os botões de acordo com a ação
        if (acao != null) {
            if (acao.equals("novo")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.cdPessoa.setVisible(false);
                this.tipoFuncionario.setDisabled(false);
            } else if (acao.equals("editar")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.btnExcluir.setVisible(true);
                this.tipoFuncionario.setDisabled(true);
            } else if (acao.equals("ler")) {
                this.tipoFuncionario.setDisabled(true);
            }
        }

        limparCampos();

        //caso tenha algum id de pessoa que veio por parâmetro, 
        //então busca essa pessoa no banco e popula os campos com os valores
        if (pessoaId != null) {
            pessoa = (Pessoa) pessoaDao.buscar(pessoaId);
            if (pessoa != null) {
                popularCampos();
            }
        }

    }

    /**
     * Limpa os campos do .zul.
     */
    public void limparCampos() {
        this.cdPessoa.setValue("-1");
        this.nome.setRawValue(null);
        this.email.setRawValue(null);
        this.celular.setRawValue(null);
        this.tipoFuncionario.setSelectedIndex(0);
        this.nome.setFocus(true);
    }

    /**
     * Popula os campos .zul com os valores do banco.
     */
    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdPessoa, (Object) this.pessoa.getCdPessoa());
        zkUtils.popularCampo(this.nome, (Object) this.pessoa.getNome());
        zkUtils.popularCampo(this.email, (Object) this.pessoa.getEmail());
        zkUtils.popularCampo(this.celular, (Object) this.pessoa.getCelular());
        
        // Define o tipo de pessoa baseado na instância
        TipoPessoa tipo = PessoaFactory.obterTipo(pessoa);
        selecionarTipoPessoa(tipo);
    }

    /**
     * Seleciona o tipo de pessoa no listbox baseado no TipoPessoa
     */
    private void selecionarTipoPessoa(TipoPessoa tipo) {
        for (Listitem item : this.tipoFuncionario.getItems()) {
            if (item.getValue().equals(tipo.getCodigo())) {
                this.tipoFuncionario.setSelectedItem(item);
                break;
            }
        }
    }

    /**
     * Valida se os campos preenchidos estão corretos.
     */
    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.tipoFuncionario.getSelectedItem() == null) {
            Clients.showNotification("Tipo de pessoa é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.tipoFuncionario, "end_center", 0);
            gerouErro = true;
            this.tipoFuncionario.setFocus(true);
            return false;
        }

        if (this.nome.getValue().trim().isEmpty()) {
            Clients.showNotification("Nome é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.nome.setFocus(true);
        }

        if (this.email.getValue().trim().isEmpty()) {
            Clients.showNotification("Email é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.email, "end_center", 0);
            gerouErro = true;
            this.email.setFocus(true);
        } else if (!utils.validaEmail(this.email.getValue())) {
            Clients.showNotification("Digite um email válido!", Clients.NOTIFICATION_TYPE_WARNING, this.email, "end_center", 0);
            gerouErro = true;
            this.email.setFocus(true);
        }

        return !gerouErro;
    }

    /**
     * Grava os dados no banco.
     */
    public void gravar() {
        if (validarCampos()) {
            String tipoPessoaSelecionada = (String) this.tipoFuncionario.getSelectedItem().getValue();
            TipoPessoa tipo = TipoPessoa.fromCodigo(tipoPessoaSelecionada);

            // Se for uma nova pessoa, cria a instância do tipo selecionado
            if (Integer.parseInt(this.cdPessoa.getValue()) == -1) {
                pessoa = PessoaFactory.criarPessoa(tipo);
            }

            //atualiza os dados da pessoa com os valores preenchidos
            pessoa.setNome(this.nome.getValue());
            pessoa.setEmail(this.email.getValue());
            pessoa.setCelular(this.celular.getValue());

            if (Integer.parseInt(this.cdPessoa.getValue()) == -1) {
                //se não tem id significa que é objeto novo, então incluímos no banco
                Integer id = PessoaFactory.salvar(pessoa);
                if (id != null && id > 0) {
                    Toast.show("Pessoa cadastrada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.cdPessoa.setValue(id.toString());
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir");
                    System.out.println("ID retornado: " + id);
                    System.out.println("Pessoa: " + pessoa);
                }
            } else {
                //se já tem id significa que é objeto que já existe no banco, então fazemos update no banco
                pessoa.setCdPessoa(Integer.valueOf(this.cdPessoa.getValue()));
                boolean atualizou = PessoaFactory.atualizar(pessoa);
                if (atualizou) {
                    Toast.show("Pessoa atualizada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }

        }
    }

    /**
     * Exclui o objeto Pessoa do banco de dados.
     */
    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir a pessoa atual?")) {
            if (!this.cdPessoa.getValue().equals("-1")) {
                boolean excluido = PessoaFactory.excluir(pessoa);
                if (excluido) {
                    Toast.show("Pessoa excluída com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    limparCampos();
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro ao excluir a pessoa");
                }
            } else {
                zkUtils.MensagemErro("Pessoa inválida");
            }
        }
    }

    /**
     * Volta para a url de retorno.
     */
    public void voltar() {
        Include include = (Include) win.getParent();
        include.setSrc(this.urlRetorno);
    }
}