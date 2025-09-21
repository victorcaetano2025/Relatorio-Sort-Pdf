// Arquivo: InsertionSort.java
package com.example.model;

// Implementando a mesma interface que o BubbleSort
public class InsertionSort implements SortAlgorithm {

    public InsertionSort() {
        // O construtor continua o mesmo
    }

    @Override
    public String getName() {
        return "Insertion Sort";
    }
    // Removemos o "static" e adicionamos @Override
    @Override
    public void sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int key = array[i]; // Mudei "chave" para "key" por convenção
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    // O método de impressão também deixa de ser estático
    public void printArray(int[] array) {
        for (int num : array) {
            System.out.print(num + " / "); // Adicionei um separador para clareza
        }
        System.out.println();
    }
}