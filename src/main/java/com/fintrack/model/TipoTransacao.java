package com.fintrack.model;

public enum TipoTransacao {
    RECEITA("Receita", "+"),
    DESPESA("Despesa", "-");

    private String descricao;
    private String sinal;

    TipoTransacao(String descricao, String sinal) {
        this.descricao = descricao;
        this.sinal = sinal;
    }

    public String getDescricao() { return descricao; }
    public String getSinal() { return sinal; }
}