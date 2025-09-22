package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    @Column(name = "ST_INICIAL")
    private String stInicial;

    @Column(name = "ST_FINAL")
    private String stFinal;

    // Relacionamento N:1 com Diretoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_ORGAO", referencedColumnName = "CD_ORGAO")
    private Diretoria diretoria;

    // Relacionamento bidirecional com Setor
    @ManyToMany(mappedBy = "documentos", fetch = FetchType.LAZY)
    private List<Setor> setores;

    // Relacionamento bidirecional com Mensagem
    @ManyToMany(mappedBy = "documentos", fetch = FetchType.LAZY)
    private List<Mensagem> mensagens;

    // Relacionamento bidirecional com FuncionárioQualidade
    @ManyToMany(mappedBy = "documentos", fetch = FetchType.LAZY)
    private List<FuncionarioQualidade> funcionariosQualidade;

    // Relacionamento bidirecional com FuncionarioHU no contexto de Avaliador
    @ManyToMany(mappedBy = "documentosAvaliador", fetch = FetchType.LAZY)
    private List<FuncionarioHU> avaliadores;

    // Relacionamento N:1 com FuncionarioHU no contexto de Responsável Técnico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PESSOA", referencedColumnName = "CD_PESSOA")
    private FuncionarioHU rt;

    // Relacionamento bidirecional com FuncionarioHU no contexto de Autor
    @ManyToMany(mappedBy = "documentosAutor", fetch = FetchType.LAZY)
    private List<FuncionarioHU> autores;

}
