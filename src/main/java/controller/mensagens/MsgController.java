/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */
package controller.mensagens;

import dao.MensagemDAO;
import model.Mensagem;
import model.FrequenciaMensagem;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgController extends Window {

    private Window win;
    private Label cdMensagem;
    private Textbox nome;
    private Textbox corpo;
    private Datebox dtInicio;
    private Datebox dtFim;
    private Listbox frequencia;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private MensagemDAO mensagemDao = new MensagemDAO();

    private Mensagem mensagem = new Mensagem();

    private String urlRetorno = "";

    public void onCreate() {

        this.win = ((Window) getFellow("winMensagem"));

        this.cdMensagem = (Label) getFellow("cdMensagem");
        this.nome = (Textbox) getFellow("nome");
        this.corpo = (Textbox) getFellow("corpo");
        this.dtInicio = (Datebox) getFellow("dtInicio");
        this.dtFim = (Datebox) getFellow("dtFim");
        this.frequencia = (Listbox) getFellow("frequencia");
        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        // Pega os parâmetros que vieram da outra página
        Integer mensagemId = (Integer) zkUtils.getParametro("mensagem");
        String acao = (String) zkUtils.getParametro("ação");
        urlRetorno = (String) zkUtils.getParametro("url_retorno");

        // Mostra os botões de acordo com a ação
        if (acao != null) {
            if (acao.equals("novo")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.cdMensagem.setVisible(false);
            } else if (acao.equals("editar")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.btnExcluir.setVisible(true);
            }
        }

        limparCampos();

        // Caso tenha algum id de mensagem que veio por parâmetro, 
        // então busca essa mensagem no banco e popula os campos com os valores
        if (mensagemId != null) {
            mensagem = (Mensagem) mensagemDao.buscar(mensagemId);
            if (mensagem != null) {
                popularCampos();
            }
        }
    }

    /**
     * Limpa os campos do .zul.
     */
    public void limparCampos() {
        this.cdMensagem.setValue("-1");
        this.nome.setRawValue(null);
        this.corpo.setRawValue(null);
        this.dtInicio.setValue(null);
        this.dtFim.setValue(null);
        this.frequencia.setSelectedIndex(0);
        this.nome.setFocus(true);
    }

    /**
     * Popula os campos .zul com os valores do banco.
     */
    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdMensagem, (Object) this.mensagem.getCdMensagem());
        zkUtils.popularCampo(this.nome, (Object) this.mensagem.getDeNome());
        zkUtils.popularCampo(this.corpo, (Object) this.mensagem.getDeCorpo());
        
        if (this.mensagem.getDtInicio() != null) {
            this.dtInicio.setValue(this.mensagem.getDtInicio());
        }
        
        if (this.mensagem.getDtFim() != null) {
            this.dtFim.setValue(this.mensagem.getDtFim());
        }
        
        // Seleciona a frequência correta no listbox
        FrequenciaMensagem freq = this.mensagem.getFrequenciaEnum(); // <-- MUDOU AQUI
        if (freq != null) {
            String freqName = freq.name(); // <-- AGORA SIM TEM O .name()
            for (Listitem item : this.frequencia.getItems()) {
                if (item.getValue() != null && item.getValue().equals(freqName)) {
                    this.frequencia.setSelectedItem(item);
                    break;
                }
            }
        }
    }

    /**
     * Valida se os campos preenchidos estão corretos.
     */
    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.nome.getValue().trim().isEmpty()) {
            Clients.showNotification("Nome é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.nome.setFocus(true);
        }

        if (this.corpo.getValue().trim().isEmpty()) {
            Clients.showNotification("Corpo da mensagem é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.corpo, "end_center", 0);
            gerouErro = true;
            this.corpo.setFocus(true);
        }

        if (this.dtInicio.getValue() == null) {
            Clients.showNotification("Data de início é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.dtInicio, "end_center", 0);
            gerouErro = true;
            this.dtInicio.setFocus(true);
        }

        if (this.dtFim.getValue() == null) {
            Clients.showNotification("Data de fim é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.dtFim, "end_center", 0);
            gerouErro = true;
            this.dtFim.setFocus(true);
        }

        // Valida se a data de fim é posterior à data de início
        if (this.dtInicio.getValue() != null && this.dtFim.getValue() != null) {
            if (this.dtFim.getValue().before(this.dtInicio.getValue())) {
                Clients.showNotification("Data de fim deve ser posterior à data de início!", Clients.NOTIFICATION_TYPE_WARNING, this.dtFim, "end_center", 0);
                gerouErro = true;
                this.dtFim.setFocus(true);
            }
        }

        if (this.frequencia.getSelectedItem() == null) {
            Clients.showNotification("Frequência é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.frequencia, "end_center", 0);
            gerouErro = true;
            this.frequencia.setFocus(true);
        }

        return !gerouErro;
    }

    /**
     * Grava os dados no banco.
     */
    public void gravar() {
        if (validarCampos()) {
            // Se validou os dados preenchidos, então atualizamos nosso objeto Mensagem com os dados preenchidos
            mensagem.setDeNome(this.nome.getValue());
            mensagem.setDeCorpo(this.corpo.getValue());
            mensagem.setDtInicio(this.dtInicio.getValue());
            mensagem.setDtFim(this.dtFim.getValue());
            
            // Pega a frequência selecionada e converte de String para Enum
            if (this.frequencia.getSelectedItem() != null) {
                String freqString = (String) this.frequencia.getSelectedItem().getValue();
                FrequenciaMensagem freq = FrequenciaMensagem.valueOf(freqString);
                mensagem.setFrequenciaEnum(freq); // <-- MUDOU AQUI
            }

            if (Integer.parseInt(this.cdMensagem.getValue()) == -1) {
                // Se não tem id significa que é objeto novo, então incluímos no banco
                Integer id = mensagemDao.incluirAutoincrementando(mensagem);
                if (id != null && id > 0) {
                    Toast.show("Mensagem cadastrada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.cdMensagem.setValue(mensagem.getCdMensagem().toString());
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir - ID: " + id);
                }
            } else {
                // Se já tem id significa que é objeto que já existe no banco, então fazemos update no banco
                mensagem.setCdMensagem(Integer.valueOf(this.cdMensagem.getValue()));
                boolean atualizou = mensagemDao.atualizar(mensagem);
                if (atualizou) {
                    Toast.show("Mensagem atualizada com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }
        }
    }
    
    /**
     * Exclui o objeto Mensagem do banco de dados.
     */
    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir a mensagem atual?")) {
            if (!this.cdMensagem.getValue().equals("-1")) {
                mensagemDao.excluir(mensagem);
                Toast.show("Mensagem excluída com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                limparCampos();
                this.voltar();
            } else {
                zkUtils.MensagemErro("Mensagem inválida");
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