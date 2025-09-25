package com.example.util;

import com.example.model.ResultadoBenchmark;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer; // Adicionado para gráfico de linhas
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.util.Rotation;

import java.awt.BasicStroke; // Adicionado para customização de linhas e bordas de rótulos
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat; // Adicionado para formatação de porcentagem
import java.text.NumberFormat; // Adicionado para formatação de porcentagem
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                chartTitle,                  // Título do Gráfico
                "Tamanho do Vetor",          // Label do Eixo X (Categorias)
                "Tempo Médio (ms)",          // Label do Eixo Y (Valores)
                dataset,                     // Dados
                PlotOrientation.VERTICAL,    // Orientação do gráfico
                true,                        // Incluir legenda?
                true,                        // Gerar tooltips?
                false                        // Gerar URLs?
        );

        // 3. (Opcional) Customiza a aparência do gráfico para ficar mais bonito
        customizeBarChart(barChart);

        // 4. Salva o gráfico como um arquivo PNG
        // Sanitiza o nome do arquivo para evitar caracteres inválidos
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + "_bar.png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, barChart, 1200, 800); // Largura x Altura da imagem

        System.out.println("Gráfico de barras gerado: " + chartFile.getAbsolutePath());
        return chartFile;
    }

    /**
     * Cria um gráfico de linhas a partir de uma lista de resultados e o salva como um arquivo PNG.
     *
     * @param chartTitle Título do gráfico (ex: "Comparação de Performance dos Algoritmos").
     * @param results    A lista de resultados de benchmark para este cenário.
     * @return Um objeto File apontando para a imagem PNG gerada.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static File createLineChart(String chartTitle, List<ResultadoBenchmark> results) throws IOException {
        // 1. Cria o conjunto de dados (o mesmo usado para o gráfico de barras)
        DefaultCategoryDataset dataset = createDataset(results);

        // 2. Cria o objeto do gráfico de linhas
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,                 // Título do Gráfico
                "Tamanho do Vetor",         // Label do Eixo X (Categorias)
                "Tempo Médio (ms)",         // Label do Eixo Y (Valores)
                dataset,                    // Dados
                PlotOrientation.VERTICAL,   // Orientação do gráfico
                true,                       // Incluir legenda?
                true,                       // Gerar tooltips?
                false                       // Gerar URLs?
        );

        // 3. (Opcional) Customiza a aparência do gráfico
        customizeLineChart(lineChart);

        // 4. Salva o gráfico como um arquivo PNG
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + "_line.png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, lineChart, 1200, 800); // Largura x Altura da imagem

        System.out.println("Gráfico de linhas gerado: " + chartFile.getAbsolutePath());
        return chartFile;
    }

    /**
     * Cria um gráfico de pizza 3D a partir de uma lista de resultados e o salva como um arquivo PNG.
     *
     * @param chartTitle Título do gráfico (ex: "Distribuição de Performance - Aleatório").
     * @param results    A lista de resultados de benchmark para este cenário.
     * @return Um objeto File apontando para a imagem PNG gerada.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static File createPieChart3D(String chartTitle, List<ResultadoBenchmark> results) throws IOException {
        // 1. Cria o conjunto de dados para o gráfico de pizza
        DefaultPieDataset<String> dataset = createPieDataset(results);

        // 2. Cria o objeto do gráfico de pizza (2D, pois 3D está depreciado)
        JFreeChart pieChart = ChartFactory.createPieChart(
                chartTitle,                  // Título do Gráfico
                dataset,                     // Dados
                true,                        // Incluir legenda?
                true,                        // Gerar tooltips?
                false                        // Gerar URLs?
        );

        // 3. Customiza a aparência do gráfico para ficar mais bonito
        customizePieChart3D(pieChart);

        // 4. Salva o gráfico como um arquivo PNG
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + "_pie3d.png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, pieChart, 1200, 800); // Largura x Altura da imagem

        System.out.println("Gráfico de pizza 3D gerado: " + chartFile.getAbsolutePath());
        return chartFile;
    }

    /**
     * Cria e popula o conjunto de dados que será usado pelo JFreeChart para gráficos de barras e linhas.
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
     * Cria e popula o conjunto de dados que será usado pelo JFreeChart para gráficos de pizza.
     * @param results A lista de resultados a serem exibidos.
     * @return um DefaultPieDataset populado.
     */
    private static DefaultPieDataset<String> createPieDataset(List<ResultadoBenchmark> results) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Agrupa os resultados por algoritmo e calcula a média dos tempos
        Map<String, Double> temposPorAlgoritmo = results.stream()
                .collect(Collectors.groupingBy(
                        ResultadoBenchmark::getNomeAlgoritmo,
                        Collectors.averagingDouble(ResultadoBenchmark::getTempoMedioEmMilisegundos)
                ));

        // Adiciona os dados ao dataset
        for (Map.Entry<String, Double> entry : temposPorAlgoritmo.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }

    /**
     * Aplica customizações visuais ao gráfico de barras gerado.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    private static void customizeBarChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        
        // Cor de fundo da área do gráfico
        plot.setBackgroundPaint(Color.WHITE);

        // Cor das linhas de grade
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Customiza as barras do gráfico
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
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

    /**
     * Aplica customizações visuais ao gráfico de linhas gerado.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    private static void customizeLineChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        
        // Cor de fundo e grade (semelhante ao bar chart)
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Customização da linha e dos pontos (renderer)
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        
        // Ativa a exibição dos pontos (shapes) em cima das linhas
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultLinesVisible(true);
        renderer.setDrawOutlines(true); // Desenha a borda dos pontos
        renderer.setUseFillPaint(true);
        
        // Altera as cores das séries para diferenciar os algoritmos
        renderer.setSeriesPaint(0, new Color(19, 137, 190)); // Azul
        renderer.setSeriesPaint(1, new Color(255, 99, 132)); // Vermelho/Rosa
        renderer.setSeriesPaint(2, new Color(54, 162, 235)); // Ciano/Azul claro
        renderer.setSeriesPaint(3, new Color(255, 159, 64)); // Laranja
        renderer.setSeriesPaint(4, new Color(75, 192, 192)); // Verde-água
        renderer.setSeriesPaint(5, new Color(153, 102, 255)); // Roxo
        
        // Torna os pontos maiores e as linhas mais grossas
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-4, -4, 8, 8)); // Define o formato do ponto
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); 
        renderer.setSeriesStroke(1, new BasicStroke(2.0f)); 
        renderer.setSeriesStroke(2, new BasicStroke(2.0f)); 
        renderer.setSeriesStroke(3, new BasicStroke(2.0f)); 
        renderer.setSeriesStroke(4, new BasicStroke(2.0f)); 
        renderer.setSeriesStroke(5, new BasicStroke(2.0f)); 
    }

    /**
     * Aplica customizações visuais ao gráfico de pizza 3D gerado, incluindo rótulos de porcentagem.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    @SuppressWarnings("unchecked")
    private static void customizePieChart3D(JFreeChart chart) {
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        
        // Configurações de rotação e transparência
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.9f); // Aumenta um pouco a opacidade
        
        // *** MODIFICAÇÃO CHAVE: Define o gerador de rótulo para mostrar a porcentagem ***
        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
            "{0}: {2}", // Formato: NomeAlgoritmo: X%
            NumberFormat.getNumberInstance(), 
            new DecimalFormat("0.00%") // Formata o percentual com duas casas decimais e o símbolo %
        );
        plot.setLabelGenerator(generator);
        
        // Cores personalizadas para diferentes algoritmos
        plot.setSectionPaint("QuickSort", new Color(255, 99, 132));
        plot.setSectionPaint("MergeSort", new Color(54, 162, 235));
        plot.setSectionPaint("HeapSort", new Color(255, 205, 86));
        plot.setSectionPaint("BubbleSort", new Color(75, 192, 192));
        plot.setSectionPaint("SelectionSort", new Color(153, 102, 255));
        plot.setSectionPaint("InsertionSort", new Color(255, 159, 64));
        
        // Fonte dos labels
        plot.setLabelFont(new Font("Arial", Font.BOLD, 20));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 180)); // Fundo semi-transparente
        plot.setLabelOutlinePaint(Color.BLACK);
        plot.setLabelOutlineStroke(new BasicStroke(0.5f)); // Adiciona uma pequena borda
        
        // Título
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        
        // Remove bordas das seções para um visual mais limpo
        plot.setSectionOutlinesVisible(false);
    }
}