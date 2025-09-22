package model;


import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIS_USUARIO")
@PrimaryKeyJoinColumn(name = "CD_PESSOA") // FK para SIS_PESSOA
public class Usuario extends Pessoa {

    // Pode ter atributos específicos de usuário
    // Exemplo: login e senha
    private String login;
    private String senha;
}
