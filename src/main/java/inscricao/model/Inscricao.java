package inscricao.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author equipes
 */
@Getter
@Setter
@Entity
@Table(name = "SIS_INSCRICAO")
public class Inscricao implements Serializable {

    //@Id define a chave primária da tabela (Obrigatório)
    @Id
    @Column(name = "CD_INSCRICAO")
    private Integer cdInscricao;

    @Column(name = "CD_CURSO")
    private Integer cdCurso;

    @Column(name = "CD_PESSOA")
    private Integer cdPessoa;

    @Column(name = "DT_INSCRICAO")
    @Temporal(TemporalType.DATE)
    private Date dtInscricao;

    @Column(name = "NU_FREQUENCIA")
    private String nuFrequencia;

    @Column(name = "FG_CERTIFICADO")
    private String fgCertificado;

    /**
     * Esta é a implementação customizada o toString(). Será impresso todo o
     * conteúdo do objeto de forma humanamente legível.
     *
     * @return o conteúdo do objeto em uma string formatada
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
