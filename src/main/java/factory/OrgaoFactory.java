/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package factory;

import model.Orgao;
import model.Diretoria;
import model.Setor;
import dao.OrgaoDAO;
import dao.DiretoriaDAO;
import dao.SetorDAO;

/**
 * Factory para criar instâncias de Orgao e seus subtipos.
 * Também gerencia o DAO correspondente para salvar os objetos.
 */
public class OrgaoFactory {

    /**
     * Define os tipos disponíveis de órgão
     */
    public enum TipoOrgao {
        
        TODOS("todos", "Todos os tipos"), // Para uso em filtros
        ORGAO_GENERICO("generico_somente", "Órgão Genérico"),
        DIRETORIA("diretoria", "Diretoria"),
        SETOR("setor", "Setor");

        private final String codigo;
        private final String descricao;

        TipoOrgao(String codigo, String descricao) {
            this.codigo = codigo;
            this.descricao = descricao;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        public static TipoOrgao fromCodigo(String codigo) {
            if (codigo == null || codigo.isEmpty() || TODOS.getCodigo().equals(codigo)) {
                return TODOS; 
            }
            
            for (TipoOrgao tipo : TipoOrgao.values()) {
                if (tipo.getCodigo().equals(codigo)) {
                    return tipo;
                }
            }
            return TODOS; // Fallback
        }
    }

    /**
     * Cria uma instância de Orgão baseada no tipo especificado
     */
    public static Orgao criarOrgao(TipoOrgao tipo) {
        switch (tipo) {
            case DIRETORIA:
                return new Diretoria();
            case SETOR:
                return new Setor();
            case ORGAO_GENERICO:
            case TODOS:
            default:
                return new Orgao();
        }
    }

    /**
     * Obtém o tipo de órgão baseado em uma instância de Orgao
     */
    public static TipoOrgao obterTipo(Orgao orgao) {
        if (orgao instanceof Diretoria) {
            return TipoOrgao.DIRETORIA;
        } else if (orgao instanceof Setor) {
            return TipoOrgao.SETOR;
        } else {
            return TipoOrgao.ORGAO_GENERICO;
        }
    }

    /**
     * Obtém o DAO correspondente a uma instância de Orgao
     */
    public static Object obterDAO(Orgao orgao) {
        return obterDAO(obterTipo(orgao));
    }
    
    /**
     * Obtém o DAO correspondente ao tipo de órgão
     */
    public static Object obterDAO(TipoOrgao tipo) {
        switch (tipo) {
            case DIRETORIA:
                return new DiretoriaDAO();
            case SETOR:
                return new SetorDAO();
            case ORGAO_GENERICO:
            case TODOS:
            default:
                return new OrgaoDAO();
        }
    }

    /**
     * Salva um órgão usando o DAO apropriado.
     * O DAO agora usará o autoincremento para gerar o ID.
     */
    public static Integer salvar(Orgao orgao) {
        Object dao = obterDAO(orgao);
        
        // Agora usamos o incluirAutoincrementando (ou incluir, se ele fizer o autoincremento).
        // Presumo que o DAO.incluir() do GenericDAO lide com o @GeneratedValue.
        if (dao instanceof DiretoriaDAO) {
            // O ID é gerado no superclasse Orgao.
            return ((DiretoriaDAO) dao).incluir((Diretoria) orgao); 
        } else if (dao instanceof SetorDAO) {
            return ((SetorDAO) dao).incluir((Setor) orgao);
        } else {
            return ((OrgaoDAO) dao).incluir(orgao);
        }
    }

    /**
     * Atualiza um órgão usando o DAO apropriado
     */
    public static boolean atualizar(Orgao orgao) {
        Object dao = obterDAO(orgao);
        
        if (dao instanceof DiretoriaDAO) {
            return ((DiretoriaDAO) dao).atualizar((Diretoria) orgao);
        } else if (dao instanceof SetorDAO) {
            return ((SetorDAO) dao).atualizar((Setor) orgao);
        } else {
            return ((OrgaoDAO) dao).atualizar(orgao);
        }
    }

    /**
     * Exclui um órgão usando o DAO apropriado
     */
    public static boolean excluir(Orgao orgao) {
        Object dao = obterDAO(orgao);
        
        if (dao instanceof DiretoriaDAO) {
            return ((DiretoriaDAO) dao).excluir((Diretoria) orgao);
        } else if (dao instanceof SetorDAO) {
            return ((SetorDAO) dao).excluir((Setor) orgao);
        } else {
            return ((OrgaoDAO) dao).excluir(orgao);
        }
    }
}