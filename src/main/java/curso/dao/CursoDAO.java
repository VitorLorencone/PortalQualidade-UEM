package curso.dao;

import curso.model.Curso;
import dao.GenericDAO;

/**
 * DAO do Curso que extende do DAO Genérico. Os métodos listar foram
 * implementados para já retornarem uma lista de Curso em vez de uma lista de
 * objetos genéricos.
 *
 * Importante: esse DAO extende do GenericDAO de modo que deve ser defindo em
 * <T, I> a classe (neste caso T é o Curso) e o tipo o ID da classe (neste caso
 * o Id é um Integer, então o I é um Integer), ficando então "extends
 * GenericDAO<Curso, Integer>".
 *
 * @author alison
 *
 * TODO: apagar esses comentários/tutoriais ao usar em seu sistema
 */
public class CursoDAO extends GenericDAO<Curso, Integer> {

    /**
     * Todo DAO tem que ter um construtor passando a classe do model como
     * parâmetro
     */
    public CursoDAO() {
        super(Curso.class);
    }

}
