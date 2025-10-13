package dao;

import model.Mensagem;

public class MensagemDAO extends GenericDAO<Mensagem, Integer> {

    public MensagemDAO() {
        super(Mensagem.class);
    }

}
