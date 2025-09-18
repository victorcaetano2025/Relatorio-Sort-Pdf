package com.example.Sort;

public class SelectionSort {

    public SelectionSort() {
        // Construtor da classe, pode ser vazio
    }

    // Método de ordenação utilizando Selection Sort
    public void selectionSort(int[] arr) {
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

    // Método para imprimir o vetor
    public void PrintarVetor(int[] vet) {
        for (int i : vet) {
            System.out.print(i + " / ");
        }
        System.out.println(); // Pula linha após imprimir o vetor
    }
}
