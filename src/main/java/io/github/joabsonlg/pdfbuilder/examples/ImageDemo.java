package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.image.Image;
import io.github.joabsonlg.pdfbuilder.components.text.Paragraph;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;

/**
 * Demonstração do uso de imagens no PDF Builder.
 */
public class ImageDemo {
    private static final Logger logger = LoggerFactory.getLogger(ImageDemo.class);

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
                    .build();

            PDFBuilder builder = new PDFBuilder(config);

            // Texto explicativo
            Paragraph intro = Paragraph.builder()
                    .addStyledText("Demonstração de Imagens\n\n",
                            TextStyle.builder()
                                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                                    .withFontSize(14f)
                                    .withColor(Color.BLACK)
                                    .build())
                    .addStyledText("Este exemplo demonstra diferentes maneiras de incluir e formatar imagens " +
                                    "em um documento PDF usando o PDF Builder. Abaixo você verá exemplos de:\n\n",
                            TextStyle.builder()
                                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                                    .withFontSize(12f)
                                    .build())
                    .addStyledText("1. Imagem em tamanho original (redimensionado se atingir o limite da página)\n" +
                                    "2. Imagem redimensionada mantendo proporção\n\n",
                            TextStyle.builder()
                                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                                    .withFontSize(12f)
                                    .build())
                    .build();

            // Carrega e adiciona imagens
            File imageFile = new File("C:\\Users\\joabs\\Documents\\Projetos\\MeusProjetos\\pdf-builder-workspace\\pdf-builder\\src\\main\\java\\br\\com\\nutit\\pdfbuilder\\examples\\sample-image.jpg");

            // Imagem original
            Image originalImage = Image.builder(builder.getDocument(), imageFile)
                    .withAlignment(Image.Alignment.CENTER)
                    .withCaption("Figura 1: Imagem em tamanho original")
                    .build();

            // Imagem redimensionada
            Image resizedImage = Image.builder(builder.getDocument(), imageFile)
                    .withWidth(300) // largura em pontos (1/72 polegada)
                    .withAlignment(Image.Alignment.RIGHT)
                    .withCaption("Figura 2: Imagem redimensionada para 300 pontos de largura e alinhada à direita")
                    .build();

            Image resizedImage2 = Image.builder(builder.getDocument(), imageFile)
                    .withWidth(100) // largura em pontos (1/72 polegada)
                    .withAlignment(Image.Alignment.LEFT)
                    .withCaption("Figura 3: Imagem redimensionada para 100 pontos de largura e alinhada à esquerda")
                    .build();

            // Monta o documento
            builder.addParagraph(intro)
                    .addImage(originalImage)
                    .addParagraph(Paragraph.builder()
                            .addStyledText("\n\n",
                                    TextStyle.builder()
                                            .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                                            .withFontSize(12f)
                                            .build())
                            .build())
                    .addImage(resizedImage)
                    .addImage(resizedImage2)
                    .addParagraph(Paragraph.builder()
                            .addStyledText("\nComo você pode ver acima, o PDF Builder oferece várias opções para manipulação " +
                                            "de imagens, permitindo controle sobre tamanho e proporção. Todas as imagens são " +
                                            "automaticamente ajustadas para caber dentro da área segura da página, garantindo uma " +
                                            "apresentação profissional do documento.",
                                    TextStyle.builder()
                                            .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                                            .withFontSize(12f)
                                            .withColor(Color.BLACK)
                                            .build())
                            .build());

            builder.save("demo_imagens.pdf");
            logger.info("PDF gerado com sucesso: demo_imagens.pdf");

        } catch (Exception e) {
            logger.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }
}
