package com.example.model;

// Implementando a nossa interface padrão
public class SelectionSort implements SortAlgorithm {

    public SelectionSort() {
        // Construtor
    }

    @Override
    public String getName() {
        return "Selection Sort";
    }

    // Renomeando para "sort" e adicionando @Override
    @Override
    public void sort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            // Encontra o índice do menor elemento a partir de i
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Troca o menor encontrado com o elemento da posição i
            if (minIndex != i) {
                int temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
            }
        }
    }

    // Método de impressão para testes
    public void printArray(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " / ");
        }
        System.out.println();
    }
}