package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_ORGAO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Orgao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Adicionando geração automática de ID
    @Column(name = "CD_ORGAO")
    private Integer cdOrgao;

    @Column(name = "NM_ORGAO")
    private String nmOrgao;

}