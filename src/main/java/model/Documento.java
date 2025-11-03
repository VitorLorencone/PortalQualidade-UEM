package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_DOCUMENTO")
public class Documento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_DOCUMENTO")
    private Integer cdDocumento;

    @Column(name = "NM_DOCUMENTO")
    private String nmDocumento;

    @Column(name = "TP_DOCUMENTO")
    private String tpDocumento;

    @Column(name = "DT_CRIACAO")
    private Date dtCriacao;

    @Column(name = "DT_VENCIMENTO")
    private Date dtVencimento;

    @Column(name = "DE_DESCRICAO")
    private String deDescricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_ESTADO")
    private EstadoDocumento estado;

    // Relacionamento N:1 com Diretoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_ORGAO", referencedColumnName = "CD_ORGAO")
    private Diretoria diretoria;

    // Relacionamento N:1 com FuncionarioHU no contexto de Responsável Técnico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PESSOA", referencedColumnName = "CD_PESSOA")
    private FuncionarioHU rt;

    // RELACIONAMENTOS N:N (gerenciados em telas/abas separadas QUE SERÃO FEITAS DEPOIS)

    // Relacionamento N:N com Setor - DOCUMENTO é o dono
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_SETOR",
        joinColumns = @JoinColumn(name = "CD_DOCUMENTO"),
        inverseJoinColumns = @JoinColumn(name = "CD_ORGAO")
    )
    private List<Setor> setores;

    // Relacionamento N:N com FuncionarioQualidade - DOCUMENTO é o dono
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_FUNCIONARIO_QUALIDADE",
        joinColumns = @JoinColumn(name = "CD_DOCUMENTO"),
        inverseJoinColumns = @JoinColumn(name = "CD_PESSOA")
    )
    private List<FuncionarioQualidade> funcionariosQualidade;
    
    // Relacionamento N:N com FuncionarioHU (Avaliadores) - DOCUMENTO é o dono
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_AVALIADORES",
        joinColumns = @JoinColumn(name = "CD_DOCUMENTO"),
        inverseJoinColumns = @JoinColumn(name = "CD_PESSOA")
    )
    private List<FuncionarioHU> avaliadores;
    
    // Relacionamento N:N com FuncionarioHU (Autores) - DOCUMENTO é o dono
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "SIS_DOCUMENTO_AUTORES",
        joinColumns = @JoinColumn(name = "CD_DOCUMENTO"),
        inverseJoinColumns = @JoinColumn(name = "CD_PESSOA")
    )
    private List<FuncionarioHU> autores;

}
