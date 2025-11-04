package dao;

import model.Documento;
import model.FuncionarioHU;
import model.Setor;
import model.FuncionarioQualidade;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.ArrayList;
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
    
    /**
     * Busca um documento e inicializa os relacionamentos N:N (setores e funcionários qualidade)
     * 
     * @param cdDocumento código do documento
     * @return documento com relacionamentos N:N inicializados
     */
    public Documento buscarComRelacionamentos(Integer cdDocumento) {
        Documento documento = null;
        Session session = null;
        try {
            session = openSession();
            documento = (Documento) session.get(Documento.class, cdDocumento);
            
            if (documento != null) {
                // Inicializa as coleções LAZY para evitar LazyInitializationException
                Hibernate.initialize(documento.getAvaliadores());
                Hibernate.initialize(documento.getAutores());
                Hibernate.initialize(documento.getSetores());
                Hibernate.initialize(documento.getFuncionariosQualidade());
                
                System.out.println("Documento " + cdDocumento + " carregado com " + 
                    (documento.getAvaliadores() != null ? documento.getAvaliadores().size() : 0) + " avaliadores, " +
                    (documento.getAutores() != null ? documento.getAutores().size() : 0) + " autores, " +
                    (documento.getSetores() != null ? documento.getSetores().size() : 0) + " setores e " +
                    (documento.getFuncionariosQualidade() != null ? documento.getFuncionariosQualidade().size() : 0) + " funcionários");
            }
            
        } catch (Exception e) {
            printException(e);
        } finally {
            closeSession(session);
        }
        return documento;
    }
    
    /**
     * Adiciona um setor ao documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdSetor código do setor
     * @return true se adicionou com sucesso
     */
    public boolean adicionarSetor(Integer cdDocumento, Integer cdSetor) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== ADICIONANDO SETOR ===");
            System.out.println("Documento ID: " + cdDocumento);
            System.out.println("Setor ID: " + cdSetor);
            
            // Busca o documento
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            if (documento == null) {
                System.err.println("ERRO: Documento não encontrado!");
                return false;
            }
            
            // Inicializa a lista de setores
            Hibernate.initialize(documento.getSetores());
            
            // Busca o setor
            Setor setor = (Setor) session.get(Setor.class, cdSetor);
            if (setor == null) {
                System.err.println("ERRO: Setor não encontrado!");
                return false;
            }
            
            System.out.println("Documento encontrado: " + documento.getNmDocumento());
            System.out.println("Setor encontrado: " + setor.getNmOrgao());
            
            // Inicializa a lista se for null
            if (documento.getSetores() == null) {
                documento.setSetores(new ArrayList<>());
                System.out.println("Lista de setores inicializada");
            }
            
            System.out.println("Setores atuais no documento: " + documento.getSetores().size());
            
            // Verifica se já não está vinculado
            boolean jaVinculado = documento.getSetores().stream()
                .anyMatch(s -> s.getCdOrgao().equals(cdSetor));
            
            if (jaVinculado) {
                System.out.println("Setor já está vinculado!");
                return false;
            }
            
            // Adiciona o setor à lista
            documento.getSetores().add(setor);
            System.out.println("Setor adicionado à lista. Total agora: " + documento.getSetores().size());
            
            // Salva as mudanças
            session.update(documento);
            session.flush();
            
            System.out.println("Update e flush executados");
            
            commit(session);
            
            System.out.println("Commit executado com sucesso!");
            sucesso = true;
            
        } catch (Exception e) {
            System.err.println("ERRO ao adicionar setor:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Remove um setor do documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdSetor código do setor
     * @return true se removeu com sucesso
     */
    public boolean removerSetor(Integer cdDocumento, Integer cdSetor) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== REMOVENDO SETOR ===");
            System.out.println("Documento ID: " + cdDocumento);
            System.out.println("Setor ID: " + cdSetor);
            
            // Busca o documento e inicializa a lista de setores
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            Hibernate.initialize(documento.getSetores());
            
            if (documento != null && documento.getSetores() != null) {
                System.out.println("Setores antes de remover: " + documento.getSetores().size());
                
                // Remove o setor da lista
                boolean removeu = documento.getSetores().removeIf(s -> s.getCdOrgao().equals(cdSetor));
                
                if (removeu) {
                    System.out.println("Setor removido da lista. Total agora: " + documento.getSetores().size());
                    session.update(documento);
                    session.flush();
                    sucesso = true;
                } else {
                    System.out.println("Setor não estava na lista");
                }
            }
            
            commit(session);
            
        } catch (Exception e) {
            System.err.println("ERRO ao remover setor:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Adiciona um funcionário qualidade ao documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdPessoa código do funcionário qualidade
     * @return true se adicionou com sucesso
     */
    public boolean adicionarFuncionarioQualidade(Integer cdDocumento, Integer cdPessoa) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== ADICIONANDO FUNCIONÁRIO QUALIDADE ===");
            System.out.println("Documento ID: " + cdDocumento);
            System.out.println("Funcionário ID: " + cdPessoa);
            
            // Busca o documento
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            if (documento == null) {
                System.err.println("ERRO: Documento não encontrado!");
                return false;
            }
            
            // Inicializa a lista
            Hibernate.initialize(documento.getFuncionariosQualidade());
            
            // Busca o funcionário qualidade
            FuncionarioQualidade funcionario = (FuncionarioQualidade) session.get(FuncionarioQualidade.class, cdPessoa);
            if (funcionario == null) {
                System.err.println("ERRO: Funcionário não encontrado!");
                return false;
            }
            
            System.out.println("Documento encontrado: " + documento.getNmDocumento());
            System.out.println("Funcionário encontrado: " + funcionario.getNome());
            
            // Inicializa a lista se for null
            if (documento.getFuncionariosQualidade() == null) {
                documento.setFuncionariosQualidade(new ArrayList<>());
            }
            
            // Verifica se já não está vinculado
            boolean jaVinculado = documento.getFuncionariosQualidade().stream()
                .anyMatch(f -> f.getCdPessoa().equals(cdPessoa));
            
            if (jaVinculado) {
                System.out.println("Funcionário já está vinculado!");
                return false;
            }
            
            // Adiciona o funcionário à lista
            documento.getFuncionariosQualidade().add(funcionario);
            System.out.println("Funcionário adicionado à lista");
            
            session.update(documento);
            session.flush();
            
            commit(session);
            
            System.out.println("Commit executado com sucesso!");
            sucesso = true;
            
        } catch (Exception e) {
            System.err.println("ERRO ao adicionar funcionário:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Remove um funcionário qualidade do documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdPessoa código do funcionário qualidade
     * @return true se removeu com sucesso
     */
    public boolean removerFuncionarioQualidade(Integer cdDocumento, Integer cdPessoa) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== REMOVENDO FUNCIONÁRIO QUALIDADE ===");
            
            // Busca o documento e inicializa a lista
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            Hibernate.initialize(documento.getFuncionariosQualidade());
            
            if (documento != null && documento.getFuncionariosQualidade() != null) {
                // Remove o funcionário da lista
                boolean removeu = documento.getFuncionariosQualidade().removeIf(f -> f.getCdPessoa().equals(cdPessoa));
                
                if (removeu) {
                    session.update(documento);
                    session.flush();
                    sucesso = true;
                }
            }
            
            commit(session);
            
        } catch (Exception e) {
            System.err.println("ERRO ao remover funcionário:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Adiciona um avaliador ao documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdPessoa código do avaliador
     * @return true se adicionou com sucesso
     */
    public boolean adicionarAvaliador(Integer cdDocumento, Integer cdPessoa) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== ADICIONANDO AVALIADOR ===");
            System.out.println("Documento ID: " + cdDocumento);
            System.out.println("Funcionário ID: " + cdPessoa);
            
            // Busca o documento
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            if (documento == null) {
                System.err.println("ERRO: Documento não encontrado!");
                return false;
            }
            
            // Inicializa a lista
            Hibernate.initialize(documento.getAvaliadores());
            
            // Busca o avaliador
            FuncionarioHU funcionario = (FuncionarioHU) session.get(FuncionarioHU.class, cdPessoa);
            if (funcionario == null) {
                System.err.println("ERRO: Funcionário não encontrado!");
                return false;
            }
            
            System.out.println("Documento encontrado: " + documento.getNmDocumento());
            System.out.println("Funcionário encontrado: " + funcionario.getNome());
            
            // Inicializa a lista se for null
            if (documento.getAvaliadores() == null) {
                documento.setAvaliadores(new ArrayList<>());
            }
            
            // Verifica se já não está vinculado
            boolean jaVinculado = documento.getAvaliadores().stream()
                .anyMatch(f -> f.getCdPessoa().equals(cdPessoa));
            
            if (jaVinculado) {
                System.out.println("Funcionário já está vinculado!");
                return false;
            }
            
            // Adiciona o avaliador à lista
            documento.getAvaliadores().add(funcionario);
            System.out.println("Avaliador adicionado à lista");
            
            session.update(documento);
            session.flush();
            
            commit(session);
            
            System.out.println("Commit executado com sucesso!");
            sucesso = true;
            
        } catch (Exception e) {
            System.err.println("ERRO ao adicionar funcionário:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Remove um avaliador do documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdPessoa código do avaliador
     * @return true se removeu com sucesso
     */
    public boolean removerAvaliador(Integer cdDocumento, Integer cdPessoa) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== REMOVENDO AVALIADOR ===");
            
            // Busca o documento e inicializa a lista
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            Hibernate.initialize(documento.getAvaliadores());
            
            if (documento != null && documento.getAvaliadores() != null) {
                // Remove o funcionário da lista
                boolean removeu = documento.getAvaliadores().removeIf(f -> f.getCdPessoa().equals(cdPessoa));
                
                if (removeu) {
                    session.update(documento);
                    session.flush();
                    sucesso = true;
                }
            }
            
            commit(session);
            
        } catch (Exception e) {
            System.err.println("ERRO ao remover avaliador:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Adiciona um autor ao documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdPessoa código do autor
     * @return true se adicionou com sucesso
     */
    public boolean adicionarAutor(Integer cdDocumento, Integer cdPessoa) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== ADICIONANDO AUTOR ===");
            System.out.println("Documento ID: " + cdDocumento);
            System.out.println("Funcionário ID: " + cdPessoa);
            
            // Busca o documento
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            if (documento == null) {
                System.err.println("ERRO: Documento não encontrado!");
                return false;
            }
            
            // Inicializa a lista
            Hibernate.initialize(documento.getAutores());
            
            // Busca o avaliador
            FuncionarioHU funcionario = (FuncionarioHU) session.get(FuncionarioHU.class, cdPessoa);
            if (funcionario == null) {
                System.err.println("ERRO: Funcionário não encontrado!");
                return false;
            }
            
            System.out.println("Documento encontrado: " + documento.getNmDocumento());
            System.out.println("Funcionário encontrado: " + funcionario.getNome());
            
            // Inicializa a lista se for null
            if (documento.getAutores() == null) {
                documento.setAutores(new ArrayList<>());
            }
            
            // Verifica se já não está vinculado
            boolean jaVinculado = documento.getAutores().stream()
                .anyMatch(f -> f.getCdPessoa().equals(cdPessoa));
            
            if (jaVinculado) {
                System.out.println("Autor já está vinculado!");
                return false;
            }
            
            // Adiciona o autor à lista
            documento.getAutores().add(funcionario);
            System.out.println("Autor adicionado à lista");
            
            session.update(documento);
            session.flush();
            
            commit(session);
            
            System.out.println("Commit executado com sucesso!");
            sucesso = true;
            
        } catch (Exception e) {
            System.err.println("ERRO ao adicionar funcionário:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
    
    /**
     * Remove um autor do documento (relacionamento N:N)
     * 
     * @param cdDocumento código do documento
     * @param cdPessoa código do autor
     * @return true se removeu com sucesso
     */
    public boolean removerAutor(Integer cdDocumento, Integer cdPessoa) {
        boolean sucesso = false;
        Session session = null;
        try {
            session = openSession();
            beginTransaction(session);
            
            System.out.println("=== REMOVENDO AUTOR ===");
            
            // Busca o documento e inicializa a lista
            Documento documento = (Documento) session.get(Documento.class, cdDocumento);
            Hibernate.initialize(documento.getAutores());
            
            if (documento != null && documento.getAutores() != null) {
                // Remove o funcionário da lista
                boolean removeu = documento.getAutores().removeIf(f -> f.getCdPessoa().equals(cdPessoa));
                
                if (removeu) {
                    session.update(documento);
                    session.flush();
                    sucesso = true;
                }
            }
            
            commit(session);
            
        } catch (Exception e) {
            System.err.println("ERRO ao remover avaliador:");
            printException(e);
            rollback(session);
        } finally {
            closeSession(session);
        }
        return sucesso;
    }
}