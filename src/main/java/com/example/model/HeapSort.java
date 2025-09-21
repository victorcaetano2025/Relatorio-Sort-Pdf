package com.example.model;

public class HeapSort implements SortAlgorithm {

    public HeapSort() {
        // Construtor
    }

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        // --- Fase 1: Construir o Max-Heap ---
        // Começamos do último nó que não é uma folha (n / 2 - 1) e vamos até a raiz.
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // --- Fase 2: Extrair elementos do heap um por um para ordenar ---
        for (int i = n - 1; i > 0; i--) {
            // Move a raiz atual (o maior elemento) para o fim do vetor
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Chama o heapify na heap reduzida para garantir que a nova raiz seja o maior elemento
            heapify(array, i, 0);
        }
    }

    /**
     * O método principal que garante a propriedade do Max-Heap para uma subárvore.
     * @param array O vetor.
     * @param n O tamanho da heap (pode ser menor que o tamanho do array durante a ordenação).
     * @param i O índice do nó raiz da subárvore a ser verificada (heapified).
     */
    private void heapify(int[] array, int n, int i) {
        int largest = i;       // Inicializa o maior como a raiz
        int left = 2 * i + 1;  // Índice do filho da esquerda
        int right = 2 * i + 2; // Índice do filho da direita

        // Se o filho da esquerda for maior que a raiz
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        // Se o filho da direita for maior que o maior até agora
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        // Se o maior não for a raiz
        if (largest != i) {
            // Troca a raiz com o maior
            int swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            // Recursivamente chama heapify para a subárvore afetada
            heapify(array, n, largest);
        }
    }
}