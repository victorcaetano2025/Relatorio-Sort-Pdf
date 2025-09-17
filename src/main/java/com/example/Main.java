package com.example;
//import Sort.BubbleSort;

public class Main {
    public static void main(String[] args) {

        // cria o vetor 
        double[] resultados = {1.2222, 3.4567, 4.8910};
        Dados dado = new Dados("bubble", resultados);
        
        // Print the Resultado details (you can add getters for these details)
        System.out.println("nome do Sort: " + dado.getNome()); // printa o nome
        
        System.out.print("Resultados: ");
        for (double res : dado.getResultados()) {// printa os resultados
            System.out.print(res + " / ");
        }
    }
}
