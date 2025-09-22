package dao;

import model.Documento;

public class DocumentoDAO extends GenericDAO<Documento, Integer> {

    public DocumentoDAO() {
        super(Documento.class);
    }

}
