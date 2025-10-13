package dao;

import model.Pessoa;

public class PessoaDAO extends GenericDAO<Pessoa, Integer> {

    public PessoaDAO() {
        super(Pessoa.class);
    }

}
