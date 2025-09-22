/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package inscricao.dao;

import dao.GenericDAO;
import inscricao.model.Inscricao;

/**
 *
 * @author equipes
 */
public class InscricaoDAO extends GenericDAO<Inscricao, Integer> {

    public InscricaoDAO() {
        super(Inscricao.class);
    }

}
