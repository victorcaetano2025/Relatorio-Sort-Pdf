package com.example.Sort;

public class InsertionSort {

        //Construtuor opicional para métodos estáticos

    public InsertionSort() {

        //contrutor da classe pode ser vazio
    }
public static void sort(int[] array) {

    for (int i = 1; i < array.length; i++) {
        int chave = array[i];
        int j = i - 1;

        while (j >= 0 && array[j] > chave) {
            array[j + 1] = array[j];
            j--;
        }

        array[j + 1] = chave;

    }

   }

   // Método para imprimir a array
   public static void printArray(int[] array) {

    for(int num : array) {
        System.out.print(num+"");
    }

    System.out.println(); //Aqui pula uma linha após imprimir a array

   }
   
}