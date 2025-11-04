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
@Table(name = "SIS_SETOR")
@PrimaryKeyJoinColumn(name = "CD_ORGAO")
public class Setor extends Orgao {

    // Relacionamento bidirecional N:N com Documento
    // mappedBy indica que DOCUMENTO é o dono do relacionamento
    @ManyToMany(mappedBy = "setores", fetch = FetchType.LAZY)
    private List<Documento> documentos;

    // Caso precise, pode ter atributos específicos
    // @Column(name = "NM_RESPONSAVEL")
    // private String nmResponsavel;

}
