package pessoa.dao;

import dao.GenericDAO;
import pessoa.model.Pessoa;

public class PessoaDAO extends GenericDAO<Pessoa, Integer> {

    public PessoaDAO() {
        super(Pessoa.class);
    }

}
