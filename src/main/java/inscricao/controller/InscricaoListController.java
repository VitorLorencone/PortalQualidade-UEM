/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package inscricao.controller;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;
import utilitarios.Utils;
import utilitarios.ZkUtils;

public class InscricaoListController extends Window {

    private Window winInscricao;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    public void onCreate() {
        
        //VALIDAR SESSÃO
        //faça essa chamada caso queira que apenas usuários logados acesse esta pagina
        //caso o usuário não esteja logado, ele será redirecionado para o login
        //Sessao.getInstance().validarSessao();

        winInscricao = (Window) getFellow("winInscricaoList");

        filtrar();
    }

    public void filtrar() {

    }

    public void novaInscricao() throws Exception {

    }

    public void onClickedVerInscricao(Event event) throws Exception {

    }

    public void onClickedAlterarInscricao(Event event) throws Exception {

    }

    private void redirecionar() {

    }

}
