package dao;

import model.Documento;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class DocumentoDAO extends GenericDAO<Documento, Integer> {
    
    public DocumentoDAO() {
        super(Documento.class);
    }
    
    /**
     * Lista os documentos com FETCH JOIN para evitar LazyInitializationException.
     * Carrega também os relacionamentos N:1 (diretoria e rt) antecipadamente.
     * 
     * @param filtro condições que irão no where, em hql
     * @param ordem condições que irão no order by, em hql
     * @return lista de documentos com relacionamentos carregados
     */
    public List<Documento> listarComFetch(String filtro, String ordem) {
        List<Documento> documentos = null;
        Session session = null;
        try {
            session = openSession();
            
            // Query HQL com LEFT JOIN FETCH para carregar relacionamentos LAZY
            String hql = "SELECT DISTINCT t FROM Documento t " +
                         "LEFT JOIN FETCH t.diretoria " +
                         "LEFT JOIN FETCH t.rt " +
                         "WHERE " + filtro + " " + ordem;
            
            Query<Documento> query = session.createQuery(hql, Documento.class);
            
            printQuery(query);
            
            documentos = query.list();
            
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return documentos;
    }
}