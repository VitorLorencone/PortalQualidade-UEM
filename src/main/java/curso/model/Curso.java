/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Alunos de Ciência da Computação - 2025
 * Copyright (c) 2025. All rights reserved.
 */

package curso.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@Entity
@Table(name = "SIS_CURSO")
public class Curso implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CD_CURSO")
    private Integer cdCurso;

    @Column(name = "DE_TITULO")
    private String deTitulo;

    @Column(name = "TP_CURSO")
    private Short tpCurso;

    @Column(name = "NU_CARGA")
    private Short nuCarga;

    @Column(name = "DT_CURSO_INICIO")
    @Temporal(TemporalType.DATE)
    private Date dtCursoInicio;

    @Column(name = "DT_CURSO_FIM")
    @Temporal(TemporalType.DATE)
    private Date dtCursoFim;

    @Column(name = "NU_VAGAS")
    private Short nuVagas;

    @Column(name = "DT_INSCRICAO_INICIO")
    @Temporal(TemporalType.DATE)
    private Date dtInscricaoInicio;

    @Column(name = "DT_INSCRICAO_FIM")
    @Temporal(TemporalType.DATE)
    private Date dtInscricaoFim;

    @Column(name = "NM_ANEXO")
    private String nmUrl;

    //Exemplo de mapeamento ManyToMany
    //Com o nosso GenericDAO fechando a sessão a cada consulta, isso só funcionará com FetchType.EAGER, o qual é muito ruim para performance. Use com cautela.
    /*
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "SIS_INSCRICAO",
            joinColumns = {
                @JoinColumn(name = "CD_CURSO")},
            inverseJoinColumns = {
                @JoinColumn(name = "CD_PESSOA")}
    )
    List<Pessoa> pessoas = new ArrayList();
    */

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