package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.text.*;
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
 * Demonstração do uso de títulos e subtítulos.
 */
public final class HeadingDemo {

    private HeadingDemo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HeadingDemo.class);

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
                .build();

            PDFBuilder builder = new PDFBuilder(config);

            // Título principal (H1)
            Heading mainTitle = Heading.builder()
                .withText("Relatório Anual 2024")
                .withLevel(HeadingLevel.H1)
                .build();

            // Subtítulo (H2) com cor personalizada
            TextStyle blueStyle = TextStyle.builder()
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                .withFontSize(HeadingLevel.H2.getFontSize())
                .withColor(new Color(0, 102, 204))
                .build();

            Heading subtitle = Heading.builder()
                .withText("Resultados do Primeiro Trimestre")
                .withLevel(HeadingLevel.H2)
                .withStyle(blueStyle)
                .build();

            // Seções numeradas (H3)
            Heading section1 = Heading.builder()
                .withText("Visão Geral Financeira")
                .withLevel(HeadingLevel.H3)
                .withNumbering(true)
                .withNumber("1.")
                .build();

            Heading section2 = Heading.builder()
                .withText("Análise de Mercado")
                .withLevel(HeadingLevel.H3)
                .withNumbering(true)
                .withNumber("2.")
                .build();

            // Subseções (H4)
            Heading subsection = Heading.builder()
                .withText("Tendências Observadas")
                .withLevel(HeadingLevel.H4)
                .withNumbering(true)
                .withNumber("2.1.")
                .build();

            // Texto de exemplo para demonstrar espaçamento
            Paragraph text = Paragraph.builder()
                .addStyledText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                    "nisi ut aliquip ex ea commodo consequat.",
                    TextStyle.builder()
                        .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                        .withFontSize(12f)
                        .build())
                .build();

            // Montando o documento
            builder.addHeading(mainTitle)
                .addHeading(subtitle)
                .addHeading(section1)
                .addParagraph(text)
                .addHeading(section2)
                .addHeading(subsection)
                .addParagraph(text);

            builder.save("demo_titulos.pdf");
            LOGGER.info("PDF gerado com sucesso: demo_titulos.pdf");

        } catch (Exception e) {
            LOGGER.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }
}
