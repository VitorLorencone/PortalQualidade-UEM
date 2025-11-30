package model;

/**
 * Enum que representa os possíveis estados de um documento no sistema.
 */
public enum EstadoDocumento {
    
    NORMAZERO("Norma Zero", "normazero"),
    REVISAO("Revisão", "revisao"),
    AVALIACAO("Avaliação", "avaliacao"),
    ASSINATURA("Assinatura de Aprovação", "assinatura"),
    ELABORACAO("Elaboração", "elaboracao"),
    REUNIAO("Reunião de Alinhamento", "reuniao"),
    EXCLUSAO("Exclusão de Documento", "exclusao"),
    CANCELAMENTO("Cancelamento do Processo", "cancelamento");
    
    private final String descricao;
    private final String codigo;
    
    EstadoDocumento(String descricao, String codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    /**
     * Obtém o EstadoDocumento a partir do código
     */
    public static EstadoDocumento fromCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return ELABORACAO; // Estado padrão
        }
        
        for (EstadoDocumento estado : EstadoDocumento.values()) {
            if (estado.getCodigo().equalsIgnoreCase(codigo)) {
                return estado;
            }
        }
        
        return ELABORACAO; // Retorna o padrão se não encontrar
    }
    
    /**
     * Obtém o EstadoDocumento a partir da descrição
     */
    public static EstadoDocumento fromDescricao(String descricao) {
        if (descricao == null || descricao.isEmpty()) {
            return ELABORACAO;
        }
        
        for (EstadoDocumento estado : EstadoDocumento.values()) {
            if (estado.getDescricao().equalsIgnoreCase(descricao)) {
                return estado;
            }
        }
        
        return ELABORACAO;
    }
}