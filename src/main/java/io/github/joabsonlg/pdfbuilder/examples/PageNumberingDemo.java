package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.page.PageNumbering;
import io.github.joabsonlg.pdfbuilder.components.text.Heading;
import io.github.joabsonlg.pdfbuilder.components.text.HeadingLevel;
import io.github.joabsonlg.pdfbuilder.components.text.Paragraph;
import io.github.joabsonlg.pdfbuilder.components.text.TextAlignment;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;

public final class PageNumberingDemo {

    private PageNumberingDemo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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

            // Cria o builder do PDF
            PDFBuilder builder = new PDFBuilder(config);

            // Configura a numeração de páginas
            PageNumbering pageNumbering = PageNumbering.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                    .withFontSize(10)
                    .withColor(new Color(128, 128, 128))
                    .withFormat(PageNumbering.Format.WITH_TOTAL)
                    .withPosition(PageNumbering.Position.BOTTOM)
                    .withAlignment(TextAlignment.RIGHT)
                    .build();

            // Adiciona a numeração de páginas ao documento
            builder.setPageNumbering(pageNumbering);

            // Estilos de texto
            TextStyle titleStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(24f)
                    .build();

            TextStyle normalStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                    .withFontSize(12f)
                    .build();

            // Adiciona conteúdo de exemplo para gerar várias páginas
            for (int i = 1; i <= 5; i++) {
                // Adiciona um título
                Heading heading = Heading.builder()
                        .withText("Página " + i)
                        .withLevel(HeadingLevel.H1)
                        .withStyle(titleStyle)
                        .build();
                builder.addHeading(heading);

                // Adiciona texto de exemplo
                String text = "Este é um exemplo de texto para a página " + i + ". " +
                        "O texto é repetido várias vezes para ocupar espaço e forçar a criação de uma nova página. "
                                .repeat(10);

                Paragraph paragraph = Paragraph.builder()
                        .addStyledText(text, normalStyle)
                        .withAlignment(TextAlignment.JUSTIFIED)
                        .build();
                builder.addParagraph(paragraph);

                // Move para a próxima página
                if (i < 5) {
                    builder.addNewPage();
                }
            }

            // Salva o documento
            builder.save("page-numbering-demo.pdf");

            System.out.println("PDF gerado com sucesso: page-numbering-demo.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
