package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_SETOR")
@PrimaryKeyJoinColumn(name = "CD_ORGAO")
public class Setor extends Orgao {

    // Relacionamento com Documento
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_SETOR",
        joinColumns = @JoinColumn(name = "CD_ORGAO"),
        inverseJoinColumns = @JoinColumn(name = "CD_DOCUMENTO")
    )
    private List<Documento> documentos;

    // Caso precise, pode ter atributos específicos
    // @Column(name = "NM_RESPONSAVEL")
    // private String nmResponsavel;

}
