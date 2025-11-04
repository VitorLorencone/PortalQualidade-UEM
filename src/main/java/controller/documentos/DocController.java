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
import dao.SetorDAO;
import dao.FuncionarioQualidadeDAO;
import model.Documento;
import model.Diretoria;
import model.EstadoDocumento;
import model.FuncionarioHU;
import model.Setor;
import model.FuncionarioQualidade;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import com.itextpdf.text.ListItem;

import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    
    // Componentes para gerenciar Avaliadores
    private Grid gridAvaliadoresVinculados;
    private Listbox lstAvaliadoresDisponiveis;
    private Button btnAdicionarAvaliador;
    
    // Componentes para gerenciar Avaliadores
    private Grid gridAutoresVinculados;
    private Listbox lstAutoresDisponiveis;
    private Button btnAdicionarAutor;
    
    // Componentes para gerenciar Setores
    private Grid gridSetoresVinculados;
    private Listbox lstSetoresDisponiveis;
    private Button btnAdicionarSetor;
    
    // Componentes para gerenciar Funcionários Qualidade
    private Grid gridFuncionariosVinculados;
    private Listbox lstFuncionariosDisponiveis;
    private Button btnAdicionarFuncionario;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private DocumentoDAO documentoDao = new DocumentoDAO();
    private DiretoriaDAO diretoriaDao = new DiretoriaDAO();
    private FuncionarioHUDAO funcionarioHUDao = new FuncionarioHUDAO();
    private SetorDAO setorDao = new SetorDAO();
    private FuncionarioQualidadeDAO funcionarioQualidadeDao = new FuncionarioQualidadeDAO();

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
        
        // Componentes das abas de relacionamentos N:N
        this.gridAvaliadoresVinculados = (Grid) getFellow("gridAvaliadoresVinculados");
        this.lstAvaliadoresDisponiveis = (Listbox) getFellow("lstAvaliadoresDisponiveis");
        this.btnAdicionarAvaliador = (Button) getFellow("btnAdicionarAvaliador");

        this.gridAutoresVinculados = (Grid) getFellow("gridAutoresVinculados");
        this.lstAutoresDisponiveis = (Listbox) getFellow("lstAutoresDisponiveis");
        this.btnAdicionarAutor = (Button) getFellow("btnAdicionarAutor");

        this.gridSetoresVinculados = (Grid) getFellow("gridSetoresVinculados");
        this.lstSetoresDisponiveis = (Listbox) getFellow("lstSetoresDisponiveis");
        this.btnAdicionarSetor = (Button) getFellow("btnAdicionarSetor");
        
        this.gridFuncionariosVinculados = (Grid) getFellow("gridFuncionariosVinculados");
        this.lstFuncionariosDisponiveis = (Listbox) getFellow("lstFuncionariosDisponiveis");
        this.btnAdicionarFuncionario = (Button) getFellow("btnAdicionarFuncionario");

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
                // Desabilita as abas de relacionamento para novo documento
                this.btnAdicionarAvaliador.setDisabled(true);
                this.btnAdicionarAutor.setDisabled(true);
                this.btnAdicionarSetor.setDisabled(true);
                this.btnAdicionarFuncionario.setDisabled(true);
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
                // Desabilita botões de adicionar/remover nas abas
                this.btnAdicionarAvaliador.setDisabled(true);
                this.btnAdicionarAutor.setDisabled(true);
                this.btnAdicionarSetor.setDisabled(true);
                this.btnAdicionarFuncionario.setDisabled(true);
            }
        }

        carregarDiretorias();
        carregarResponsaveisTecnicos();
        limparCampos();

        // Caso tenha algum id de documento que veio por parâmetro,
        // então busca esse documento no banco e popula os campos com os valores
        if (documentoId != null) {
            documento = documentoDao.buscarComRelacionamentos(documentoId);
            if (documento != null) {
                popularCampos();
                carregarAvaliadoresVinculados();
                carregarAvaliadoresDisponiveis();
                carregarAutoresVinculados();
                carregarAutoresDisponiveis();
                carregarSetoresVinculados();
                carregarSetoresDisponiveis();
                carregarFuncionariosVinculados();
                carregarFuncionariosDisponiveis();
            }
        }
    }

    /**
     * Carrega as diretorias no listbox dinamicamente do banco de dados
     */
    private void carregarDiretorias() {
        try {
            List<Diretoria> diretorias = diretoriaDao.listar("1=1", "order by t.nmOrgao");
            
            if (diretorias != null && !diretorias.isEmpty()) {
                while (this.diretoria.getItemCount() > 1) {
                    this.diretoria.removeItemAt(1);
                }
                
                for (Diretoria dir : diretorias) {
                    Listitem item = new Listitem();
                    item.setValue(dir.getCdOrgao());
                    
                    Listcell cell = new Listcell();
                    cell.setLabel(dir.getNmOrgao());
                    item.appendChild(cell);
                    
                    this.diretoria.appendChild(item);
                }
                
                System.out.println("Diretorias carregadas: " + diretorias.size());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar diretorias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os funcionários HU no listbox
     */
    private void carregarResponsaveisTecnicos() {
        try {
            List<FuncionarioHU> funcionarios = funcionarioHUDao.listar("1=1", "order by t.nome");
            
            if (funcionarios != null && !funcionarios.isEmpty()) {
                while (this.rt.getItemCount() > 1) {
                    this.rt.removeItemAt(1);
                }
                
                for (FuncionarioHU func : funcionarios) {
                    Listitem item = new Listitem();
                    item.setValue(func.getCdPessoa());
                    
                    Listcell cell = new Listcell();
                    cell.setLabel(func.getNome());
                    item.appendChild(cell);
                    
                    this.rt.appendChild(item);
                }
                
                System.out.println("Funcionários HU carregados: " + funcionarios.size());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar responsáveis técnicos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega os avaliadores já vinculados ao documento
     */
    private void carregarAvaliadoresVinculados() {
        // Inicializa a lista se for null
        if (documento.getAvaliadores() == null) {
            documento.setAvaliadores(new ArrayList<>());
        }

        System.out.println("Carregando avaliadores vinculados. Total: " + documento.getAvaliadores().size());

        SimpleListModel listModel = new SimpleListModel<>(documento.getAvaliadores());
        this.gridAvaliadoresVinculados.setModel(listModel);
    }
    
    /**
     * Carrega os avaliadores disponíveis (não vinculados) para seleção
     */
    private void carregarAvaliadoresDisponiveis() {
        try {
            List<FuncionarioHU> todosFuncionariosHU = funcionarioHUDao.listar("1=1", "order by t.nome");

            // Incializa a lista se for null
            if (documento.getAvaliadores() == null) {
                documento.setAvaliadores(new ArrayList<>());
            }

            // Filtra apenas os avaliadores que NÃO estão vinculados ao documento
            List<Integer> idsVinculados = documento.getAvaliadores().stream()
                .map(FuncionarioHU::getCdPessoa)
                .collect(Collectors.toList());
            
            List<FuncionarioHU> funcionariosHUDisponiveis = todosFuncionariosHU.stream()
                .filter(f -> !idsVinculados.contains(f.getCdPessoa()))
                .collect(Collectors.toList());

            System.out.println("Avaliadores disponíveis: " + funcionariosHUDisponiveis.size());
            System.out.println("Avaliadores vinculados (IDs): " + idsVinculados);

            // Limpa e preenche o listbox
            this.lstAvaliadoresDisponiveis.getItems().clear();

            if (!funcionariosHUDisponiveis.isEmpty()) {
                for (FuncionarioHU funcionarioHU: funcionariosHUDisponiveis) {
                    Listitem item = new Listitem();
                    item.setValue(funcionarioHU.getCdPessoa());
                    item.setLabel(funcionarioHU.getNome());
                    this.lstAvaliadoresDisponiveis.appendChild(item);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar avaliadores disponíveis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adiciona um avaliador ao documento
     */
    public void adicionarAvaliador() {
        if (this.lstAvaliadoresDisponiveis.getSelectedItem() == null) {
            Clients.showNotification("Selecione um avaliador!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.lstAutoresDisponiveis, "end_center", 0);
            return;
        }
        
        Integer cdAvaliador = (Integer) this.lstAutoresDisponiveis.getSelectedItem().getValue();
        
        System.out.println("Tentando adicionar avaliador ID: " + cdAvaliador + " ao documento ID: " + documento.getCdDocumento());
        
        boolean adicionou = documentoDao.adicionarAvaliador(documento.getCdDocumento(), cdAvaliador);
        
        if (adicionou) {
            Toast.show("Avaliador vinculado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            
            // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
            documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
            
            System.out.println("Documento recarregado. Avaliadores: " + 
                (documento.getAvaliadores() != null ? documento.getSetores().size() : 0));
            
            carregarAvaliadoresVinculados();
            carregarAvaliadoresDisponiveis();
        } else {
            zkUtils.MensagemErro("Erro ao vincular avaliador. Verifique os logs.");
            System.err.println("Falha ao adicionar avaliador ao documento");
        }
    }
    
    /**
     * Remove um avaliador do documento
     */
    public void removerAvaliador(Button button) {
        Integer cdAvaliador = (Integer) button.getAttribute("cdPessoa");
        if (zkUtils.MensagemConfirmacao("Deseja remover este avaliador do documento?")) {
            
            System.out.println("Tentando remover avaliador ID: " + cdAvaliador + " do documento ID: " + documento.getCdDocumento());
            
            boolean removeu = documentoDao.removerAvaliador(documento.getCdDocumento(), cdAvaliador);
            
            if (removeu) {
                Toast.show("Avaliador removido com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                
                // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
                documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
                
                carregarAvaliadoresVinculados();
                carregarAvaliadoresDisponiveis();
            } else {
                zkUtils.MensagemErro("Erro ao remover setor");
            }
        }
    }
    /**
     * Carrega os autores já vinculados ao documento
     */
    private void carregarAutoresVinculados() {
        // Inicializa a lista se for null
        if (documento.getAutores() == null) {
            documento.setAutores(new ArrayList<>());
        }

        System.out.println("Carregando autores vinculados. Total: " + documento.getAutores().size());

        SimpleListModel listModel = new SimpleListModel<>(documento.getAutores());
        this.gridAutoresVinculados.setModel(listModel);
    }
    
    /**
     * Carrega os avaliadores disponíveis (não vinculados) para seleção
     */
    private void carregarAutoresDisponiveis() {
        try {
            List<FuncionarioHU> todosFuncionariosHU = funcionarioHUDao.listar("1=1", "order by t.nome");

            // Incializa a lista se for null
            if (documento.getAutores() == null) {
                documento.setAutores(new ArrayList<>());
            }

            // Filtra apenas os avaliadores que NÃO estão vinculados ao documento
            List<Integer> idsVinculados = documento.getAutores().stream()
                .map(FuncionarioHU::getCdPessoa)
                .collect(Collectors.toList());
            
            List<FuncionarioHU> funcionariosHUDisponiveis = todosFuncionariosHU.stream()
                .filter(f -> !idsVinculados.contains(f.getCdPessoa()))
                .collect(Collectors.toList());

            System.out.println("Autores disponíveis: " + funcionariosHUDisponiveis.size());
            System.out.println("Autores vinculados (IDs): " + idsVinculados);

            // Limpa e preenche o listbox
            this.lstAutoresDisponiveis.getItems().clear();

            if (!funcionariosHUDisponiveis.isEmpty()) {
                for (FuncionarioHU funcionarioHU: funcionariosHUDisponiveis) {
                    Listitem item = new Listitem();
                    item.setValue(funcionarioHU.getCdPessoa());
                    item.setLabel(funcionarioHU.getNome());
                    this.lstAutoresDisponiveis.appendChild(item);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar avaliadores disponíveis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adiciona um autor ao documento
     */
    public void adicionarAutor() {
        if (this.lstAutoresDisponiveis.getSelectedItem() == null) {
            Clients.showNotification("Selecione um autor!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.lstAutoresDisponiveis, "end_center", 0);
            return;
        }
        
        Integer cdAutor = (Integer) this.lstAutoresDisponiveis.getSelectedItem().getValue();
        
        System.out.println("Tentando adicionar autor ID: " + cdAutor + " ao documento ID: " + documento.getCdDocumento());
        
        boolean adicionou = documentoDao.adicionarAutor(documento.getCdDocumento(), cdAutor);
        
        if (adicionou) {
            Toast.show("Avaliador vinculado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            
            // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
            documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
            
            System.out.println("Documento recarregado. Avaliadores: " + 
                (documento.getAutores() != null ? documento.getSetores().size() : 0));
            
            carregarAutoresVinculados();
            carregarAutoresDisponiveis();
        } else {
            zkUtils.MensagemErro("Erro ao vincular avaliador. Verifique os logs.");
            System.err.println("Falha ao adicionar avaliador ao documento");
        }
    }
    
    /**
     * Remove um autor do documento
     */
    public void removerAutor(Button button) {
        Integer cdAutor = (Integer) button.getAttribute("cdPessoa");
        if (zkUtils.MensagemConfirmacao("Deseja remover este autor do documento?")) {
            
            System.out.println("Tentando remover autor ID: " + cdAutor + " do documento ID: " + documento.getCdDocumento());
            
            boolean removeu = documentoDao.removerAutor(documento.getCdDocumento(), cdAutor);
            
            if (removeu) {
                Toast.show("Avaliador removido com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                
                // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
                documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
                
                carregarAutoresVinculados();
                carregarAutoresDisponiveis();
            } else {
                zkUtils.MensagemErro("Erro ao remover setor");
            }
        }
    }
    
    /**
     * Carrega os setores já vinculados ao documento
     */
    private void carregarSetoresVinculados() {
        // Inicializa a lista se for null
        if (documento.getSetores() == null) {
            documento.setSetores(new ArrayList<>());
        }
        
        System.out.println("Carregando setores vinculados. Total: " + documento.getSetores().size());
        
        SimpleListModel listModel = new SimpleListModel(documento.getSetores());
        this.gridSetoresVinculados.setModel(listModel);
    }
    
    /**
     * Carrega os setores disponíveis (não vinculados) para seleção
     */
    private void carregarSetoresDisponiveis() {
        try {
            List<Setor> todosSetores = setorDao.listar("1=1", "order by t.nmOrgao");
            
            // Inicializa a lista se for null
            if (documento.getSetores() == null) {
                documento.setSetores(new ArrayList<>());
            }
            
            // Filtra apenas os setores que NÃO estão vinculados ao documento
            List<Integer> idsVinculados = documento.getSetores().stream()
                .map(Setor::getCdOrgao)
                .collect(Collectors.toList());
            
            List<Setor> setoresDisponiveis = todosSetores.stream()
                .filter(s -> !idsVinculados.contains(s.getCdOrgao()))
                .collect(Collectors.toList());
            
            System.out.println("Setores disponíveis: " + setoresDisponiveis.size());
            System.out.println("Setores vinculados (IDs): " + idsVinculados);
            
            // Limpa e preenche o listbox
            this.lstSetoresDisponiveis.getItems().clear();
            
            if (!setoresDisponiveis.isEmpty()) {
                for (Setor setor : setoresDisponiveis) {
                    Listitem item = new Listitem();
                    item.setValue(setor.getCdOrgao());
                    item.setLabel(setor.getNmOrgao());
                    this.lstSetoresDisponiveis.appendChild(item);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar setores disponíveis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adiciona um setor ao documento
     */
    public void adicionarSetor() {
        if (this.lstSetoresDisponiveis.getSelectedItem() == null) {
            Clients.showNotification("Selecione um setor!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.lstSetoresDisponiveis, "end_center", 0);
            return;
        }
        
        Integer cdSetor = (Integer) this.lstSetoresDisponiveis.getSelectedItem().getValue();
        
        System.out.println("Tentando adicionar setor ID: " + cdSetor + " ao documento ID: " + documento.getCdDocumento());
        
        boolean adicionou = documentoDao.adicionarSetor(documento.getCdDocumento(), cdSetor);
        
        if (adicionou) {
            Toast.show("Setor vinculado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            
            // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
            documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
            
            System.out.println("Documento recarregado. Setores: " + 
                (documento.getSetores() != null ? documento.getSetores().size() : 0));
            
            carregarSetoresVinculados();
            carregarSetoresDisponiveis();
        } else {
            zkUtils.MensagemErro("Erro ao vincular setor. Verifique os logs.");
            System.err.println("Falha ao adicionar setor ao documento");
        }
    }
    
    /**
     * Remove um setor do documento
     */
    public void removerSetor(Button button) {
        Integer cdSetor = (Integer) button.getAttribute("cdOrgao");
        if (zkUtils.MensagemConfirmacao("Deseja remover este setor do documento?")) {
            
            System.out.println("Tentando remover setor ID: " + cdSetor + " do documento ID: " + documento.getCdDocumento());
            
            boolean removeu = documentoDao.removerSetor(documento.getCdDocumento(), cdSetor);
            
            if (removeu) {
                Toast.show("Setor removido com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                
                // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
                documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
                
                carregarSetoresVinculados();
                carregarSetoresDisponiveis();
            } else {
                zkUtils.MensagemErro("Erro ao remover setor");
            }
        }
    }
    
    /**
     * Carrega os funcionários qualidade já vinculados ao documento
     */
    private void carregarFuncionariosVinculados() {
        // Inicializa a lista se for null
        if (documento.getFuncionariosQualidade() == null) {
            documento.setFuncionariosQualidade(new ArrayList<>());
        }
        
        System.out.println("Carregando funcionários vinculados. Total: " + documento.getFuncionariosQualidade().size());
        
        SimpleListModel listModel = new SimpleListModel(documento.getFuncionariosQualidade());
        this.gridFuncionariosVinculados.setModel(listModel);
    }
    
    /**
     * Carrega os funcionários qualidade disponíveis (não vinculados)
     */
    private void carregarFuncionariosDisponiveis() {
        try {
            List<FuncionarioQualidade> todosFuncionarios = funcionarioQualidadeDao.listar("1=1", "order by t.nome");
            
            // Inicializa a lista se for null
            if (documento.getFuncionariosQualidade() == null) {
                documento.setFuncionariosQualidade(new ArrayList<>());
            }
            
            // Filtra apenas os funcionários que NÃO estão vinculados
            List<Integer> idsVinculados = documento.getFuncionariosQualidade().stream()
                .map(FuncionarioQualidade::getCdPessoa)
                .collect(Collectors.toList());
            
            List<FuncionarioQualidade> funcionariosDisponiveis = todosFuncionarios.stream()
                .filter(f -> !idsVinculados.contains(f.getCdPessoa()))
                .collect(Collectors.toList());
            
            System.out.println("Funcionários disponíveis: " + funcionariosDisponiveis.size());
            
            // Limpa e preenche o listbox
            this.lstFuncionariosDisponiveis.getItems().clear();
            
            if (!funcionariosDisponiveis.isEmpty()) {
                for (FuncionarioQualidade func : funcionariosDisponiveis) {
                    Listitem item = new Listitem();
                    item.setValue(func.getCdPessoa());
                    item.setLabel(func.getNome());
                    this.lstFuncionariosDisponiveis.appendChild(item);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar funcionários disponíveis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adiciona um funcionário qualidade ao documento
     */
    public void adicionarFuncionario() {
        if (this.lstFuncionariosDisponiveis.getSelectedItem() == null) {
            Clients.showNotification("Selecione um funcionário!", 
                Clients.NOTIFICATION_TYPE_WARNING, this.lstFuncionariosDisponiveis, "end_center", 0);
            return;
        }
        
        Integer cdPessoa = (Integer) this.lstFuncionariosDisponiveis.getSelectedItem().getValue();
        
        System.out.println("Tentando adicionar funcionário ID: " + cdPessoa + " ao documento ID: " + documento.getCdDocumento());
        
        boolean adicionou = documentoDao.adicionarFuncionarioQualidade(documento.getCdDocumento(), cdPessoa);
        
        if (adicionou) {
            Toast.show("Funcionário vinculado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
            
            // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
            documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
            
            carregarFuncionariosVinculados();
            carregarFuncionariosDisponiveis();
        } else {
            zkUtils.MensagemErro("Erro ao vincular funcionário");
        }
    }
    
    /**
     * Remove um funcionário qualidade do documento
     */
    public void removerFuncionario(Button button) {
        Integer cdPessoa = (Integer) button.getAttribute("cdPessoa");
        if (zkUtils.MensagemConfirmacao("Deseja remover este funcionário do documento?")) {
            
            System.out.println("Tentando remover funcionário ID: " + cdPessoa + " do documento ID: " + documento.getCdDocumento());
            
            boolean removeu = documentoDao.removerFuncionarioQualidade(documento.getCdDocumento(), cdPessoa);
            
            if (removeu) {
                Toast.show("Funcionário removido com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                
                // IMPORTANTE: Recarrega o documento com relacionamentos atualizados
                documento = documentoDao.buscarComRelacionamentos(documento.getCdDocumento());
                
                carregarFuncionariosVinculados();
                carregarFuncionariosDisponiveis();
            } else {
                zkUtils.MensagemErro("Erro ao remover funcionário");
            }
        }
    }

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

    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdDocumento, (Object) this.documento.getCdDocumento());
        zkUtils.popularCampo(this.nmDocumento, (Object) this.documento.getNmDocumento());
        zkUtils.popularCampo(this.deDescricao, (Object) this.documento.getDeDescricao());
        
        if (this.documento.getDtCriacao() != null) {
            this.dtCriacao.setValue(this.documento.getDtCriacao());
        }
        if (this.documento.getDtVencimento() != null) {
            this.dtVencimento.setValue(this.documento.getDtVencimento());
        }

        if (this.documento.getTpDocumento() != null) {
            selecionarTipoDocumento(this.documento.getTpDocumento());
        }

        if (this.documento.getEstado() != null) {
            selecionarEstado(this.documento.getEstado());
        }

        if (this.documento.getDiretoria() != null) {
            selecionarDiretoria(this.documento.getDiretoria().getCdOrgao());
        }

        if (this.documento.getRt() != null) {
            selecionarRT(this.documento.getRt().getCdPessoa());
        }
    }

    private void selecionarTipoDocumento(String tipo) {
        for (Listitem item : this.tpDocumento.getItems()) {
            if (item.getValue().equals(tipo)) {
                this.tpDocumento.setSelectedItem(item);
                break;
            }
        }
    }

    private void selecionarEstado(EstadoDocumento estadoDoc) {
        String codigoEstado = estadoDoc.name();
        
        for (Listitem item : this.estado.getItems()) {
            if (item.getValue() != null && item.getValue().equals(codigoEstado)) {
                this.estado.setSelectedItem(item);
                break;
            }
        }
    }

    private void selecionarDiretoria(Integer cdOrgao) {
        for (Listitem item : this.diretoria.getItems()) {
            if (item.getValue() != null && item.getValue().equals(cdOrgao)) {
                this.diretoria.setSelectedItem(item);
                break;
            }
        }
    }

    private void selecionarRT(Integer cdPessoa) {
        for (Listitem item : this.rt.getItems()) {
            if (item.getValue() != null && item.getValue().equals(cdPessoa)) {
                this.rt.setSelectedItem(item);
                break;
            }
        }
    }

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

    public void gravar() {
        if (validarCampos()) {
            documento.setNmDocumento(this.nmDocumento.getValue());
            documento.setTpDocumento((String) this.tpDocumento.getSelectedItem().getValue());
            documento.setDtCriacao(this.dtCriacao.getValue());
            documento.setDtVencimento(this.dtVencimento.getValue());
            documento.setDeDescricao(this.deDescricao.getValue());
            
            String estadoString = (String) this.estado.getSelectedItem().getValue();
            EstadoDocumento estadoEnum = EstadoDocumento.valueOf(estadoString);
            documento.setEstado(estadoEnum);

            if (this.diretoria.getSelectedItem() != null && this.diretoria.getSelectedIndex() > 0) {
                Integer cdOrgao = (Integer) this.diretoria.getSelectedItem().getValue();
                Diretoria dir = diretoriaDao.buscar(cdOrgao);
                documento.setDiretoria(dir);
            } else {
                documento.setDiretoria(null);
            }

            if (this.rt.getSelectedItem() != null && this.rt.getSelectedIndex() > 0) {
                Integer cdPessoa = (Integer) this.rt.getSelectedItem().getValue();
                FuncionarioHU funcionario = funcionarioHUDao.buscar(cdPessoa);
                documento.setRt(funcionario);
            } else {
                documento.setRt(null);
            }

            if (Integer.parseInt(this.cdDocumento.getValue()) == -1) {
                Integer id = documentoDao.incluirAutoincrementando(documento);
                if (id != null && id > 0) {
                    Toast.show("Documento cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.cdDocumento.setValue(id.toString());
                    // Atualiza o objeto documento com o ID gerado
                    documento.setCdDocumento(id);
                    
                    // IMPORTANTE: Recarrega o documento do banco para garantir que tem todos os dados
                    documento = documentoDao.buscarComRelacionamentos(id);
                    
                    // Habilita os botões de relacionamento
                    this.btnAdicionarSetor.setDisabled(false);
                    this.btnAdicionarFuncionario.setDisabled(false);
                    
                    // CORREÇÃO: Carrega as listas após salvar documento novo
                    carregarAutoresVinculados();
                    carregarAutoresDisponiveis();
                    carregarAvaliadoresVinculados();
                    carregarAvaliadoresDisponiveis();
                    carregarSetoresVinculados();
                    carregarSetoresDisponiveis();
                    carregarFuncionariosVinculados();
                    carregarFuncionariosDisponiveis();
                    
                    System.out.println("Documento salvo com ID: " + id);
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir");
                }
            } else {
                documento.setCdDocumento(Integer.valueOf(this.cdDocumento.getValue()));
                boolean atualizou = documentoDao.atualizar(documento);
                if (atualizou) {
                    Toast.show("Documento atualizado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }
        }
    }

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

    public void voltar() {
        Include include = (Include) win.getParent();
        include.setSrc(this.urlRetorno);
    }
}