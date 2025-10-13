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
@Table(name = "SIS_FUNCIONARIO_QUALIDADE")
@PrimaryKeyJoinColumn(name = "CD_PESSOA") // FK para SIS_PESSOA
public class FuncionarioQualidade extends Pessoa {

    // Relacionamento com Documento
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_FUNCIONARIO_QUALIDADE",
        joinColumns = @JoinColumn(name = "CD_PESSOA"),
        inverseJoinColumns = @JoinColumn(name = "CD_DOCUMENTO")
    )
    private List<Documento> documentos;
}
