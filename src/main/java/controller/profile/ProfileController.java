/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package controller.profile;

import org.zkoss.zul.*;
import application.service.Application;
import application.service.Sessao;
import model.Usuario;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;
import org.zkoss.zul.Div;

/**
 * Controller responsável pela página de perfil do usuário.
 * Exibe informações básicas e permite encerrar a sessão.
 */
public class ProfileController extends Window {

    private Label lbNomeUsuario;
    private Label lbEmailUsuario;
    private Label lbVersao;
    private Label lbErro;
    private Div divDesenv;
    private Window janela;

    private String urlRetorno = "";

    public void onCreate() {
        this.janela = (Window) getFellowIfAny("winProfile", true);
        this.lbNomeUsuario = (Label) getFellowIfAny("lb_nome_usuario", true);
        this.lbEmailUsuario = (Label) getFellowIfAny("lb_email_usuario", true);
        this.lbVersao = (Label) getFellowIfAny("lb_versao", true);
        this.lbErro = (Label) getFellowIfAny("msg_erro", true);
        this.divDesenv = (Div) getFellowIfAny("div_desenv", true);

        // Exibe a versão da aplicação
        String versao = Application.getInstance().getVersion();
        if (lbVersao != null)
            lbVersao.setValue("Versão " + versao);

        // Mensagem de ambiente de desenvolvimento
        mensagemDesenvol();

        // Exibe dados do usuário logado, se existir
        carregarUsuarioLogado();

        // Atualiza título e console, se aplicável
        Application.getInstance().insereDadosNoConsole(this.janela);
        Application.getInstance().atualizaTituloDaPagina();
    }

    /**
     * Exibe informações do usuário logado, se houver.
     */
    private void carregarUsuarioLogado() {
        Usuario usuario = Sessao.getInstance().getUsuario();
        if (usuario != null) {
            if (lbNomeUsuario != null && usuario.getNome() != null)
                lbNomeUsuario.setValue(usuario.getNome());
            if (lbEmailUsuario != null && usuario.getEmail() != null)
                lbEmailUsuario.setValue(usuario.getEmail());
        } else {
            showErro("Nenhum usuário ativo na sessão.");
        }
    }

    /**
     * Encerra a sessão do usuário e redireciona para o login.
     */
    public void sair() {
        try {
            Executions.getCurrent().getSession().invalidate();
            Sessao.getInstance().setUsuario(null);
            Executions.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
            showErro("Erro ao encerrar sessão: " + e.getMessage());
        }
    }

    /**
     * Mostra erro na tela do perfil.
     */
    public void showErro(String msg) {
        if (lbErro != null) {
            lbErro.setValue(msg);
            lbErro.setVisible(true);
        }
    }

    /**
     * Esconde a mensagem de erro.
     */
    public void hideErro() {
        if (lbErro != null) {
            lbErro.setValue("");
            lbErro.setVisible(false);
        }
    }

    /**
     * Mostra aviso se for ambiente de desenvolvimento.
     */
    private void mensagemDesenvol() {
        if (Application.getInstance().getEnviroment().equals(Application.DEVELOP) && divDesenv != null) {
            divDesenv.setVisible(true);
        }
    }

    public void voltar() {
        Include include = (Include) janela.getParent();
        include.setSrc(this.urlRetorno);
    }
}
