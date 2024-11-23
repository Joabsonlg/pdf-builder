package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.text.SimpleText;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Color;

/**
 * Demonstração completa das funcionalidades implementadas no PDFBuilder.
 */
public class PDFBuilderDemo {
    private static final Logger logger = LoggerFactory.getLogger(PDFBuilderDemo.class);

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

            // 2. Criação do PDFBuilder
            PDFBuilder builder = new PDFBuilder(config);

            // 3. Demonstração de texto simples com formatação
            SimpleText title = SimpleText.builder()
                .withText("Demonstração de Texto Simples")
                .withFont(builder.getResourceManager().getFont("Helvetica-Bold"))
                .withFontSize(24f)
                .withLineSpacing(1.5f)
                .withColor(new Color(0, 102, 204)) // Azul
                .build();

            SimpleText subtitle = SimpleText.builder()
                .withText("Usando o componente SimpleText")
                .withFont(builder.getResourceManager().getFont("Times-Italic"))
                .withFontSize(16f)
                .withLineSpacing(1.3f)
                .withColor(new Color(102, 102, 102)) // Cinza
                .build();

            SimpleText normalText = SimpleText.builder()
                .withText("Este é um texto normal usando a fonte padrão.")
                .withFont(builder.getResourceManager().getDefaultFont())
                .withFontSize(12f)
                .build();

            SimpleText highlightText = SimpleText.builder()
                .withText(" Este texto está destacado em vermelho! Este texto está destacado em vermelho! Este texto está destacado em vermelho! Este texto está destacado em vermelho!")
                .withFont(builder.getResourceManager().getFont("Helvetica-Bold"))
                .withFontSize(12f)
                .withColor(new Color(204, 0, 0)) // Vermelho
                .build();

            // 4. Adicionando os textos
            builder.addSimpleText(title)
                .moveToStart()
                .moveDown(30f)
                .addSimpleText(subtitle)
                .moveToStart()
                .moveDown(60f)
                .addSimpleText(normalText)
                .addSimpleText(highlightText);

            // 5. Salvar o PDF
            builder.save("demo_texto_simples.pdf");
            logger.info("PDF gerado com sucesso: demo_texto_simples.pdf");

        } catch (Exception e) {
            logger.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }
}
