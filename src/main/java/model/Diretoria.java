package model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_DIRETORIA")
@PrimaryKeyJoinColumn(name = "CD_ORGAO")
public class Diretoria extends Orgao {

    // Relacionamento 1:N com Documento
    @OneToMany(mappedBy = "diretoria", fetch = FetchType.LAZY)
    private List<Documento> documentos;

    // Caso precise, pode ter atributos específicos
    // @Column(name = "NM_DIRETOR")
    // private String nmDiretor;

}
