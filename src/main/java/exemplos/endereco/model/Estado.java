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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

/**
 *
 * @author alison
 */
@Getter
@Setter
@Entity
@Immutable
@Subselect("SELECT DISTINCT "
        + "     E.UFE_SG AS SG_UF "
        + "FROM "
        + "     DNE.LOG_LOCALIDADE E ")
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "SG_UF")
    private String sgUf;

}
