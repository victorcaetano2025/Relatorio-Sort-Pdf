package com.example.util;

import com.example.model.ResultadoBenchmark;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChartGenerator {

    /**
     * Cria um gráfico de barras a partir de uma lista de resultados e o salva como um arquivo PNG.
     *
     * @param chartTitle Título do gráfico (ex: "Aleatório com Repetição").
     * @param results    A lista de resultados de benchmark para este cenário.
     * @return Um objeto File apontando para a imagem PNG gerada.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static File createBarChart(String chartTitle, List<ResultadoBenchmark> results) throws IOException {
        // 1. Cria o conjunto de dados para o gráfico de barras
        DefaultCategoryDataset dataset = createDataset(results);

        // 2. Cria o objeto do gráfico
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,                // Título do Gráfico
                "Tamanho do Vetor",        // Label do Eixo X (Categorias)
                "Tempo Médio (ms)",        // Label do Eixo Y (Valores)
                dataset,                   // Dados
                PlotOrientation.VERTICAL,  // Orientação do gráfico
                true,                      // Incluir legenda?
                true,                      // Gerar tooltips?
                false                      // Gerar URLs?
        );

        // 3. (Opcional) Customiza a aparência do gráfico para ficar mais bonito
        customizeChart(barChart);

        // 4. Salva o gráfico como um arquivo PNG
        // Sanitiza o nome do arquivo para evitar caracteres inválidos
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + ".png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, barChart, 1200, 800); // Largura x Altura da imagem

        System.out.println("Gráfico gerado: " + chartFile.getAbsolutePath());
        return chartFile;
    }

    /**
     * Cria e popula o conjunto de dados que será usado pelo JFreeChart.
     * @param results A lista de resultados a serem exibidos.
     * @return um DefaultCategoryDataset populado.
     */
    private static DefaultCategoryDataset createDataset(List<ResultadoBenchmark> results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (ResultadoBenchmark res : results) {
            // Valor: o tempo em milissegundos
            Number value = res.getTempoMedioEmMilisegundos();
            // Série (Legenda): o nome do algoritmo
            String series = res.getNomeAlgoritmo();
            // Categoria (Eixo X): o tamanho do vetor
            String category = String.valueOf(res.getTamanhoVetor());

            dataset.addValue(value, series, category);
        }

        return dataset;
    }

    /**
     * Aplica customizações visuais ao gráfico gerado.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    private static void customizeChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        
        // Cor de fundo da área do gráfico
        plot.setBackgroundPaint(Color.WHITE);
        // Cor das linhas de grade
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Customiza as barras
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false); // Remove a borda preta das barras
        
        // Define um gradiente de cores para as barras (opcional, mas elegante)
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.BLUE, 0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.GREEN, 0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.RED, 0.0f, 0.0f, new Color(64, 0, 0));
        GradientPaint gp3 = new GradientPaint(0.0f, 0.0f, Color.ORANGE, 0.0f, 0.0f, new Color(64, 64, 0));
        GradientPaint gp4 = new GradientPaint(0.0f, 0.0f, Color.CYAN, 0.0f, 0.0f, new Color(0, 64, 64));
        GradientPaint gp5 = new GradientPaint(0.0f, 0.0f, Color.MAGENTA, 0.0f, 0.0f, new Color(64, 0, 64));
        
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        renderer.setSeriesPaint(4, gp4);
        renderer.setSeriesPaint(5, gp5);
    }
}