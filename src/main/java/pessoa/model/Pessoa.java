package pessoa.model;

import curso.model.Curso;
import java.io.Serializable;
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
@Table(name = "SIS_PESSOA")
public class Pessoa implements Serializable {

    @Id
    @Column(name = "CD_PESSOA")
    private Integer cdPessoa;

    @Column(name = "TP_PESSOA")
    private String tpPessoa;

    @Column(name = "FG_SEXO")
    private String fgSexo;

    @Column(name = "NM_PESSOA")
    private String nmPessoa;

    @Column(name = "NU_RG")
    private String nuRg;

    @Column(name = "NM_RGEXPED")
    private String nmRgExped;

    @Column(name = "NU_CPF_CNPJ")
    private String NuCpfCnpj;

    @Column(name = "DE_ENDERECO")
    private String deEndereco;

    @Column(name = "DE_COMPLEMENTO")
    private String deComplemento;

    @Column(name = "NM_BAIRRO")
    private String nmBairro;

    @Column(name = "NM_CIDADE")
    private String nmCidade;

    @Column(name = "NM_UF")
    private String nmUf;

    @Column(name = "NU_CEP")
    private String nuCep;

    @Column(name = "NU_FONE")
    private String nuFone;

    @Column(name = "NU_CELULAR")
    private String nuCelular;

    @Column(name = "LT_EMAIL")
    private String ltEmail;

    //Exemplo de mapeamento Bidirecional, pegando o que já foi mapeamdo em Curso
    //Com o nosso GenericDAO fechando a sessão a cada consulta, isso só funcionará com FetchType.EAGER, o qual é muito ruim para performance. Use com cautela.
    //@ManyToMany(mappedBy = "pessoas", fetch = FetchType.EAGER)
    //private List<Curso> cursos;

}
