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
import javax.validation.constraints.NotNull;
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
        + "     L.LOC_NU AS NU_MUNICIPIO,"
        + "	CASE WHEN (SELECT COUNT(*) FROM dne.LOG_LOCALIDADE WHERE UFE_SG = L.UFE_SG AND LOC_NO = L.LOC_NO GROUP BY LOC_NO) > 1 AND L.LOC_IN_TIPO_LOC <> 'M' THEN L.LOC_NO || ' (' || (SELECT LOC_NO FROM DNE.LOG_LOCALIDADE WHERE LOC_NU = L.LOC_NU_SUB ) || ')' ELSE L.LOC_NO END AS NM_MUNICIPIO, "
        + "	UFE_SG AS SG_UF, "
        + "	CASE WHEN LENGTH(TRANSLATE(MUN_NU,'',TRANSLATE(MUN_NU,'','1234567890',''),'')) > 0 THEN INTEGER(TRANSLATE(MUN_NU,'',TRANSLATE(MUN_NU,'','1234567890',''),'')) ELSE (SELECT INTEGER(TRANSLATE(MUN_NU,'',TRANSLATE(MUN_NU,'','1234567890',''),'')) FROM DNE.LOG_LOCALIDADE WHERE LOC_NU = L.LOC_NU_SUB )  END AS CD_IBGE  "
        + "FROM "
        + "	DNE.LOG_LOCALIDADE L ")
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NU_MUNICIPIO")
    private Integer nuMunicipio;

    @Column(name = "NM_MUNICIPIO")
    private String nmMunicipio;

    @Column(name = "SG_UF")
    private String sgUf;

    @Column(name = "CD_IBGE")
    private Integer cdIbgeMunicipio;

}
