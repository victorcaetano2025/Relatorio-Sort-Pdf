package com.example.Sort;

public class BubbleSort {

    public BubbleSort() {
        // Construtor da classe, pode ser vazio
    }

    // Método de ordenação utilizando Bubble Sort
    public void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    // Método para imprimir o vetor
    public void PrintarVetor(int[] vet) {
        for (int i : vet) {
            System.out.print(i + " / ");
        }
        System.out.println();  // Para pular uma linha após a impressão do vetor
    }
}
