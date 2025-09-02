/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package exemplos.endereco.dao;

import dao.GenericDAO;
import exemplos.endereco.model.Pais;

/**
 *
 * @author alison
 */
public class PaisDAO extends GenericDAO<Pais, String> {

    public PaisDAO() {
        super(Pais.class);
    }

}
