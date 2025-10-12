package login.dao;

import application.model.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class UsuarioDAO {

    private SessionFactory sessionFactory;

    public UsuarioDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Busca usuário por email ou username
     */
    public Usuario buscarPorLogin(String login) throws Exception {
        Session session = sessionFactory.openSession();

        try {
            // Query usando HQL (Hibernate Query Language)
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario u WHERE u.email = :login OR u.username = :login",
                    Usuario.class
            );
            query.setParameter("login", login);

            Usuario usuario = query.uniqueResult();
            return usuario;

        } finally {
            session.close();
        }
    }

    /**
     * Salva um novo usuário
     */
    public void salvar(Usuario usuario) throws Exception {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}