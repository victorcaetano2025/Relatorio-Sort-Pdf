package com.example.model;

public class Benchmark {

    private static final int NUMERO_DE_EXECUCOES = 3;

    /**
     * Executa um algoritmo de ordenação para um cenário e tamanho específicos,
     * mede o tempo, e retorna o resultado.
     *
     * @param algoritmo A instância do algoritmo a ser testado (ex: new QuickSort()).
     * @param tipoVetor O cenário do teste (ex: TipoVetor.ALEATORIO_SEM_REPETICAO).
     * @param tamanhoVetor O número de elementos do vetor.
     * @return Um objeto ResultadoBenchmark com todas as informações do teste.
     */
    public static ResultadoBenchmark executar(SortAlgorithm algoritmo, TipoVetor tipoVetor, int tamanhoVetor) {
        long tempoTotalNano = 0;

        // Executa o teste 3 vezes para tirar a média
        for (int i = 0; i < NUMERO_DE_EXECUCOES; i++) {
            // Gera um novo vetor a cada execução para garantir um teste justo
            int[] vetor = GeradorDeVetores.gerarVetor(tipoVetor, tamanhoVetor);

            long inicio = System.nanoTime();
            algoritmo.sort(vetor); // Executa a ordenação
            long fim = System.nanoTime();

            tempoTotalNano += (fim - inicio);
        }

        // Calcula a média em nanosegundos
        double tempoMedioNano = (double) tempoTotalNano / NUMERO_DE_EXECUCOES;
        // Converte para milissegundos para melhor leitura
        double tempoMedioMili = tempoMedioNano / 1_000_000.0;

        // Retorna um objeto com os resultados compilados
        return new ResultadoBenchmark(algoritmo.getName(), tipoVetor, tamanhoVetor, tempoMedioMili);
    }
}