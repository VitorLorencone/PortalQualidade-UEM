package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    // Relacionamento N:N com Documento
    // mappedBy aponta para o campo "funcionariosQualidade" em Documento
    // Documento é o dono deste relacionamento
    @ManyToMany(mappedBy = "funcionariosQualidade", fetch = FetchType.LAZY)
    private List<Documento> documentos;
}
