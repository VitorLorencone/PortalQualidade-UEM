package exemplos.anexo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author alison
 *
 * Esta classe usa Lombok https://projectlombok.org assim não precisamos
 * declarar os getters e setters, basta usar as anotações @Data (que também
 * implementa os getters e setter e também o toString(), equals() e hashCode())
 * ou @Getter e @Setter (os quais apenas implementam os getters e setter).
 * Atenção: caso esta classe for transformada em json (por exemplo, em
 * aplicações REST/API), NÃO usar o @Data, utilizar apenas @Getter e @Setter
 * para não dar erro de referência circular com o toString. Mais em
 * https://projectlombok.org/features/Data e
 * https://projectlombok.org/features/GetterSetter
 */
@Getter
@Setter
public class Anexo implements Serializable {

    private Long cdAnexo;
    private String dsDirArquivo;
    private String nmArquivoServidor;
    private String nmArquivoOriginal;
    private String tpDocumento;
    
    private String dsUsuarioInserido;
    private Date dhInserido;
    private String dsUsuarioApagado;
    private Date dhApagado;

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
