package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_MENSAGEM")
public class Mensagem implements Serializable {

    @Id
    @Column(name = "CD_MENSAGEM")
    private Integer cdMensagem;

    @Column(name = "DE_CORPO")
    private String deCorpo;

    // Relacionamento com Documento
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_MENSAGEM",
        joinColumns = @JoinColumn(name = "CD_MENSAGEM"),
        inverseJoinColumns = @JoinColumn(name = "CD_DOCUMENTO")
    )
    private List<Documento> documentos;

    // Relacionamento com Regra
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_MENSAGEM_REGRA",
        joinColumns = @JoinColumn(name = "CD_MENSAGEM"),
        inverseJoinColumns = @JoinColumn(name = "CD_REGRA")
    )
    private List<Regra> regras;

}
