/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package exemplos.endereco.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author alison
 */
@Getter
@Setter
public class EnderecoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sgUf;

    private Integer nuMunicipio;

    private String nmMunicipio;

    private String nmBairro;

    private String nmLogradouro;

    private Integer cdIbgeMunicipio;

    private Integer nuCep;

}
