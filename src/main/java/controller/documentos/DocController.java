/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.documentos;

import dao.DocumentoDAO;
import dao.DiretoriaDAO;
import dao.FuncionarioHUDAO;
import model.Documento;
import model.Diretoria;
import model.EstadoDocumento;
import model.FuncionarioHU;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;
import java.util.List;

public class DocController extends Window {

    private Window win;
    private Label cdDocumento;
    private Textbox nmDocumento;
    private Listbox tpDocumento;
    private Datebox dtCriacao;
    private Datebox dtVencimento;
    private Textbox deDescricao;
    private Listbox estado;
    private Listbox diretoria;
    private Listbox rt;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private DocumentoDAO documentoDao = new DocumentoDAO();
    private DiretoriaDAO diretoriaDao = new DiretoriaDAO();
    private FuncionarioHUDAO funcionarioHUDao = new FuncionarioHUDAO();

    private Documento documento = new Documento();

    private String urlRetorno = "";
    private String acaoAtual = "";

    public void onCreate() {

        this.win = ((Window) getFellow("winDocumento"));

        this.cdDocumento = (Label) getFellow("cdDocumento");
        this.nmDocumento = (Textbox) getFellow("nmDocumento");
        this.tpDocumento = (Listbox) getFellow("tpDocumento");
        this.dtCriacao = (Datebox) getFellow("dtCriacao");
        this.dtVencimento = (Datebox) getFellow("dtVencimento");
        this.deDescricao = (Textbox) getFellow("deDescricao");
        this.estado = (Listbox) getFellow("estado");
        this.diretoria = (Listbox) getFellow("diretoria");
        this.rt = (Listbox) getFellow("rt");
        
        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        // Pega os parâmetros que vieram da outra página
        Integer documentoId = (Integer) zkUtils.getParametro("documento");
        String acao = (String) zkUtils.getParametro("ação");
        urlRetorno = (String) zkUtils.getParametro("url_retorno");
        acaoAtual = acao;

        // Mostra os botões de acordo com a ação
        if (acao != null) {
            if (acao.equals("novo")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.cdDocumento.setVisible(false);
            } else if (acao.equals("editar")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.btnExcluir.setVisible(true);
            } else if (acao.equals("ler")) {
                this.nmDocumento.setDisabled(true);
                this.tpDocumento.setDisabled(true);
                this.dtCriacao.setDisabled(true);
                this.dtVencimento.setDisabled(true);
                this.deDescricao.setDisabled(true);
                this.estado.setDisabled(true);
                this.diretoria.setDisabled(true);
                this.rt.setDisabled(true);
            }
        }

        carregarDiretorias();
        carregarResponsaveisTecnicos();
        limparCampos();

        // Caso tenha algum id de documento que veio por parâmetro,
        // então busca esse documento no banco e popula os campos com os valores
        if (documentoId != null) {
            documento = (Documento) documentoDao.buscar(documentoId);
            if (documento != null) {
                popularCampos();
            }
        }
    }

    /**
     * Carrega as diretorias no listbox dinamicamente do banco de dados
     */
    private void carregarDiretorias() {
        try {
            // Lista todas as diretorias do banco usando filtro "1=1" e ordenação
            List<Diretoria> diretorias = diretoriaDao.listar("1=1", "order by t.nmOrgao");
            
            if (diretorias != null && !diretorias.isEmpty()) {
                // Limpa os itens atuais (exceto o primeiro que é "Selecione...")
                while (this.diretoria.getItemCount() > 1) {
                    this.diretoria.removeItemAt(1);
                }
                
                // Adiciona cada diretoria como um item do listbox
                for (Diretoria dir : diretorias) {
                    Listitem item = new Listitem();
                    item.setValue(dir.getCdOrgao());
                    
                    Listcell cell = new Listcell();
                    cell.setLabel(dir.getNmOrgao());
                    item.appendChild(cell);
                    
                    this.diretoria.appendChild(item);
                }
                
                System.out.println("Diretorias carregadas: " + diretorias.size());
            } else {
                System.out.println("Nenhuma diretoria encontrada no banco de dados");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar diretorias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os funcionários HU (responsáveis técnicos) no listbox dinamicamente do banco
     */
    private void carregarResponsaveisTecnicos() {
        try {
            // Lista todos os funcionários HU do banco usando filtro "1=1" e ordenação
            List<FuncionarioHU> funcionarios = funcionarioHUDao.listar("1=1", "order by t.nome");
            
            if (funcionarios != null && !funcionarios.isEmpty()) {
                // Limpa os itens atuais (exceto o primeiro que é "Selecione...")
                while (this.rt.getItemCount() > 1) {
                    this.rt.removeItemAt(1);
                }
                
                // Adiciona cada funcionário como um item do listbox
                for (FuncionarioHU func : funcionarios) {
                    Listitem item = new Listitem();
                    item.setValue(func.getCdPessoa());
                    
                    Listcell cell = new Listcell();
                    cell.setLabel(func.getNome());
                    item.appendChild(cell);
                    
                    this.rt.appendChild(item);
                }
                
                System.out.println("Funcionários HU carregados: " + funcionarios.size());
            } else {
                System.out.println("Nenhum funcionário HU encontrado no banco de dados");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar responsáveis técnicos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Limpa os campos do .zul.
     */
    public void limparCampos() {
        this.cdDocumento.setValue("-1");
        this.nmDocumento.setRawValue(null);
        this.tpDocumento.setSelectedIndex(0);
        this.dtCriacao.setValue(null);
        this.dtVencimento.setValue(null);
        this.deDescricao.setRawValue(null);
        this.estado.setSelectedIndex(0);
        this.diretoria.setSelectedIndex(0);
        this.rt.setSelectedIndex(0);
        this.nmDocumento.setFocus(true);
    }

    /**
     * Popula os campos .zul com os valores do banco.
     */
    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdDocumento, (Object) this.documento.getCdDocumento());
        zkUtils.popularCampo(this.nmDocumento, (Object) this.documento.getNmDocumento());
        zkUtils.popularCampo(this.deDescricao, (Object) this.documento.getDeDescricao());
        
        // Popular datas
        if (this.documento.getDtCriacao() != null) {
            this.dtCriacao.setValue(this.documento.getDtCriacao());
        }
        if (this.documento.getDtVencimento() != null) {
            this.dtVencimento.setValue(this.documento.getDtVencimento());
        }

        // Popular tipo de documento
        if (this.documento.getTpDocumento() != null) {
            selecionarTipoDocumento(this.documento.getTpDocumento());
        }

        // Popular estado
        if (this.documento.getEstado() != null) {
            selecionarEstado(this.documento.getEstado());
        }

        // Popular diretoria
        if (this.documento.getDiretoria() != null) {
            selecionarDiretoria(this.documento.getDiretoria().getCdOrgao());
        }

        // Popular responsável técnico
        if (this.documento.getRt() != null) {
            selecionarRT(this.documento.getRt().getCdPessoa());
        }
    }

    /**
     * Seleciona o tipo de documento no listbox
     */
    private void selecionarTipoDocumento(String tipo) {
        for (Listitem item : this.tpDocumento.getItems()) {
            if (item.getValue().equals(tipo)) {
                this.tpDocumento.setSelectedItem(item);
                break;
            }
        }
    }

    /**
     * Seleciona o estado no listbox baseado no código String
     */
    private void selecionarEstado(EstadoDocumento estadoDoc) {
        String codigoEstado = estadoDoc.name(); // Retorna "ELABORACAO", "AVALIACAO", etc.
        
        for (Listitem item : this.estado.getItems()) {
            if (item.getValue() != null && item.getValue().equals(codigoEstado)) {
                this.estado.setSelectedItem(item);
                break;
            }
        }
    }

    /**
     * Seleciona a diretoria no listbox
     */
    private void selecionarDiretoria(Integer cdOrgao) {
        for (Listitem item : this.diretoria.getItems()) {
            if (item.getValue() != null && item.getValue().equals(cdOrgao)) {
                this.diretoria.setSelectedItem(item);
                break;
            }
        }
    }

    /**
     * Seleciona o responsável técnico no listbox
     */
    private void selecionarRT(Integer cdPessoa) {
        for (Listitem item : this.rt.getItems()) {
            if (item.getValue() != null && item.getValue().equals(cdPessoa)) {
                this.rt.setSelectedItem(item);
                break;
            }
        }
    }

    /**
     * Valida se os campos preenchidos estão corretos.
     */
    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.nmDocumento.getValue().trim().isEmpty()) {
            Clients.showNotification("Nome do documento é obrigatório!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.nmDocumento, "end_center", 0);
            gerouErro = true;
            this.nmDocumento.setFocus(true);
        }

        if (this.tpDocumento.getSelectedItem() == null) {
            Clients.showNotification("Tipo de documento é obrigatório!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.tpDocumento, "end_center", 0);
            gerouErro = true;
            this.tpDocumento.setFocus(true);
            return false;
        }

        if (this.dtCriacao.getValue() == null) {
            Clients.showNotification("Data de criação é obrigatória!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.dtCriacao, "end_center", 0);
            gerouErro = true;
            this.dtCriacao.setFocus(true);
        }

        if (this.estado.getSelectedItem() == null) {
            Clients.showNotification("Estado do documento é obrigatório!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.estado, "end_center", 0);
            gerouErro = true;
            this.estado.setFocus(true);
            return false;
        }

        // Validação: data de vencimento deve ser posterior à data de criação
        if (this.dtCriacao.getValue() != null && this.dtVencimento.getValue() != null) {
            if (this.dtVencimento.getValue().before(this.dtCriacao.getValue())) {
                Clients.showNotification("Data de vencimento deve ser posterior à data de criação!", 
                    Clients.NOTIFICATION_TYPE_WARNING, this.dtVencimento, "end_center", 0);
                gerouErro = true;
                this.dtVencimento.setFocus(true);
            }
        }

        return !gerouErro;
    }

    /**
     * Grava os dados no banco.
     */
    public void gravar() {
        if (validarCampos()) {
            // Atualiza os dados do documento com os valores preenchidos
            documento.setNmDocumento(this.nmDocumento.getValue());
            documento.setTpDocumento((String) this.tpDocumento.getSelectedItem().getValue());
            documento.setDtCriacao(this.dtCriacao.getValue());
            documento.setDtVencimento(this.dtVencimento.getValue());
            documento.setDeDescricao(this.deDescricao.getValue());
            
            // Atualiza o estado (converte String para ENUM)
            String estadoString = (String) this.estado.getSelectedItem().getValue();
            EstadoDocumento estadoEnum = EstadoDocumento.valueOf(estadoString);
            documento.setEstado(estadoEnum);

            // Atualiza diretoria se selecionada
            if (this.diretoria.getSelectedItem() != null && this.diretoria.getSelectedIndex() > 0) {
                Integer cdOrgao = (Integer) this.diretoria.getSelectedItem().getValue();
                Diretoria dir = diretoriaDao.buscar(cdOrgao);
                documento.setDiretoria(dir);
            } else {
                documento.setDiretoria(null);
            }

            // Atualiza responsável técnico se selecionado
            if (this.rt.getSelectedItem() != null && this.rt.getSelectedIndex() > 0) {
                Integer cdPessoa = (Integer) this.rt.getSelectedItem().getValue();
                FuncionarioHU funcionario = funcionarioHUDao.buscar(cdPessoa);
                documento.setRt(funcionario);
            } else {
                documento.setRt(null);
            }

            if (Integer.parseInt(this.cdDocumento.getValue()) == -1) {
                // Se não tem id significa que é objeto novo, então incluímos no banco
                Integer id = documentoDao.incluirAutoincrementando(documento);
                if (id != null && id > 0) {
                    Toast.show("Documento cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.cdDocumento.setValue(id.toString());
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir");
                    System.out.println("ID retornado: " + id);
                    System.out.println("Documento: " + documento);
                }
            } else {
                // Se já tem id significa que é objeto que já existe no banco, então fazemos update
                documento.setCdDocumento(Integer.valueOf(this.cdDocumento.getValue()));
                boolean atualizou = documentoDao.atualizar(documento);
                if (atualizou) {
                    Toast.show("Documento atualizado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }
        }
    }

    /**
     * Exclui o objeto Documento do banco de dados.
     */
    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir o documento atual?")) {
            if (!this.cdDocumento.getValue().equals("-1")) {
                boolean excluido = documentoDao.excluir(documento);
                if (excluido) {
                    Toast.show("Documento excluído com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    limparCampos();
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro ao excluir o documento");
                }
            } else {
                zkUtils.MensagemErro("Documento inválido");
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