/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package factory;

import model.Pessoa;
import model.FuncionarioHU;
import model.FuncionarioQualidade;
import dao.PessoaDAO;
import dao.FuncionarioHUDAO;
import dao.FuncionarioQualidadeDAO;

/**
 * Factory para criar instâncias de Pessoa e seus subtipos.
 * Também gerencia o DAO correspondente para salvar os objetos.
 */
public class PessoaFactory {

    /**
     * Define os tipos disponíveis de pessoa
     */
    public enum TipoPessoa {
        
        // NOVO TIPO: Representa a opção "Todos os Tipos" no filtro, sem aplicar restrição
        TODOS("todos", "Todos os tipos"),
        
        // MODIFICADO: Use um código explícito para "Pessoa Genérica" no filtro
        PESSOA_GENERICA("generica_somente", "Pessoa Genérica"),
        
        FUNCIONARIO_HU("hu", "Funcionário HU"),
        FUNCIONARIO_QUALIDADE("qualidade", "Funcionário Qualidade");

        private final String codigo;
        private final String descricao;

        TipoPessoa(String codigo, String descricao) {
            this.codigo = codigo;
            this.descricao = descricao;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        public static TipoPessoa fromCodigo(String codigo) {
            
            // Tratamento para "Todos os tipos" (código vazio ou "todos")
            if (codigo == null || codigo.isEmpty() || TODOS.getCodigo().equals(codigo)) {
                return TODOS;
            }
            
            // Nota: O código original usava "generica", que vamos substituir por "generica_somente"
            // para o propósito de filtragem. Manter a coerência aqui é essencial.
            
            for (TipoPessoa tipo : TipoPessoa.values()) {
                if (tipo.getCodigo().equals(codigo)) {
                    return tipo;
                }
            }
            
            // Se não encontrar, retorna TODOS para não aplicar filtro por padrão
            return TODOS;
        }
    }

    /**
     * Cria uma instância de Pessoa baseada no tipo especificado
     *
     * @param tipo o tipo de pessoa a ser criado
     * @return uma nova instância do tipo especificado
     */
    public static Pessoa criarPessoa(TipoPessoa tipo) {
        switch (tipo) {
            case FUNCIONARIO_HU:
                return new FuncionarioHU();
            case FUNCIONARIO_QUALIDADE:
                return new FuncionarioQualidade();
            default:
                return new Pessoa();
        }
    }

    /**
     * Cria uma instância de Pessoa baseada no código string
     *
     * @param codigoPessoa o código do tipo de pessoa
     * @return uma nova instância do tipo especificado
     */
    public static Pessoa criarPessoa(String codigoPessoa) {
        return criarPessoa(TipoPessoa.fromCodigo(codigoPessoa));
    }

    /**
     * Obtém o tipo de pessoa baseado em uma instância de Pessoa
     *
     * @param pessoa a instância de Pessoa
     * @return o tipo correspondente
     */
    public static TipoPessoa obterTipo(Pessoa pessoa) {
        if (pessoa instanceof FuncionarioHU) {
            return TipoPessoa.FUNCIONARIO_HU;
        } else if (pessoa instanceof FuncionarioQualidade) {
            return TipoPessoa.FUNCIONARIO_QUALIDADE;
        } else {
            return TipoPessoa.PESSOA_GENERICA;
        }
    }

    /**
     * Obtém o DAO correspondente ao tipo de pessoa
     *
     * @param tipo o tipo de pessoa
     * @return o DAO para o tipo especificado
     */
    public static Object obterDAO(TipoPessoa tipo) {
        switch (tipo) {
            case FUNCIONARIO_HU:
                return new FuncionarioHUDAO();
            case FUNCIONARIO_QUALIDADE:
                return new FuncionarioQualidadeDAO();
            default:
                return new PessoaDAO();
        }
    }

    /**
     * Obtém o DAO correspondente a uma instância de Pessoa
     *
     * @param pessoa a instância de Pessoa
     * @return o DAO para o tipo da Pessoa
     */
    public static Object obterDAO(Pessoa pessoa) {
        return obterDAO(obterTipo(pessoa));
    }

    /**
     * Salva uma pessoa usando o DAO apropriado.
     * O ID é gerado automaticamente pelo Hibernate via @GeneratedValue(IDENTITY)
     *
     * @param pessoa a pessoa a ser salva
     * @return o ID da pessoa salva
     */
    public static Integer salvar(Pessoa pessoa) {
        Object dao = obterDAO(pessoa);
        
        if (dao instanceof FuncionarioHUDAO) {
            return ((FuncionarioHUDAO) dao).incluir((FuncionarioHU) pessoa);
        } else if (dao instanceof FuncionarioQualidadeDAO) {
            return ((FuncionarioQualidadeDAO) dao).incluir((FuncionarioQualidade) pessoa);
        } else {
            return ((PessoaDAO) dao).incluir(pessoa);
        }
    }

    /**
     * Atualiza uma pessoa usando o DAO apropriado
     *
     * @param pessoa a pessoa a ser atualizada
     * @return true se a atualização foi bem-sucedida
     */
    public static boolean atualizar(Pessoa pessoa) {
        Object dao = obterDAO(pessoa);
        
        if (dao instanceof FuncionarioHUDAO) {
            return ((FuncionarioHUDAO) dao).atualizar((FuncionarioHU) pessoa);
        } else if (dao instanceof FuncionarioQualidadeDAO) {
            return ((FuncionarioQualidadeDAO) dao).atualizar((FuncionarioQualidade) pessoa);
        } else {
            return ((PessoaDAO) dao).atualizar(pessoa);
        }
    }

    /**
     * Exclui uma pessoa usando o DAO apropriado
     *
     * @param pessoa a pessoa a ser excluída
     * @return true se a exclusão foi bem-sucedida
     */
    public static boolean excluir(Pessoa pessoa) {
        Object dao = obterDAO(pessoa);
        
        if (dao instanceof FuncionarioHUDAO) {
            return ((FuncionarioHUDAO) dao).excluir((FuncionarioHU) pessoa);
        } else if (dao instanceof FuncionarioQualidadeDAO) {
            return ((FuncionarioQualidadeDAO) dao).excluir((FuncionarioQualidade) pessoa);
        } else {
            return ((PessoaDAO) dao).excluir(pessoa);
        }
    }
}