/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package exemplos.endereco.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author alison
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "mec.MEC_PAIS")
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "CD_PAIS")
    private String cdPais;

    @Column(name = "NM_PAIS")
    private String nmPais;

}
