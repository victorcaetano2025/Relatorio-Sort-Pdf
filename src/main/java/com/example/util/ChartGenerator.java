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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.util.Rotation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartGenerator {

    // Paleta de cores sólida para os gráficos de barra e linha
    private static final Color[] CHART_COLORS = {
            new Color(79, 129, 189),    // Azul
            new Color(155, 89, 182),    // Roxo
            new Color(241, 196, 15),    // Amarelo
            new Color(231, 76, 60),     // Vermelho
            new Color(46, 204, 113),    // Verde
            new Color(230, 126, 34)     // Laranja
    };

    /**
     * Cria um gráfico de barras a partir de uma lista de resultados e o salva como um arquivo PNG.
     * @param chartTitle Título do gráfico.
     * @param results A lista de resultados de benchmark.
     * @return Um objeto File apontando para a imagem PNG gerada.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static File createBarChart(String chartTitle, List<ResultadoBenchmark> results) throws IOException {
        DefaultCategoryDataset dataset = createCategoryDataset(results);
        // Alteração para criar um gráfico de barras 3D
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Tamanho do Vetor",
                "Tempo Médio (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        customizeBarChart(barChart);
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + "_bar.png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, barChart, 1200, 800);
        return chartFile;
    }

    /**
     * Cria um gráfico de linhas a partir de uma lista de resultados e o salva como um arquivo PNG.
     * @param chartTitle Título do gráfico.
     * @param results A lista de resultados de benchmark.
     * @return Um objeto File apontando para a imagem PNG gerada.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static File createLineChart(String chartTitle, List<ResultadoBenchmark> results) throws IOException {
        DefaultCategoryDataset dataset = createCategoryDataset(results);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Tamanho do Vetor",
                "Tempo Médio (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        customizeLineChart(lineChart);
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + "_line.png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, lineChart, 1200, 800);
        return chartFile;
    }

    /**
     * Cria um gráfico de pizza 3D a partir de uma lista de resultados e o salva como um arquivo PNG.
     * @param chartTitle Título do gráfico.
     * @param results A lista de resultados de benchmark.
     * @return Um objeto File apontando para a imagem PNG gerada.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static File createPieChart3D(String chartTitle, List<ResultadoBenchmark> results) throws IOException {
        DefaultPieDataset<String> dataset = createPieDataset(results);
        JFreeChart pieChart = ChartFactory.createPieChart(
                chartTitle,
                dataset,
                true,
                true,
                false
        );
        customizePieChart3D(pieChart);
        String fileName = chartTitle.replaceAll("[^a-zA-Z0-9.-]", "_") + "_pie3d.png";
        File chartFile = new File(fileName);
        ChartUtils.saveChartAsPNG(chartFile, pieChart, 1200, 800);
        return chartFile;
    }

    /**
     * Cria e popula o conjunto de dados para gráficos de barras e linhas.
     * @param results A lista de resultados a serem exibidos.
     * @return um DefaultCategoryDataset populado.
     */
    private static DefaultCategoryDataset createCategoryDataset(List<ResultadoBenchmark> results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ResultadoBenchmark res : results) {
            Number value = res.getTempoMedioEmMilisegundos();
            String series = res.getNomeAlgoritmo();
            String category = String.valueOf(res.getTamanhoVetor());
            dataset.addValue(value, series, category);
        }
        return dataset;
    }

    /**
     * Cria e popula o conjunto de dados para gráficos de pizza.
     * @param results A lista de resultados a serem exibidos.
     * @return um DefaultPieDataset populado.
     */
    private static DefaultPieDataset<String> createPieDataset(List<ResultadoBenchmark> results) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Map<String, Double> temposPorAlgoritmo = results.stream()
                .collect(Collectors.groupingBy(
                        ResultadoBenchmark::getNomeAlgoritmo,
                        Collectors.averagingDouble(ResultadoBenchmark::getTempoMedioEmMilisegundos)
                ));
        for (Map.Entry<String, Double> entry : temposPorAlgoritmo.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return dataset;
    }

    /**
     * Aplica customizações visuais ao gráfico de barras.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    private static void customizeBarChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();

        // Alterações de cores para fundo preto e textos brancos
        chart.setBackgroundPaint(Color.BLACK);
        plot.setBackgroundPaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);

        // Customização de fontes e cores
        chart.getTitle().setPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));
        chart.getLegend().setBackgroundPaint(Color.BLACK); // Fundo da legenda para preto
        chart.getLegend().setItemPaint(Color.WHITE); // Cor do texto da legenda para branco
        chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 16));

        // Customização dos eixos
        plot.getDomainAxis().setLabelPaint(Color.WHITE);
        plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);
        plot.getRangeAxis().setTickLabelPaint(Color.WHITE);
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        for (int i = 0; i < CHART_COLORS.length; i++) {
            renderer.setSeriesPaint(i, CHART_COLORS[i]);
        }
        
        // Remove a linha branca das barras
        renderer.setDrawBarOutline(false);

        // Adiciona e formata os números acima das barras
        renderer.setDefaultItemLabelsVisible(false); // << ALTERADO AQUI para não mostrar os números
    }

    /**
     * Aplica customizações visuais ao gráfico de linhas.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    private static void customizeLineChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        for (int i = 0; i < CHART_COLORS.length; i++) {
            renderer.setSeriesPaint(i, CHART_COLORS[i]);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
            renderer.setSeriesShape(i, new java.awt.geom.Ellipse2D.Double(-4, -4, 8, 8));
        }
    }

    /**
     * Aplica customizações visuais ao gráfico de pizza 3D.
     * @param chart O objeto JFreeChart a ser customizado.
     */
    @SuppressWarnings("unchecked")
    private static void customizePieChart3D(JFreeChart chart) {
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.9f);

        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
                "{0}: {2}",
                NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")
        );
        plot.setLabelGenerator(generator);
        plot.setLabelFont(new Font("Arial", Font.BOLD, 20));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 180));
        plot.setLabelOutlinePaint(Color.BLACK);
        plot.setLabelOutlineStroke(new BasicStroke(0.5f));
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        plot.setSectionOutlinesVisible(false);
    }
}