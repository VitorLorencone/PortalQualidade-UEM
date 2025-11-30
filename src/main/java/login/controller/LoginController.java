/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package login.controller;

import application.service.Application;
import application.service.Sessao;
import login.dao.UsuarioDAO;
import model.Usuario;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import utilitarios.CookieUtil;
import utilitarios.LdapUtil;
import utilitarios.SenhaUtil;
import org.hibernate.SessionFactory;

public class LoginController extends Window {

    private UsuarioDAO usuarioDAO;
    private Textbox usuarioDigitado;
    private Textbox senhaDigitada;
    private Include conteudo;
    private Label lbVersao;
    private Label lbErro;
    private Div divDesenv;
    private Window janela;

    public void onCreate() {

        this.conteudo = (Include) getFellowIfAny("conteudo", true);
        this.janela = (Window) getFellow("login");
        this.usuarioDigitado = (Textbox) getFellow("usuario");
        this.senhaDigitada = (Textbox) getFellow("senha");
        this.lbVersao = (Label) getFellow("lb_versao");
        this.lbErro = (Label) getFellow("msg_erro");
        this.divDesenv = (Div) getFellow("div_desenv");

        String versao = Application.getInstance().getVersion();
        this.lbVersao.setValue("Versão " + versao);

        this.mensagemDesenvol();
        Application.getInstance().insereDadosNoConsole(this.janela);
        Application.getInstance().atualizaTituloDaPagina();

        verificaCookie();

        //acesso automático para teste
        //this.usuarioDigitado.setValue("arpfreitas");
        //this.senhaDigitada.setValue(SENHA_MESTRA);
        //acessar();
    }

    /**
     * Executado quando clica no botão para entrar.
     */
    public void acessar() {
        validarSessao();
    }

    /**
     * Verifica se tem o último usuário que logou salvo em cookie, se tiver
     * preenche o campo automaticamente.
     */
    private void verificaCookie() {
        String ultimoUsuario = CookieUtil.getCookie("usuariologin");
        if (ultimoUsuario != null) {
            this.usuarioDigitado.setValue(ultimoUsuario);
        }
    }

    /**
     * Salva em cookie o último usuário que logou.
     */
    private void atualizaCookie() {
        CookieUtil.setCookie("usuariologin", this.usuarioDigitado.getValue());
    }

    /**
     * Valida a senha no LDAP da UEM. Funciona para todos que tiver um e-mail
     * válido @uem.br
     *
     * @param login é o e-mail ou apenas o nome do usuário
     * @param senha senha
     * @return
     */
    private boolean isSenhaValidaLdap(String login, String senha) {

        LdapUtil ldapUtil = new LdapUtil();

        try {
            ldapUtil.bind(login, senha);
        } catch (Exception e) {
            this.showErro(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Valida a senha conforme cadastrado no banco de dados usando Argon2.
     *
     * @param login é o e-mail ou apenas o nome do usuário
     * @param senha senha
     * @return
     */
    private boolean isSenhaValidaBanco(String login, String senha) {

        try {
            // Para testes rápidos durante desenvolvimento
            if (login.equals("teste") && senha.equals("teste123")) {
                return true;
            }

            // Obtém o SessionFactory do Hibernate
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            UsuarioDAO dao = new UsuarioDAO(sessionFactory);

            // Busca o usuário no banco
            Usuario usuarioBD = dao.buscarPorLogin(login);

            if (usuarioBD == null) {
                this.showErro("Usuário não encontrado");
                return false;
            }

            // Verifica a senha usando Argon2
            boolean senhaCorreta = SenhaUtil.verificarSenha(senha, usuarioBD.getSenhaHash());

            if (!senhaCorreta) {
                this.showErro("Senha incorreta");
                return false;
            }

            return true;

        } catch (Exception e) {
            this.showErro("Erro ao validar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void validarSessao() {

        String login = this.usuarioDigitado.getValue();
        String senha = this.senhaDigitada.getValue();

        if (isSenhaMestra(login, senha) || isSenhaValidaLdap(login, senha) || isSenhaValidaBanco(login, senha)) {

            //aqui estamos colocando @uem.br porem tem que ver se essa regra faz sentido,
            //ou seja, se seu sistema somente aceita usuarios @uem.br ou se aceita usuarios externos
            String email = "";
            int i = login.indexOf("@");
            if (i < 1) {
                email = login + "@uem.br";
            } else {
                email = login;
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            //aqui pode ter tratativa de ir buscar esse usuário no banco de dados usando o e-mail, para pegar as permissões do usuário, caso você tenha um cadastro de usuário e permissão
            //pode neste momento verificar também se o usuário pode ter acesso ou não ao sistema
            //se tiver algum erro utilize o this.showErro(mensagem)

            //GUARDE O USUÁRIO NA SESSÃO
            //assim você pode pegar ele na sessão toda vez que entrar em alguma página
            //e assim pegar as permissões do usuário logado para saber se realmente ele tem permissão para acessar aquela página
            //ou qual permissão ele tem naquele página (alterar, incluir, ver, etc.)
            Sessao.getInstance().setUsuario(usuario);
            atualizaCookie();
            Executions.sendRedirect("/");

        }

    }

    public void sair() {
        try {
            // Invalida a sessão atual do ZK (remove todos os atributos da sessão)
            Executions.getCurrent().getSession().invalidate();

            // Opcional: também limpar a referência na sua classe Sessao (caso ela mantenha estado estático)
            Sessao.getInstance().setUsuario(null);

            // Redireciona para a tela de login
            Executions.sendRedirect("/login");

        } catch (Exception e) {
            e.printStackTrace();
            showErro("Erro ao encerrar sessão: " + e.getMessage());
        }
    }

    /**
     * Mostra erro na tela de login
     *
     * @param msg
     */
    public void showErro(String msg) {
        this.lbErro.setValue(msg);
        this.lbErro.setVisible(true);
    }

    /**
     * Esconde o erro na tela de login.
     */
    public void hideErro() {
        this.lbErro.setValue("");
        this.lbErro.setVisible(false);
    }

    /**
     * Caso o ambiente de desenvolvimento seja de testes (não esteja conectado
     * em produção), então mostra um aviso.
     */
    private void mensagemDesenvol() {
        if (Application.getInstance().getEnviroment().equals(Application.DEVELOP)) {
            this.divDesenv.setVisible(true);
        }
    }

    //ATENCAO: EXCLUA OU ALTERE ESSA SENHA MESTRA
    private final String SENHA_MESTRA = "SENHA_MESTRA";

    private boolean isSenhaMestra(String login, String senha) {
        return senha.equals(SENHA_MESTRA);
    }
}