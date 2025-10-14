/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.query.Query;
import org.hibernate.Session;

/**
 * DAO genérico. Esta classe não dá para ser usada diretamente. Todo DAO deve
 * extender desta classe.
 */
public abstract class GenericDAO<T, I extends Serializable> extends DAO {

    private final Class<T> classe;
    private static final boolean SHOW_SQL_TO_DEBUG = true;

    public GenericDAO(Class<T> classe) {
        this.classe = classe;
    }

    public Class<T> getClasse() {
        return classe;
    }

    /**
     * Salvar um objeto no banco. A geração do ID é feita automaticamente pelo
     * Hibernate usando @GeneratedValue(strategy = GenerationType.IDENTITY).
     *
     * @param entidade objeto a ser inserido no banco
     * @return id do objeto inserido
     */
    public I incluirAutoincrementando(T entidade) {
        I id = null;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            id = incluir(session, entidade);
            commit(session);
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return id;
    }

    /**
     * Salvar um objeto no banco. Este método NÃO fará o autoincremento do @Id,
     * utilize caso seu objeto utilize chave composta.
     *
     * @param entidade objeto a ser inserido no banco
     * @return id do objeto inserido
     */
    public I incluir(T entidade) {
        I id = null;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            id = incluir(session, entidade);
            commit(session);
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return id;
    }

    /**
     * Salvar um objeto no banco de forma transacional. Utilize este método caso
     * precise fazer várias alterações no banco na mesma transação. Será
     * necessário pegar a sessão e iniciar a transação manualmente, passar a
     * sessão para este método, depois commitar ou fazer rollback, e fechar a
     * sessão.
     *
     * @param session sessão do hibernate
     * @param entidade objeto a ser inserido no banco
     * @return id do objeto inserido
     */
    public I incluir(Session session, T entidade) {
        I idEntidade = (I) session.save(entidade);
        session.flush();
        session.refresh(entidade);
        return idEntidade;
    }

    /**
     * Exclui um objeto do banco.
     *
     * @param entidade objeto a ser excluído
     * @return true caso sucesso, false caso ocorra algum erro
     */
    public boolean excluir(T entidade) {
        boolean excluded = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            excluir(session, entidade);
            commit(session);
            excluded = true;
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return excluded;
    }

    /**
     * Exclui um objeto do banco de forma transacional. Utilize este método caso
     * precise fazer várias alterações no banco na mesma transação. Será
     * necessário pegar a sessão e iniciar a transação manualmente, passar a
     * sessão para este método, depois commitar ou fazer rollback, e fechar a
     * sessão.
     *
     * @param session sessão do hibernate
     * @param entidade objeto a ser excluído
     */
    public void excluir(Session session, T entidade) {
        session.delete(entidade);
        session.flush();
    }

    /**
     * Exclui um objeto do banco passando um id. Utilize este método caso você
     * não tenha o objeto que veio do banco de dados, ou seja, tenha apenas o
     * valor do id.
     *
     * @param codigo id do objeto a ser excluído
     * @return true caso sucesso, false caso ocorra algum erro
     */
    public boolean excluir(I codigo) {
        boolean excluded = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            excluir(session, codigo);
            commit(session);
            excluded = true;
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return excluded;
    }

    /**
     * Exclui um objeto do banco de forma transacional, passando um id. Utilize
     * este método caso você não tenha o objeto que veio do banco de dados, ou
     * seja, tenha apenas o valor do id.
     *
     * @param session sessão do hibernate
     * @param codigo id do objeto a ser excluído
     */
    public void excluir(Session session, I codigo) {
        T genericClass = (T) session.get(classe, codigo);
        if (genericClass != null) {
            session.delete(genericClass);
        }
    }

    /**
     * Atualiza um objeto no banco.
     *
     * @param entidade objeto a ser atualizado
     * @return true caso sucesso, false caso ocorra algum erro
     */
    public boolean atualizar(T entidade) {
        boolean updated = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            atualizar(session, entidade);
            commit(session);
            updated = true;
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return updated;
    }

    /**
     * Atualiza um objeto no banco de forma transacional. Utilize este método
     * caso precise fazer várias alterações no banco na mesma transação. Será
     * necessário pegar a sessão e iniciar a transação manualmente, passar a
     * sessão para este método, depois commitar ou fazer rollback, e fechar a
     * sessão.
     *
     * @param session sessão do hibernate
     * @param entidade objeto a ser atualizado
     */
    public void atualizar(Session session, T entidade) {
        session.update(entidade);
        session.flush();
        session.refresh(entidade);
    }

    /**
     * Buscar um objeto no banco de dados. Para herança JOINED, retorna o tipo
     * correto (FuncionarioHU, FuncionarioQualidade, ou Pessoa).
     *
     * @param codigo id do objeto a ser encontrado
     * @return o objeto encontrado desejado
     */
    public T buscar(I codigo) {
        T object = null;
        Session session = null;
        try {
            session = openSession();
            object = (T) session.get(classe, codigo);
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return object;
    }

    /**
     * Lista todos os objetos existentes no banco da classe desejada.
     *
     * @return lista de objetos
     */
    public List<T> listar() {
        List<T> objs = null;
        Session session = null;
        try {
            session = openSession();
            CriteriaQuery criteria = session.getCriteriaBuilder().createQuery(classe);
            Query query = session.createQuery(criteria);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return objs;
    }

    /**
     * Lista os objetos da classe baseado em um filtro e ordem.
     *
     * @param filtro condições que irão no where, em hql
     * @param ordem condições que irão no order by, em hql
     * @return lista de objetos
     */
    public List<T> listar(String filtro, String ordem) {
        List<T> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery("from " + classe.getSimpleName() + " t where " + filtro + " " + ordem);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return objs;
    }

    /**
     * Lista os objetos da classe baseado em um filtro e ordem de forma paginada.
     *
     * @param filtro condições que irão no where, em hql
     * @param ordem condições que irão no order by, em hql
     * @param page é o offset, ou seja, inicio dos dados a serem buscados
     * @param pageSize é o limit, ou seja, quantidade de dados a serem buscados
     * @return lista de objetos
     */
    public List<T> listarPaginado(String filtro, String ordem, int page, int pageSize) {
        List<T> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery("from " + classe.getSimpleName() + " t where " + filtro + " " + ordem);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return objs;
    }

    /**
     * Quantidade de dados encontrados na tabela.
     *
     * @param filtro condições que irão no where, em hql
     * @return quantidade de dados encontrados
     */
    public int contar(String filtro) {
        int count = 0;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery("select count(*) from " + classe.getSimpleName() + " t where " + filtro);

            printQuery(query);

            count = ((Long) query.uniqueResult()).intValue();
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return count;
    }

    /**
     * Executa um Select em HQL.
     *
     * @param hql sql em hql a ser executado o select
     * @return lista de objetos
     */
    public List<Object[]> executarSelectHql(String hql) {
        List<Object[]> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createQuery(hql);

            printQuery(query);

            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return objs;
    }

    /**
     * Executa um Select em SQL puro.
     *
     * @param sql sql puro a ser executado o select
     * @return lista de objetos
     */
    public List<Object[]> executarSelectSql(String sql) {
        List<Object[]> objs = null;
        Session session = null;
        try {
            session = openSession();
            Query query = session.createNativeQuery(sql);
            objs = query.list();
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return objs;
    }

    /**
     * Executa um Update em SQL puro.
     *
     * @param sql sql puro a ser executado o update
     * @return número de linhas atualizadas pelo update
     */
    public int executarUpdateSql(String sql) {
        int result = 0;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            result = executarUpdateSql(session, sql);
            commit(session);
        } catch (Exception e) {
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return result;
    }

    /**
     * Executa um Update em SQL puro.
     *
     * @param session sessão do hibernate
     * @param sql sql puro a ser executado o update
     * @return número de linhas atualizadas pelo update
     */
    public int executarUpdateSql(Session session, String sql) {
        int result = 0;
        try {
            result = session.createNativeQuery(sql).executeUpdate();
        } catch (Exception e) {
            printException(e);
        }
        return result;
    }
}