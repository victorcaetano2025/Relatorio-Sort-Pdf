package com.example;
 
public class Dados {
    private String nome;
    private double[] Resultados;

    // Construtor da classe
    public Dados(String nome, double[] Resultados) {
        this.nome = nome;
        this.Resultados = Resultados;
    }

    // Método para imprimir os dados (array de resultados)
    public void PrintarDados() {
        for (double res : Resultados) {
            System.out.print(res + " / ");
        }
    }

    // Métodos Getter e Setter para nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Métodos Getter e Setter para resultados
    public double[] getResultados() {
        return Resultados;
    }

    public void setResultados(double[] resultados) {
        this.Resultados = resultados;
    }
}
