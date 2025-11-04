/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.mensagens;

import dao.DocumentoDAO;
import model.Documento;
import model.EstadoDocumento;
import model.FuncionarioHU;

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

// TESTE
import utilitarios.Email;
import zk.custom.Toast;
import org.zkoss.zk.ui.util.Clients;

public class MsgTesteMailController extends Window {

    private Window win;

    private Listbox vlCampo;
    private Listbox vlTipoDocumento;
    private Listbox vlEstado;
    private Textbox vlPesquisa;
    private Grid resultados;
    private List<Documento> docs;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final DocumentoDAO documentoDao = new DocumentoDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void onCreate() {
        this.win = (Window) getFellow("winTesteMensagensMail");

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
        List<Documento> documentos = documentoDao.listarComFetch(filtro, ordem);
        
        if (documentos != null) {
            SimpleListModel listModel = new SimpleListModel(documentos);
            this.resultados.setModel(listModel);

            // teste 04/11/2025
            this.docs = documentos;
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


    /*
     * BEGIN TESTE ENVIO DE EMAIL - 04/11/2025
    */

    public void enviarEmail() {
        try {
            List<Documento> documentos = this.docs;

            if (documentos == null || documentos.isEmpty()) {
                Clients.showNotification("Nenhum documento encontrado.", 
                        Clients.NOTIFICATION_TYPE_WARNING, resultados, "middle_center", 3000);
                return;
            }

            int enviados = 0;
            for (Documento doc : documentos) {
                FuncionarioHU rt = doc.getRt();
                if (rt == null || rt.getEmail() == null || rt.getEmail().isBlank()) {
                    System.out.println("Documento " + doc.getNmDocumento() + " sem RT com e-mail.");
                    continue;
                }

                String assunto = "Aviso sobre o documento: " + doc.getNmDocumento();
                String mensagem = "<p>Olá " + rt.getNome() + ",</p>"
                        + "<p>O documento <b>" + doc.getNmDocumento() + "</b> "
                        + "está no estado <b>"
                        + (doc.getEstado() != null ? doc.getEstado().toString() : "N/A")
                        + "</b>.</p><p>Por favor, verifique o sistema.</p>";

                Email email = new Email();
                email.setDeEmail("nao-responda@meusistem.com");
                email.setDeNome("Sistema de Documentos");
                email.setParaEmail(rt.getEmail());
                email.setAssunto(assunto);
                email.setMensagem(mensagem);

                if (email.enviarEmailTexto()) {
                    enviados++;
                }
            }

            Toast.show("E-mails enviados: " + enviados + " / " + documentos.size(),
                    "Envio concluído", Toast.Type.SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.show("Erro ao enviar e-mails: " + e.getMessage(), "Erro", Toast.Type.ERROR);
        }
    }

    /*
     * END TESTE
    */
}