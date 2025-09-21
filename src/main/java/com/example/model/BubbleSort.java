// Arquivo: BubbleSort.java
package com.example.model;

// A classe agora implementa a nossa interface padrão
public class BubbleSort implements SortAlgorithm {

    // O construtor continua igual
    public BubbleSort() {
    }

    // ... dentro da classe BubbleSort
    @Override
    public String getName() {
        return "Bubble Sort";
    }

    // Usamos @Override para indicar que este método vem da interface.
    // O nome do método é padronizado para "sort".
    @Override
    public void sort(int[] arr) {
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

    // Este método é ótimo para testes, mas podemos mantê-lo separado
    // da lógica principal de ordenação.
    public void printArray(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " / ");
        }
        System.out.println();
    }
}