package model;

public enum CategoriaMensagem {
    PADRAO("Padrão"), 
    CUSTOMIZADA("Customizada");

    private final String valor;

    CategoriaMensagem(String val) {
        this.valor = val;
    }

    public String getValue() {
        return this.valor;
    }

    public CategoriaMensagem enumFromValue(String val) {
        if (val.equalsIgnoreCase("Padrão")) {
            return PADRAO;
        } else { 
            return CUSTOMIZADA; 
        }
    }
}
