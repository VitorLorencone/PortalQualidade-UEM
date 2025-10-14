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
@Table(name = "SIS_PESSOA")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_PESSOA")
    private Integer cdPessoa;
    
    @Column(name = "NM_PESSOA")
    private String nome;
    
    @Column(name = "LT_EMAIL")
    private String email;
    
    @Column(name = "NU_CELULAR")
    private String celular;
}