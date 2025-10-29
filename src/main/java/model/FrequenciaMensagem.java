package model;

public enum FrequenciaMensagem {
    UNICA("Única", "U"),
    DIARIA("Diária", "D"),
    SEMANAL("Semanal", "S"),
    MENSAL("Mensal", "M"),
    ANUAL("Anual", "A");
    
    private final String descricao;
    private final String codigo;
    
    FrequenciaMensagem(String descricao, String codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public static FrequenciaMensagem fromCodigo(String codigo) {
        for (FrequenciaMensagem freq : values()) {
            if (freq.codigo.equals(codigo)) {
                return freq;
            }
        }
        return UNICA;
    }
}