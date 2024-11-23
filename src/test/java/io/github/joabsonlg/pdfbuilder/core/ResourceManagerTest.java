package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResourceManager")
class ResourceManagerTest {

    private ResourceManager resourceManager;
    private PDDocument document;

    @BeforeEach
    void setUp() throws IOException {
        document = new PDDocument();
        resourceManager = new ResourceManager(document);
    }

    @AfterEach
    void tearDown() throws IOException {
        document.close();
    }

    @Test
    @DisplayName("Deve inicializar com fonte padrão Helvetica")
    void shouldInitializeWithDefaultFont() {
        PDFont defaultFont = resourceManager.getDefaultFont();
        PDFont helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        assertEquals(helvetica.getName(), defaultFont.getName());
    }

    @Test
    @DisplayName("Deve alterar fonte padrão")
    void shouldChangeDefaultFont() {
        resourceManager.setDefaultFont("Times-Roman");
        PDFont timesRoman = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
        assertEquals(timesRoman.getName(), resourceManager.getDefaultFont().getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir fonte inválida")
    void shouldThrowExceptionForInvalidFont() {
        assertThrows(IllegalArgumentException.class, () -> resourceManager.setDefaultFont("FonteInexistente"));
    }

    @Test
    @DisplayName("Deve retornar fonte por nome")
    void shouldGetFontByName() {
        PDFont helveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDFont font = resourceManager.getFont("Helvetica-Bold");
        assertNotNull(font);
        assertEquals(helveticaBold.getName(), font.getName());
    }

    @Test
    @DisplayName("Deve carregar e recuperar imagem de arquivo")
    void shouldLoadAndRetrieveImageFromFile(@TempDir Path tempDir) throws IOException {
        // Criar uma imagem temporária para teste
        Path imagePath = tempDir.resolve("test.png");
        createTestImage(imagePath);

        resourceManager.loadImage("test", imagePath);
        PDImageXObject image = resourceManager.getImage("test");
        assertNotNull(image);
    }

    @Test
    @DisplayName("Deve carregar e recuperar imagem de InputStream")
    void shouldLoadAndRetrieveImageFromInputStream() throws IOException {
        // Criar uma imagem em memória
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();

        // Converter para PNG em memória
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        resourceManager.loadImage("test", inputStream);
        PDImageXObject image = resourceManager.getImage("test");
        assertNotNull(image);
    }

    @Test
    @DisplayName("Deve remover imagem")
    void shouldRemoveImage(@TempDir Path tempDir) throws IOException {
        // Criar uma imagem temporária para teste
        Path imagePath = tempDir.resolve("test.png");
        createTestImage(imagePath);

        resourceManager.loadImage("test", imagePath);
        assertTrue(resourceManager.removeImage("test"));
        assertNull(resourceManager.getImage("test"));
    }

    @Test
    @DisplayName("Deve retornar falso ao tentar remover imagem inexistente")
    void shouldReturnFalseWhenRemovingNonexistentImage() {
        assertFalse(resourceManager.removeImage("nonexistent"));
    }

    @Test
    @DisplayName("Deve criar PDResources")
    void shouldCreatePageResources() {
        assertNotNull(resourceManager.createPageResources());
    }

    /**
     * Cria uma imagem de teste no caminho especificado.
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
