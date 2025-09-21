package com.example.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class GeradorDeVetores {

    private static final Random random = new Random();

    /**
     * Método principal que gera um vetor de um tipo e tamanho específicos.
     *
     * @param tipo O tipo de vetor a ser gerado, vindo do enum TipoVetor.
     * @param tamanho O número de elementos que o vetor deve ter.
     * @return Um array de inteiros (int[]) configurado conforme solicitado.
     */
    public static int[] gerarVetor(TipoVetor tipo, int tamanho) {
        switch (tipo) {
            case ALEATORIO_COM_REPETICAO:
                return gerarAleatorioComRepeticao(tamanho);
            case CRESCENTE_COM_REPETICAO:
                return gerarCrescenteComRepeticao(tamanho);
            case DECRESCENTE_COM_REPETICAO:
                return gerarDecrescenteComRepeticao(tamanho);
            case ALEATORIO_SEM_REPETICAO:
                return gerarAleatorioSemRepeticao(tamanho);
            case CRESCENTE_SEM_REPETICAO:
                return gerarCrescenteSemRepeticao(tamanho);
            case DECRESCENTE_SEM_REPETICAO:
                return gerarDecrescenteSemRepeticao(tamanho);
            default:
                // Retorna um array vazio se o tipo for desconhecido
                return new int[0];
        }
    }

    // --- Métodos Privados para cada cenário ---

    private static int[] gerarAleatorioComRepeticao(int tamanho) {
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            // Gera números aleatórios num intervalo razoável
            array[i] = random.nextInt(tamanho * 5);
        }
        return array;
    }

    private static int[] gerarCrescenteComRepeticao(int tamanho) {
        int[] array = gerarAleatorioComRepeticao(tamanho);
        Arrays.sort(array); // Ordena para ficar crescente
        return array;
    }

    private static int[] gerarDecrescenteComRepeticao(int tamanho) {
        int[] array = gerarAleatorioComRepeticao(tamanho);
        Arrays.sort(array);
        // Inverte o array para ficar decrescente
        for (int i = 0; i < tamanho / 2; i++) {
            int temp = array[i];
            array[i] = array[tamanho - 1 - i];
            array[tamanho - 1 - i] = temp;
        }
        return array;
    }

    private static int[] gerarAleatorioSemRepeticao(int tamanho) {
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) {
            lista.add(i); // Adiciona números sequenciais
        }
        Collections.shuffle(lista); // Embaralha a lista
        // Converte a lista de volta para um array de int
        return lista.stream().mapToInt(i -> i).toArray();
    }

    private static int[] gerarCrescenteSemRepeticao(int tamanho) {
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = i;
        }
        return array;
    }



    private static int[] gerarDecrescenteSemRepeticao(int tamanho) {
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = tamanho - 1 - i;
        }
        return array;
    }
}