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
