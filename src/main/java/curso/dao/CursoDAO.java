/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package curso.dao;

import curso.model.Curso;
import dao.GenericDAO;

/**
 * DAO do Curso que extende do DAO Genérico. Os métodos listar foram
 * implementados para já retornarem uma lista de Curso em vez de uma lista de
 * objetos genéricos.
 *
 * Importante: esse DAO extende do GenericDAO de modo que deve ser defindo em
 * <T, I> a classe (neste caso T é o Curso) e o tipo o ID da classe (neste caso
 * o Id é um Integer, então o I é um Integer), ficando então "extends
 * GenericDAO<Curso, Integer>".
 */
public class CursoDAO extends GenericDAO<Curso, Integer> {

    public CursoDAO() {
        super(Curso.class);
    }

}