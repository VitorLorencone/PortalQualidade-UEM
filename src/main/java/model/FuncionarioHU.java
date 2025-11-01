package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    // mappedBy aponta para o campo "avaliadores" em Documento
    // Documento é o dono deste relacionamento
    @ManyToMany(mappedBy = "avaliadores", fetch = FetchType.LAZY)
    private List<Documento> documentosAvaliador;

    // Relacionamento 1:N com Documento
    @OneToMany(mappedBy = "rt", fetch = FetchType.LAZY)
    private List<Documento> documentosRt;

    // Relacionamento N:N com Documento no contexto de Autores
    // mappedBy aponta para o campo "autores" em Documento
    // Documento é o dono deste relacionamento
    @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
    private List<Documento> documentosAutor;

}
