/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.orgaos;

import dao.OrgaoDAO;
import factory.OrgaoFactory;
import factory.OrgaoFactory.TipoOrgao;
import model.Orgao;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import utilitarios.ZkUtils;
import zk.custom.Toast;

public class OrgaoController extends Window {

    private Window win;
    private Label cdOrgao; // <--- ALTERADO para Label (somente leitura)
    private Textbox nome;
    private Listbox tipoOrgao;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final ZkUtils zkUtils = new ZkUtils();

    private OrgaoDAO orgaoDao = new OrgaoDAO();

    private Orgao orgao = new Orgao();

    private String urlRetorno = "";
    private String acaoAtual = "";

    public void onCreate() {

        this.win = ((Window) getFellow("winOrgao"));

        this.cdOrgao = (Label) getFellow("cdOrgao"); // <--- ALTERADO
        this.nome = (Textbox) getFellow("nome");
        this.tipoOrgao = (Listbox) getFellow("tipoOrgao");
        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        // pega os parâmetros que vieram da outra página
        Integer orgaoId = (Integer) zkUtils.getParametro("orgao");
        String acao = (String) zkUtils.getParametro("ação");
        urlRetorno = (String) zkUtils.getParametro("url_retorno");
        acaoAtual = acao;

        // mostra os botões de acordo com a ação
        if (acao != null) {
            if (acao.equals("novo")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.cdOrgao.setVisible(false); // ID é ocultado na inclusão
                this.tipoOrgao.setDisabled(false);
            } else if (acao.equals("editar")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.btnExcluir.setVisible(true);
                this.tipoOrgao.setDisabled(true);
            } else if (acao.equals("ler")) {
                this.tipoOrgao.setDisabled(true);
            }
        }

        limparCampos();

        // caso tenha algum id de orgao que veio por parâmetro,
        // então busca esse órgão no banco e popula os campos com os valores
        if (orgaoId != null) {
            orgao = (Orgao) orgaoDao.buscar(orgaoId);
            if (orgao != null) {
                popularCampos();
            }
        }
    }

    /**
     * Limpa os campos do .zul.
     */
    public void limparCampos() {
        this.cdOrgao.setValue("-1"); // ID padrão para "novo"
        this.nome.setRawValue(null);
        this.tipoOrgao.setSelectedIndex(0);
        this.nome.setFocus(true);
    }

    /**
     * Popula os campos .zul com os valores do banco.
     */
    public void popularCampos() {
        zkUtils.popularCampo(this.cdOrgao, (Object) this.orgao.getCdOrgao());
        zkUtils.popularCampo(this.nome, (Object) this.orgao.getNmOrgao());
        
        // Define o tipo de órgão baseado na instância
        TipoOrgao tipo = OrgaoFactory.obterTipo(orgao);
        selecionarTipoOrgao(tipo);
    }

    /**
     * Seleciona o tipo de órgão no listbox baseado no TipoOrgao
     */
    private void selecionarTipoOrgao(TipoOrgao tipo) {
        for (Listitem item : this.tipoOrgao.getItems()) {
            if (item.getValue() != null && item.getValue().equals(tipo.getCodigo())) {
                this.tipoOrgao.setSelectedItem(item);
                break;
            }
        }
    }

    /**
     * Valida se os campos preenchidos estão corretos.
     */
    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.tipoOrgao.getSelectedItem() == null || this.tipoOrgao.getSelectedItem().getValue().equals(TipoOrgao.TODOS.getCodigo())) {
            Clients.showNotification("Tipo de órgão é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.tipoOrgao, "end_center", 0);
            gerouErro = true;
            this.tipoOrgao.setFocus(true);
            return false;
        }

        if (this.nome.getValue().trim().isEmpty()) {
            Clients.showNotification("Nome do Órgão é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.nome.setFocus(true);
        }

        return !gerouErro;
    }

    /**
     * Grava os dados no banco.
     */
    public void gravar() {
        if (validarCampos()) {
            String tipoOrgaoSelecionado = (String) this.tipoOrgao.getSelectedItem().getValue();
            TipoOrgao tipo = TipoOrgao.fromCodigo(tipoOrgaoSelecionado);
            Integer idAtual = Integer.valueOf(this.cdOrgao.getValue());

            // Se for um novo órgão (ID == -1), cria a instância do tipo selecionado
            if (idAtual == -1) {
                orgao = OrgaoFactory.criarOrgao(tipo);
            }
            
            // atualiza os dados do órgão com os valores preenchidos
            orgao.setNmOrgao(this.nome.getValue());

            if (idAtual == -1) {
                // Incluímos no banco (o ID será gerado automaticamente)
                Integer idGerado = OrgaoFactory.salvar(orgao);
                
                if (idGerado != null && idGerado > 0) {
                    Toast.show("Órgão cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir o órgão");
                }
            } else {
                // Update no banco
                orgao.setCdOrgao(idAtual);
                boolean atualizou = OrgaoFactory.atualizar(orgao);
                if (atualizou) {
                    Toast.show("Órgão atualizado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar o órgão");
                }
            }
        }
    }

    /**
     * Exclui o objeto Orgao do banco de dados.
     */
    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir o órgão atual?")) {
            if (this.orgao.getCdOrgao() != null && this.orgao.getCdOrgao() > 0) {
                boolean excluido = OrgaoFactory.excluir(orgao);
                if (excluido) {
                    Toast.show("Órgão excluído com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    limparCampos();
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro ao excluir o órgão");
                }
            } else {
                zkUtils.MensagemErro("Órgão inválido");
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