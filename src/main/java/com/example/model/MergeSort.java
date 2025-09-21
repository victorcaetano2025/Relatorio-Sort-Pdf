// Arquivo: MergeSort.java
package com.example.model;

public class MergeSort implements SortAlgorithm {

    public MergeSort() {
        // Construtor
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void sort(int[] array) {
        // Apenas verificamos se o array precisa ser ordenado e chamamos o método principal
        if (array == null || array.length <= 1) {
            return;
        }
        mergeSort(array, 0, array.length - 1);
    }

    // Este é o método recursivo principal, agora privado e que trabalha com índices
    private void mergeSort(int[] array, int left, int right) {
        // Condição de parada: quando o subarray tem 1 ou 0 elementos
        if (left < right) {
            // Encontra o meio
            int middle = left + (right - left) / 2;

            // Ordena a metade da esquerda
            mergeSort(array, left, middle);
            // Ordena a metade da direita
            mergeSort(array, middle + 1, right);

            // Junta (merge) as duas metades já ordenadas
            merge(array, left, middle, right);
        }
    }

    // Este é o método que faz a "mágica" de juntar os pedaços
    private void merge(int[] array, int left, int middle, int right) {
        // Cria arrays temporários APENAS para a seção que estamos trabalhando
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Copia os dados para os arrays temporários
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[middle + 1 + j];
        }

        // --- Início do Merge ---
        int i = 0; // Índice inicial do primeiro subarray
        int j = 0; // Índice inicial do segundo subarray
        int k = left; // Índice inicial do array principal (que será modificado)

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Copia os elementos restantes do array da esquerda, se houver
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Copia os elementos restantes do array da direita, se houver
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}