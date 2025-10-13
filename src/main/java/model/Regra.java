package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_REGRA")
public class Regra implements Serializable {

    @Id
    @Column(name = "CD_REGRA")
    private Integer cdRegra;

    @Column(name = "NM_REGRA")
    private String nome;

    @Column(name = "DE_GATILHO")
    private String deGatilho;

    @Column(name = "DT_INICIO")
    private Date dtInicio;

    @Column(name = "DT_FIM")
    private Date dtFim;

    @Column(name = "DE_FREQUENCIA")
    private String deFrequencia;

    // Relacionamento com Mensagem (lado inverso)
    @ManyToMany(mappedBy = "regras", fetch = FetchType.LAZY)
    private List<Mensagem> mensagens;

}
