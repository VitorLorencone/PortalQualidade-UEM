package dao;

import model.Mensagem;

public class MensagemDAO extends GenericDAO<Mensagem, Integer> {

    public MensagemDAO() {
        super(Mensagem.class);
    }

    public String getCategoriaById(Integer id) {
        Mensagem msg = this.buscar(id);
        String categoria = msg.getDeCategoria();
        return categoria;
    }

}
