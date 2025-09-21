package com.example.model;

import java.util.Random; // Importe a classe Random

public class QuickSort implements SortAlgorithm {

    // Adicionamos um gerador de números aleatórios
    private final Random random = new Random();

    public QuickSort() {
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public void sort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        // --- INÍCIO DA MUDANÇA ---
        // Escolhe um índice de pivô aleatório entre low e high
        int pivotIndex = low + random.nextInt(high - low + 1);
        
        // Troca o pivô aleatório com o último elemento (high)
        // para que possamos usar a mesma lógica de partição de antes.
        int tempPivot = arr[pivotIndex];
        arr[pivotIndex] = arr[high];
        arr[high] = tempPivot;
        // --- FIM DA MUDANÇA ---

        // O resto do método continua exatamente igual
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}