package com.example.model;

import java.text.DecimalFormat;

public class ResultadoBenchmark {
    private final String nomeAlgoritmo;
    private final TipoVetor tipoVetor;
    private final int tamanhoVetor;
    private final double tempoMedioEmMilisegundos;

    public ResultadoBenchmark(String nomeAlgoritmo, TipoVetor tipoVetor, int tamanhoVetor, double tempoMedioEmMilisegundos) {
        this.nomeAlgoritmo = nomeAlgoritmo;
        this.tipoVetor = tipoVetor;
        this.tamanhoVetor = tamanhoVetor;
        this.tempoMedioEmMilisegundos = tempoMedioEmMilisegundos;
    }

    // Métodos "get" para acessar os dados
    public String getNomeAlgoritmo() { return nomeAlgoritmo; }
    public double getTempoMedioEmMilisegundos() { return tempoMedioEmMilisegundos; }

    // --- MÉTODOS QUE FALTAVAM ---
    public TipoVetor getTipoVetor() {
        return tipoVetor;
    }

    public int getTamanhoVetor() {
        return tamanhoVetor;
    }
    // ----------------------------

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.000");
        return String.format(
            "Algoritmo: %s | Tamanho: %d | Cenário: %s | Tempo Médio: %s ms",
            nomeAlgoritmo,
            tamanhoVetor,
            tipoVetor,
            df.format(tempoMedioEmMilisegundos)
        );
    }

    public Integer getTamanhoEntrada() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTamanhoEntrada'");
    }

    public Double getTempoExecucao() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTempoExecucao'");
    }
}