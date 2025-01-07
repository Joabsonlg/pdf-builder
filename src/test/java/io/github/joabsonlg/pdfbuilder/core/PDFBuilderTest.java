package io.github.joabsonlg.pdfbuilder.core;

import io.github.joabsonlg.pdfbuilder.components.logo.LogoStyle;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PDFBuilder")
class PDFBuilderTest {

    private PDFBuilder pdfBuilder;
    private PDDocument document;

    @BeforeEach
    void setUp() {
        document = new PDDocument();
        pdfBuilder = PDFBuilder.create();
    }

    @AfterEach
    void tearDown() throws IOException {
        document.close();
    }

    @Test
    @DisplayName("Deve adicionar logo com imagens esquerda e direita")
    void shouldAddLogoWithLeftAndRightImages(@TempDir Path tempDir) throws IOException {
        // Criar imagens temporárias para teste
        Path leftImagePath = tempDir.resolve("left-logo.png");
        Path rightImagePath = tempDir.resolve("right-logo.png");
        createTestImage(leftImagePath);
        createTestImage(rightImagePath);

        LogoStyle style = LogoStyle.builder().build();
        pdfBuilder.setLogo("Test Logo", style, leftImagePath.toString(), rightImagePath.toString());
        pdfBuilder.addNewPage(); // Adiciona uma página para o logo ser renderizado
        PDPage page = pdfBuilder.getDocument().getPage(0);

        // Verifica se o número de XObjects (imagens) é 2
        long numberOfXObjects = 0;
        for (COSName ignored : page.getResources().getXObjectNames()) {
            numberOfXObjects++;
        }
        assertTrue(numberOfXObjects == 2, "Deveriam existir 2 imagens de logo (esquerda e direita).");
    }

    /**
     * Cria uma imagem de teste no caminho especificado.
     *
     * @param path Caminho onde a imagem será criada
     * @throws IOException se houver erro ao criar a imagem
     */
    private void createTestImage(Path path) throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        ImageIO.write(image, "PNG", path.toFile());
    }
}
