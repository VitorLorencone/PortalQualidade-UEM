/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.documentos;

import dao.DocumentoDAO;
import model.Documento;
import model.EstadoDocumento;
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
import java.text.SimpleDateFormat;
import java.util.List;

public class DocListController extends Window {

    private Window win;

    private Listbox vlCampo;
    private Listbox vlTipoDocumento;
    private Listbox vlEstado;
    private Textbox vlPesquisa;
    private Grid resultados;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final DocumentoDAO documentoDao = new DocumentoDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void onCreate() {
        this.win = (Window) getFellow("winDocumentoList");

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
    String ordem = " ORDER BY t.nmDocumento ";

    String campoSelecionado = this.vlCampo.getSelectedItem() != null ?
            (String) this.vlCampo.getSelectedItem().getValue() : "";

    String tipoDocumento = this.vlTipoDocumento.getSelectedItem() != null ?
            (String) this.vlTipoDocumento.getSelectedItem().getValue() : "";

    String estado = this.vlEstado.getSelectedItem() != null ?
            (String) this.vlEstado.getSelectedItem().getValue() : "";

    String textoPesquisa = this.vlPesquisa.getValue();

    if (tipoDocumento != null && !tipoDocumento.isEmpty()) {
        filtro += " AND t.tpDocumento = '" + tipoDocumento + "'";
    }

    if (estado != null && !estado.isEmpty()) {
        filtro += " AND t.estado = '" + estado + "'";
    }

    if (textoPesquisa != null && !textoPesquisa.trim().isEmpty()) {

        String texto = Normalizer.normalize(textoPesquisa, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .trim()
                .toUpperCase();

        if ("nome".equals(campoSelecionado)) {

            filtro += " AND UPPER(t.nmDocumento) LIKE '%" + texto + "%'";

        } else if ("descricao".equals(campoSelecionado)) {

            filtro += " AND UPPER(t.deDescricao) LIKE '%" + texto + "%'";

        } else if ("data_cri".equals(campoSelecionado)) {

            filtro += " AND DATE_FORMAT(t.dtCriacao, '%d/%m/%y') LIKE '%" + texto + "%'";

        } else if ("data_ven".equals(campoSelecionado)) {

            filtro += " AND DATE_FORMAT(t.dtVencimento, '%d/%m/%y') LIKE '%" + texto + "%'";

        } else if ("diretoria".equals(campoSelecionado)) {

            filtro += " AND UPPER(t.diretoria.nmOrgao) LIKE '%" + texto + "%'";

        } else if ("rt".equals(campoSelecionado)) {

            filtro += " AND UPPER(t.rt.nome) LIKE '%" + texto + "%'";

        } else if ("setor".equals(campoSelecionado)) {
            filtro += " AND UPPER(s.nmOrgao) LIKE '%" + texto + "%'";

        } else if ("avaliador".equals(campoSelecionado)) {
            filtro += " AND UPPER(av.nome) LIKE '%" + texto + "%'";

        } else if ("qualidade".equals(campoSelecionado)) {
            filtro += " AND UPPER(fq.nome) LIKE '%" + texto + "%'";
            
        } else {
            filtro += " AND ( "
                    + "UPPER(t.nmDocumento) LIKE '%" + texto + "%' "
                    + "OR UPPER(t.deDescricao) LIKE '%" + texto + "%' "
                    + "OR DATE_FORMAT(t.dtCriacao, '%d/%m/%y') LIKE '%" + texto + "%' "
                    + "OR DATE_FORMAT(t.dtVencimento, '%d/%m/%y') LIKE '%" + texto + "%' "
                    + "OR UPPER(t.diretoria.nmOrgao) LIKE '%" + texto + "%' "
                    + "OR UPPER(t.rt.nome) LIKE '%" + texto + "%' "
                    + "OR UPPER(s.nmOrgao) LIKE '%" + texto + "%' "
                    + "OR UPPER(av.nome) LIKE '%" + texto + "%' "
                    + "OR UPPER(fq.nome) LIKE '%" + texto + "%' "
                    + ")";
        }
    }

    List<Documento> documentos = documentoDao.listarComFetch(filtro, ordem);

    if (documentos != null) {
        this.resultados.setModel(new SimpleListModel(documentos));
    }
}

    /**
     * Executado quando é clicado no botão "novo documento". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */
    public void novoDocumento() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
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
        this.redirecionar();
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
        this.redirecionar();
    }

    /**
     * Redireciona para a página de cadastro do Documento. É passado como parâmetro
     * a url de retorno, ou seja, a url que aparecerá no botão "Voltar" e a url
     * que será redirecionado automaticamente após salvar ou excluir os dados.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent(); // pega o "pai" desta tela, o qual é um "include"
        String urlOrigem = include.getSrc(); // o source do include é a página atual
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem); // a página atual será a url de retorno
        include.setSrc("dados/documentos/doc.zul"); // vai para a página de cadastro de documento
    }
}