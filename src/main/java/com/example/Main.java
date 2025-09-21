package com.example;
//import Sort.BubbleSort;

import com.example.model.Benchmark;
import com.example.model.QuickSort;
import com.example.model.ResultadoBenchmark;
import com.example.model.SortAlgorithm;
import com.example.model.TipoVetor;

public class Main {
    public static void main(String[] args) {

        // Em um método main para teste:
        SortAlgorithm quick = new QuickSort();
        ResultadoBenchmark resultado = Benchmark.executar(quick, TipoVetor.ALEATORIO_COM_REPETICAO, 50000);
        System.out.println(resultado);
        // Saída esperada: Algoritmo: Quick Sort | Tamanho: 50000 | ... | Tempo Médio: X
        // ms
    }
}
