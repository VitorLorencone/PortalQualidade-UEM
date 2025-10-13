package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_FUNCIONARIO_HU")
@PrimaryKeyJoinColumn(name = "CD_PESSOA") // FK para SIS_PESSOA
public class FuncionarioHU extends Pessoa {

    // Relacionamento N:N com Documento no contexto de Avaliadores
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_AVALIADORES",
        joinColumns = @JoinColumn(name = "CD_PESSOA"),
        inverseJoinColumns = @JoinColumn(name = "CD_DOCUMENTO")
    )
    private List<Documento> documentosAvaliador;

    // Relacionamento 1:N com Documento
    @OneToMany(mappedBy = "rt", fetch = FetchType.LAZY)
    private List<Documento> documentosRt;

    // Relacionamento N:N com Documento no contexto de Autores
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_AUTORES",
        joinColumns = @JoinColumn(name = "CD_PESSOA"),
        inverseJoinColumns = @JoinColumn(name = "CD_DOCUMENTO")
    )
    private List<Documento> documentosAutor;

}
