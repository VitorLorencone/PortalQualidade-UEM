package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_MENSAGEM")
public class Mensagem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_MENSAGEM")
    private Integer cdMensagem;
    
    @Column(name = "DE_NOME", length = 200)
    private String deNome;
    
    @Column(name = "DE_CORPO", length = 1000)
    private String deCorpo;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_INICIO")
    private Date dtInicio;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_FIM")
    private Date dtFim;
    
    @Column(name = "DE_FREQUENCIA", length = 1)
    private String deFrequencia;

    @Column(name = "DE_CATEGORIA", length = 11)
    private String deCategoria;
    
    // Métodos auxiliares para trabalhar com o enum
    public FrequenciaMensagem getFrequenciaEnum() {
        return deFrequencia != null ? FrequenciaMensagem.fromCodigo(deFrequencia) : null;
    }
    
    public void setFrequenciaEnum(FrequenciaMensagem frequencia) {
        this.deFrequencia = frequencia != null ? frequencia.getCodigo() : null;
    }
}