package com.example.Sort;

public class MergeSort {

    public MergeSort() {
        // Construtor da classe, pode ser vazio
    }

    // Método público para iniciar o Merge Sort
    public void mergeSort(int[] arr) {
        if (arr.length > 1) {
            int meio = arr.length / 2;

            // Divide o array em duas metades
            int[] esquerda = new int[meio];
            int[] direita = new int[arr.length - meio];

            System.arraycopy(arr, 0, esquerda, 0, meio);
            System.arraycopy(arr, meio, direita, 0, arr.length - meio);

            // Ordena recursivamente as duas metades
            mergeSort(esquerda);
            mergeSort(direita);

            // Mescla as duas metades
            merge(arr, esquerda, direita);
        }
    }

    // Método auxiliar para mesclar dois subarrays
    private void merge(int[] arr, int[] esquerda, int[] direita) {
        int i = 0, j = 0, k = 0;

        // Compara elementos de esquerda e direita e insere no array principal
        while (i < esquerda.length && j < direita.length) {
            if (esquerda[i] <= direita[j]) {
                arr[k++] = esquerda[i++];
            } else {
                arr[k++] = direita[j++];
            }
        }

        // Copia os elementos restantes de esquerda (se houver)
        while (i < esquerda.length) {
            arr[k++] = esquerda[i++];
        }

        // Copia os elementos restantes de direita (se houver)
        while (j < direita.length) {
            arr[k++] = direita[j++];
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
