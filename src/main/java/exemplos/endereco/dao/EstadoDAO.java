/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package exemplos.endereco.dao;

import dao.GenericDAO;
import exemplos.endereco.model.Estado;
import java.util.List;

/**
 *
 * @author alison
 */
public class EstadoDAO extends GenericDAO<Estado, String> {

    public EstadoDAO() {
        super(Estado.class);
    }
    
    @Override
    public List<Estado> listar() {
        return listar("1=1", "ORDER BY sgUf ASC");
    }

}
