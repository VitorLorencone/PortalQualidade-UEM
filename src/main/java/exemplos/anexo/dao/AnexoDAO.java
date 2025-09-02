package exemplos.anexo.dao;

import exemplos.anexo.model.Anexo;
import dao.GenericDAO;

/**
 * DAO do Curso que extende do DAO Genérico. Os métodos listar foram
 * implementados para já retornarem uma lista de Curso em vez de uma lista de
 * objetos genéricos.
 *
 * @author alison
 */
public class AnexoDAO extends GenericDAO<Anexo, Integer> {

    public AnexoDAO() {
        super(Anexo.class);
    }

}
