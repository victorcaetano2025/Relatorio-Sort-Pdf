package com.example.util;

import com.example.model.ResultadoBenchmark;
import com.example.model.TipoVetor;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PdfGenerator {

    public static void gerarComIText(List<ResultadoBenchmark> resultados, Map<TipoVetor, File> chartFiles, Component parentComponent) {
        if (resultados == null || resultados.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Não há resultados para gerar o relatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Map<TipoVetor, List<ResultadoBenchmark>> resultadosAgrupados =
                resultados.stream().collect(Collectors.groupingBy(ResultadoBenchmark::getTipoVetor));

        String fileName = "Relatorio_Benchmark_iText.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            // Mudei para A4 retrato para a página de texto inicial
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(50, 50, 50, 50);

            // --- NOVO CÓDIGO: ADICIONA A PÁGINA DE ANÁLISE TEÓRICA ---
            adicionarPaginaDeAnalise(document);
            // --- FIM DO NOVO CÓDIGO ---

            DecimalFormat df = new DecimalFormat("#,##0.00");
            Iterator<Map.Entry<TipoVetor, List<ResultadoBenchmark>>> iterator = resultadosAgrupados.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<TipoVetor, List<ResultadoBenchmark>> entry = iterator.next();
                TipoVetor cenario = entry.getKey();
                List<ResultadoBenchmark> resultadosDoCenario = entry.getValue();

                // Adiciona quebra de página e muda para paisagem para os resultados
                document.add(new AreaBreak());
                pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

                // Adiciona o título
                document.add(new Paragraph("Relatório de Desempenho: " + cenario.toString())
                        .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

                // Adiciona a tabela
                List<Integer> tamanhos = resultadosDoCenario.stream().map(ResultadoBenchmark::getTamanhoVetor).distinct().sorted().collect(Collectors.toList());
                Map<String, List<ResultadoBenchmark>> resultadosPorAlgoritmo = resultadosDoCenario.stream().collect(Collectors.groupingBy(ResultadoBenchmark::getNomeAlgoritmo));
                Table table = new Table(UnitValue.createPercentArray(tamanhos.size() + 1)).useAllAvailableWidth();
                table.addHeaderCell(new Cell().add(new Paragraph("Algoritmo")).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
                for (Integer tamanho : tamanhos) {
                    table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(tamanho))).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
                }
                for (String nomeAlgoritmo : resultadosPorAlgoritmo.keySet()) {
                    table.addCell(nomeAlgoritmo);
                    for (Integer tamanho : tamanhos) {
                        String tempoFormatado = resultadosPorAlgoritmo.get(nomeAlgoritmo).stream().filter(r -> r.getTamanhoVetor() == tamanho).findFirst().map(r -> df.format(r.getTempoMedioEmMilisegundos()) + " ms").orElse("-");
                        table.addCell(new Cell().add(new Paragraph(tempoFormatado)).setTextAlignment(TextAlignment.RIGHT));
                    }
                }
                document.add(table);

                // Adiciona o gráfico
                if (chartFiles != null && chartFiles.containsKey(cenario)) {
                    File chartFile = chartFiles.get(cenario);
                    if (chartFile != null && chartFile.exists()) {
                        Image pdfImage = new Image(ImageDataFactory.create(chartFile.getAbsolutePath()));
                        pdfImage.setAutoScale(true); // << ALTERADO AQUI para a imagem caber na página
                        document.add(new Paragraph("\n"));
                        document.add(pdfImage);
                        chartFile.delete();
                    }
                }
            }

            document.close();
            JOptionPane.showMessageDialog(parentComponent, "Relatório '" + fileName + "' gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(fileName));

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Erro ao gerar PDF com iText: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- NOVO MÉTODO PARA GERAR A PÁGINA DE ANÁLISE ---
    private static void adicionarPaginaDeAnalise(Document document) throws IOException {
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        document.add(new Paragraph("Análise Teórica dos Algoritmos de Ordenação")
                .setFont(fontBold).setFontSize(18).setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

        // --- Algoritmos O(n²) ---
        document.add(new Paragraph("Algoritmos de Complexidade Quadrática - O(n²)")
                .setFont(fontBold).setFontSize(14).setMarginTop(15).setMarginBottom(10));
        document.add(new Paragraph("Estes algoritmos são mais simples, mas sua eficiência cai drasticamente com o aumento dos dados. Seus melhores casos, no entanto, podem ser notavelmente rápidos.")
                .setFont(fontNormal).setFontSize(10));

        addDescricaoAlgoritmo(document, "1. Bubble Sort (Ordenação por Bolha)",
                "Melhor Caso: O(n) — Ocorre quando a lista já está ordenada.",
                "Caso Médio: O(n²)",
                "Pior Caso: O(n²) — Ocorre quando a lista está em ordem inversa.");

        addDescricaoAlgoritmo(document, "2. Selection Sort (Ordenação por Seleção)",
                "Melhor Caso: O(n²)",
                "Caso Médio: O(n²)",
                "Pior Caso: O(n²) — O desempenho é consistente e não melhora para listas já ordenadas.");

        addDescricaoAlgoritmo(document, "3. Insertion Sort (Ordenação por Inserção)",
                "Melhor Caso: O(n) — Ocorre quando a lista já está ordenada.",
                "Caso Médio: O(n²)",
                "Pior Caso: O(n²) — Ocorre quando a lista está em ordem inversa.");

        // --- Algoritmos O(n log n) ---
        document.add(new Paragraph("Algoritmos de Complexidade Logarítmica Linear - O(n log n)")
                .setFont(fontBold).setFontSize(14).setMarginTop(20).setMarginBottom(10));
        document.add(new Paragraph("Esses algoritmos são muito mais eficientes e escaláveis para grandes volumes de dados. Seus piores e melhores casos costumam ser os mesmos, o que os torna previsíveis e confiáveis.")
                .setFont(fontNormal).setFontSize(10));

        addDescricaoAlgoritmo(document, "4. Merge Sort (Ordenação por Intercalação)",
                "Melhor Caso: O(n log n)",
                "Caso Médio: O(n log n)",
                "Pior Caso: O(n log n) — Um dos algoritmos mais estáveis em termos de desempenho.");

        addDescricaoAlgoritmo(document, "5. Quick Sort (Ordenação Rápida)",
                "Melhor Caso: O(n log n) — Ocorre quando o pivô escolhido divide a lista em duas metades de tamanho quase igual.",
                "Caso Médio: O(n log n)",
                "Pior Caso: O(n²) — Ocorre em raras situações de péssima escolha de pivô.");

        addDescricaoAlgoritmo(document, "6. Heap Sort (Ordenação por Monte)",
                "Melhor Caso: O(n log n)",
                "Caso Médio: O(n log n)",
                "Pior Caso: O(n log n) — Assim como o Merge Sort, seu desempenho é muito previsível.");
    }

    private static void addDescricaoAlgoritmo(Document doc, String titulo, String... itens) throws IOException {
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        doc.add(new Paragraph(titulo).setFont(fontBold).setFontSize(12).setMarginTop(10));
        for (String item : itens) {
            doc.add(new Paragraph(item).setFont(fontNormal).setFontSize(10).setMarginLeft(20));
        }
    }
}