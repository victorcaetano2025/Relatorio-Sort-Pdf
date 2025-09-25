package com.example.util;

import com.example.model.ResultadoBenchmark;
import com.example.model.TipoVetor;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
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

    // A assinatura do método foi atualizada para receber os arquivos dos gráficos
    public static void gerarComIText(List<ResultadoBenchmark> resultados, Map<TipoVetor, File> chartFiles, Component parentComponent) {
        if (resultados == null || resultados.isEmpty()) {
            // ... (código de aviso não muda)
            return;
        }

        Map<TipoVetor, List<ResultadoBenchmark>> resultadosAgrupados =
                resultados.stream().collect(Collectors.groupingBy(ResultadoBenchmark::getTipoVetor));

        String fileName = "Relatorio_Benchmark_iText.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4.rotate());
            document.setMargins(50, 50, 50, 50);

            DecimalFormat df = new DecimalFormat("#,##0.00");
            Iterator<Map.Entry<TipoVetor, List<ResultadoBenchmark>>> iterator = resultadosAgrupados.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<TipoVetor, List<ResultadoBenchmark>> entry = iterator.next();
                TipoVetor cenario = entry.getKey();
                List<ResultadoBenchmark> resultadosDoCenario = entry.getValue();

                // Adiciona o título
                document.add(new Paragraph("Relatório de Desempenho: " + cenario.toString())
                        .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

                // Adiciona a tabela (lógica da tabela não muda)
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

                // --- NOVO CÓDIGO PARA ADICIONAR O GRÁFICO ---
                if (chartFiles != null && chartFiles.containsKey(cenario)) {
                    File chartFile = chartFiles.get(cenario);
                    if (chartFile != null && chartFile.exists()) {
                        Image pdfImage = new Image(ImageDataFactory.create(chartFile.getAbsolutePath()));
                        pdfImage.setWidth(UnitValue.createPercentValue(100)); // Ocupa 100% da largura disponível
                        document.add(new Paragraph("\n")); // Adiciona um espaço
                        document.add(pdfImage);
                        chartFile.delete(); // Apaga o arquivo de imagem temporário após o uso
                    }
                }
                // --- FIM DO NOVO CÓDIGO ---

                if (iterator.hasNext()) {
                    document.add(new AreaBreak());
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
}