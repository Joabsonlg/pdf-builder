package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.text.Paragraph;
import io.github.joabsonlg.pdfbuilder.components.text.TextAlignment;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;

/**
 * Demonstração dos diferentes tipos de alinhamento de parágrafos e formatação rica.
 */
public class ParagraphDemo {
    private static final Logger logger = LoggerFactory.getLogger(ParagraphDemo.class);

    public static void main(String[] args) {
        try {
            // Configuração da área segura com header e footer
            SafeArea safeArea = SafeArea.builder()
                    .withMargins(50f, 40f, 30f, 40f)
                    .withHeader(true)
                    .withFooter(true)
                    .build();

            // Configuração do documento
            PDFConfiguration config = PDFConfiguration.create()
                    .withPageSize(PDRectangle.A4)
                    .withSafeArea(safeArea)
                    .withDPI(300)
                    .withCompressionQuality(0.8f)
                    .withFontSize(12f)
                    .withLineSpacing(14f)
                    .build();

            PDFBuilder builder = new PDFBuilder(config);

            // 2. Estilos de texto
            TextStyle normalStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                    .withFontSize(12f)
                    .build();

            TextStyle boldStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(12f)
                    .build();

            TextStyle italicStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE))
                    .withFontSize(12f)
                    .build();

            TextStyle underlineStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                    .withFontSize(12f)
                    .withUnderline(true)
                    .build();

            TextStyle colorStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                    .withFontSize(12f)
                    .withColor(Color.BLUE)
                    .build();

            // 3. Parágrafos com diferentes alinhamentos e estilos
            Paragraph leftAligned = Paragraph.builder()
                    .addStyledText("Alinhamento à Esquerda: ", boldStyle)
                    .addStyledText("Este é um exemplo de texto com ", normalStyle)
                    .addStyledText("diferentes estilos ", italicStyle)
                    .addStyledText("e formatações. ", underlineStyle)
                    .addStyledText("Observe como podemos misturar tudo!", colorStyle)
                    .withAlignment(TextAlignment.LEFT)
                    .build();

            Paragraph centered = Paragraph.builder()
                    .addStyledText("Alinhamento Centralizado: ", boldStyle)
                    .addStyledText("Este texto está centralizado e possui ", normalStyle)
                    .addStyledText("palavras em destaque ", colorStyle)
                    .addStyledText("para demonstrar a flexibilidade ", italicStyle)
                    .addStyledText("do componente.", underlineStyle)
                    .withAlignment(TextAlignment.CENTER)
                    .build();

            Paragraph rightAligned = Paragraph.builder()
                    .addStyledText("Alinhamento à Direita: ", boldStyle)
                    .addStyledText("Note como o texto está alinhado à direita. ", normalStyle)
                    .addStyledText("Podemos usar cores ", colorStyle)
                    .addStyledText("e sublinhado ", underlineStyle)
                    .addStyledText("livremente!", italicStyle)
                    .withAlignment(TextAlignment.RIGHT)
                    .build();

            Paragraph justified = Paragraph.builder()
                    .addStyledText("Alinhamento Justificado: ", boldStyle)
                    .addStyledText("Este é um exemplo de texto justificado que demonstra como as palavras são distribuídas uniformemente. ", normalStyle)
                    .addStyledText("Observe que o espaçamento entre as palavras é ajustado ", italicStyle)
                    .addStyledText("para que o texto fique alinhado em ambas as margens. ", underlineStyle)
                    .addStyledText("Este é um recurso muito utilizado em livros e documentos formais.", colorStyle)
                    .withAlignment(TextAlignment.JUSTIFIED)
                    .build();

            // 4. Adicionando os parágrafos
            builder.addParagraph(leftAligned)
                    .moveDown(20f)
                    .addParagraph(centered)
                    .moveDown(20f)
                    .addParagraph(rightAligned)
                    .moveDown(20f)
                    .addParagraph(justified);

            // 5. Salvando o PDF
            builder.save("demo_paragrafos_formatados.pdf");
            logger.info("PDF gerado com sucesso: demo_paragrafos_formatados.pdf");

        } catch (Exception e) {
            logger.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }
}
