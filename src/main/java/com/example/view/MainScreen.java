package com.example.view;

import com.example.model.*;
import com.example.util.ChartGenerator;
import com.example.util.PdfGenerator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Classe auxiliar para passar múltiplos resultados do SwingWorker
class WorkerResult {
    final List<ResultadoBenchmark> benchmarkResults;
    final Map<TipoVetor, File> chartFiles;
    WorkerResult(List<ResultadoBenchmark> benchmarkResults, Map<TipoVetor, File> chartFiles) {
        this.benchmarkResults = benchmarkResults;
        this.chartFiles = chartFiles;
    }
}

public class MainScreen extends JFrame {
    private Map<JCheckBox, SortAlgorithm> algorithmMap = new LinkedHashMap<>();
    private Map<JCheckBox, Integer> sizeMap = new LinkedHashMap<>();
    private JCheckBox chkOutputTabela = new JCheckBox("Tabela de Tempos", true);
    private JCheckBox chkOutputGrafico = new JCheckBox("Gráfico Comparativo");
    private JButton btnGerarRelatorio = new JButton("Gerar Relatório");
    private JProgressBar progressBar = new JProgressBar();

    public MainScreen() {
        setTitle("Gerador de Relatórios de Algoritmos");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initializeMaps();
        add(createAlgorithmPanel(), BorderLayout.WEST);
        add(createSettingsPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
        btnGerarRelatorio.addActionListener(e -> onGerarRelatorioClick());
    }

    private void initializeMaps() {
        algorithmMap.put(new JCheckBox("QuickSort"), new QuickSort());
        algorithmMap.put(new JCheckBox("MergeSort"), new MergeSort());
        algorithmMap.put(new JCheckBox("HeapSort"), new HeapSort());
        algorithmMap.put(new JCheckBox("InsertionSort"), new InsertionSort());
        algorithmMap.put(new JCheckBox("SelectionSort"), new SelectionSort());
        algorithmMap.put(new JCheckBox("BubbleSort"), new BubbleSort());
        sizeMap.put(new JCheckBox("100.000"), 100000);
        sizeMap.put(new JCheckBox("160.000"), 160000);
        sizeMap.put(new JCheckBox("220.000"), 220000);
        sizeMap.put(new JCheckBox("280.000"), 280000);
        sizeMap.put(new JCheckBox("340.000"), 340000);
        sizeMap.put(new JCheckBox("400.000"), 400000);
        sizeMap.put(new JCheckBox("520.000"), 520000);
        sizeMap.put(new JCheckBox("640.000"), 640000);
        sizeMap.put(new JCheckBox("700.000"), 700000);
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel pnlTamanhosInterno = new JPanel();
        pnlTamanhosInterno.setLayout(new BoxLayout(pnlTamanhosInterno, BoxLayout.Y_AXIS));
        for (JCheckBox checkBox : sizeMap.keySet()) {
            pnlTamanhosInterno.add(checkBox);
        }
        JScrollPane scrollPaneTamanhos = new JScrollPane(pnlTamanhosInterno);
        scrollPaneTamanhos.setBorder(new TitledBorder("Quantidade de Elementos"));
        scrollPaneTamanhos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneTamanhos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel pnlSaida = new JPanel();
        pnlSaida.setLayout(new BoxLayout(pnlSaida, BoxLayout.Y_AXIS));
        pnlSaida.setBorder(new TitledBorder("Incluir no Relatório"));
        pnlSaida.add(chkOutputTabela);
        pnlSaida.add(chkOutputGrafico);
        panel.add(scrollPaneTamanhos);
        panel.add(pnlSaida);
        return panel;
    }

    private JPanel createAlgorithmPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Algoritmos"));
        for (JCheckBox checkBox : algorithmMap.keySet()) {
            panel.add(checkBox);
        }
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        btnGerarRelatorio.setFont(new Font("Arial", Font.BOLD, 16));
        btnGerarRelatorio.setBackground(new Color(60, 179, 113));
        btnGerarRelatorio.setForeground(Color.WHITE);
        progressBar.setStringPainted(true);
        progressBar.setString("Pronto para começar");
        panel.add(btnGerarRelatorio, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        return panel;
    }

    private void onGerarRelatorioClick() {
        List<SortAlgorithm> selectedAlgorithms = algorithmMap.entrySet()
                .stream()
                .filter(e -> e.getKey().isSelected())
                .map(Map.Entry::getValue)
                .toList();

        List<Integer> selectedSizes = sizeMap.entrySet()
                .stream()
                .filter(e -> e.getKey().isSelected())
                .map(Map.Entry::getValue)
                .toList();

        final boolean isChartSelected = chkOutputGrafico.isSelected();
        final boolean isTableSelected = chkOutputTabela.isSelected();

        if (selectedAlgorithms.isEmpty() || selectedSizes.isEmpty() || (!isChartSelected && !isTableSelected)) {
            JOptionPane.showMessageDialog(this,
                    "Selecione pelo menos um algoritmo, um tamanho e um formato de saída (Tabela e/ou Gráfico).",
                    "Seleção Inválida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnGerarRelatorio.setEnabled(false);
        progressBar.setIndeterminate(true);
        progressBar.setString("Executando benchmarks...");

        SwingWorker<WorkerResult, Void> worker = new SwingWorker<>() {
            @Override
            protected WorkerResult doInBackground() throws Exception {
                List<ResultadoBenchmark> allResults = new ArrayList<>();
                for (SortAlgorithm algoritmo : selectedAlgorithms) {
                    for (Integer tamanho : selectedSizes) {
                        for (TipoVetor tipo : TipoVetor.values()) {
                            System.out.println("Testando: " + algoritmo.getName() + " com " + tamanho + " (" + tipo + ")");
                            allResults.add(Benchmark.executar(algoritmo, tipo, tamanho));
                        }
                    }
                }

                Map<TipoVetor, File> chartFiles = new HashMap<>();
                if (isChartSelected) {
                    progressBar.setString("Gerando gráficos...");
                    Map<TipoVetor, List<ResultadoBenchmark>> resultadosAgrupados =
                            allResults.stream().collect(Collectors.groupingBy(ResultadoBenchmark::getTipoVetor));

                    for (Map.Entry<TipoVetor, List<ResultadoBenchmark>> entry : resultadosAgrupados.entrySet()) {
                        // Aqui cada gráfico vai mostrar todos os algoritmos lado a lado
                        File chartFile = ChartGenerator.createBarChart(entry.getKey().toString(), entry.getValue());
                        chartFiles.put(entry.getKey(), chartFile);
                    }
                }

                return new WorkerResult(allResults, chartFiles);
            }

            @Override
            protected void done() {
                try {
                    WorkerResult result = get();
                    progressBar.setString("Gerando PDF...");
                    PdfGenerator.gerarComIText(result.benchmarkResults, result.chartFiles, MainScreen.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainScreen.this,
                            "Ocorreu um erro: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    btnGerarRelatorio.setEnabled(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setString("Concluído! Verifique o PDF.");
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> new MainScreen().setVisible(true));
    }
}
