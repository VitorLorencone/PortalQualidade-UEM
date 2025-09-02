/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package exemplos.endereco.dao;

import dao.GenericDAO;
import java.util.List;
import exemplos.endereco.model.Municipio;

/**
 *
 * @author alison
 */
public class MunicipioDAO extends GenericDAO<Municipio, Integer> {

    public MunicipioDAO() {
        super(Municipio.class);
    }

    @Override
    public List<Municipio> listar() {
        return listar("1=1", "ORDER BY nmMunicipio ASC");
    }

    public List<Municipio> listarByEstado(String sgUf) {
        return listar("sgUf LIKE '%" + sgUf + "%'", "ORDER BY nmMunicipio ASC");
    }

}
