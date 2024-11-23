package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PDFBuilderTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Deve criar builder com configuração padrão")
    void createWithDefaultConfiguration() {
        PDFBuilder builder = PDFBuilder.create();
        assertNotNull(builder);
        assertNotNull(builder.getDocument());
        assertNotNull(builder.getCurrentPage());
        assertNotNull(builder.getCurrentPosition());
        assertEquals(PDRectangle.A4, builder.getConfig().getPageSize());
    }

    @Test
    @DisplayName("Deve criar builder com configuração personalizada")
    void createWithCustomConfiguration() {
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.LETTER)
            .withSafeArea(new SafeArea.Builder(PDRectangle.LETTER)
                .margins(50f, 50f, 50f, 50f)
                .build())
            .build();

        PDFBuilder builder = PDFBuilder.create(config);
        assertNotNull(builder);
        assertEquals(PDRectangle.LETTER, builder.getConfig().getPageSize());
        assertEquals(50f, builder.getConfig().getSafeArea().getMarginTop());
    }

    @Test
    @DisplayName("Deve criar nova página com tamanho correto")
    void addNewPageCreatesNewPageWithCorrectSize() {
        PDFBuilder builder = PDFBuilder.create();
        PDPage initialPage = builder.getCurrentPage();
        
        builder.addNewPage();
        PDPage newPage = builder.getCurrentPage();
        
        assertNotNull(newPage);
        assertNotEquals(initialPage, newPage);
        assertEquals(PDRectangle.A4, newPage.getMediaBox());
    }

    @Test
    @DisplayName("Deve atualizar posição com moveTo")
    void moveToUpdatesPosition() {
        PDFBuilder builder = PDFBuilder.create();
        builder.moveTo(100, 200);
        
        Coordinates pos = builder.getCurrentPosition();
        assertEquals(100, pos.getX());
        assertEquals(200, pos.getY());
    }

    @Test
    @DisplayName("Deve atualizar posição relativamente com moveBy")
    void moveByUpdatesPositionRelatively() {
        PDFBuilder builder = PDFBuilder.create();
        builder.moveTo(100, 100).moveBy(50, -25);
        
        Coordinates pos = builder.getCurrentPosition();
        assertEquals(150, pos.getX());
        assertEquals(75, pos.getY());
    }

    @Test
    @DisplayName("Deve posicionar corretamente na área de conteúdo")
    void moveToContentPercentPositionsCorrectly() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 50f, 50f, 50f)
            .build();
        
        PDFConfiguration config = PDFConfiguration.create()
            .withSafeArea(safeArea)
            .build();

        PDFBuilder builder = PDFBuilder.create(config);
        builder.moveToContentPercent(50, 50);
        
        Coordinates pos = builder.getCurrentPosition();
        PDRectangle contentArea = safeArea.getContentArea();
        float expectedX = contentArea.getLowerLeftX() + (contentArea.getWidth() * 0.5f);
        float expectedY = contentArea.getLowerLeftY() + (contentArea.getHeight() * 0.5f);
        
        assertEquals(expectedX, pos.getX(), 0.1);
        assertEquals(expectedY, pos.getY(), 0.1);
    }

    @Test
    @DisplayName("Deve posicionar corretamente na área do cabeçalho")
    void moveToHeaderPositionsInHeaderArea() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 50f, 50f, 50f)
            .headerHeight(100f)
            .build();
            
        PDFConfiguration config = PDFConfiguration.create()
            .withSafeArea(safeArea)
            .build();

        PDFBuilder builder = PDFBuilder.create(config);
        builder.moveToHeader(50, 50);
        
        Coordinates pos = builder.getCurrentPosition();
        PDRectangle headerArea = safeArea.getHeaderArea();
        float expectedY = headerArea.getLowerLeftY() + (headerArea.getHeight() * 0.5f);
        assertTrue(pos.getY() >= expectedY);
    }

    @Test
    @DisplayName("Deve posicionar corretamente na área do rodapé")
    void moveToFooterPositionsInFooterArea() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 50f, 50f, 50f)
            .footerHeight(100f)
            .build();
            
        PDFConfiguration config = PDFConfiguration.create()
            .withSafeArea(safeArea)
            .build();

        PDFBuilder builder = PDFBuilder.create(config);
        builder.moveToFooter(50, 50);
        
        Coordinates pos = builder.getCurrentPosition();
        PDRectangle footerArea = safeArea.getFooterArea();
        float expectedY = footerArea.getLowerLeftY() + (footerArea.getHeight() * 0.5f);
        assertTrue(pos.getY() <= expectedY + footerArea.getHeight());
    }

    @Test
    @DisplayName("Deve criar arquivo PDF com texto")
    void addTextAndSaveCreatesPDFFile() throws IOException {
        PDFBuilder builder = PDFBuilder.create();
        String testText = "Hello, PDF!";
        
        File outputFile = tempDir.resolve("test.pdf").toFile();
        
        builder.moveTo(50, 700)
            .addText(testText)
            .save(outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
        
        try (PDDocument doc = Loader.loadPDF(outputFile)) {
            assertEquals(1, doc.getNumberOfPages());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção para configuração nula")
    void throwsExceptionForNullConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> PDFBuilder.create(null));
    }

    @Test
    @DisplayName("Deve criar PDF com configurações padrão")
    void shouldCreatePDFWithDefaultSettings() throws IOException {
        Path pdfPath = tempDir.resolve("test.pdf");
        
        PDFBuilder builder = PDFBuilder.create();
        builder.save(pdfPath.toString());
        builder.close();
        
        assertTrue(pdfPath.toFile().exists());
        assertTrue(pdfPath.toFile().length() > 0);
    }

    @Test
    @DisplayName("Deve criar PDF com configuração personalizada")
    void shouldCreatePDFWithCustomConfiguration() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A3)
            .margins(100f, 100f, 100f, 100f)
            .build();
            
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.A3)
            .withSafeArea(safeArea)
            .withDPI(600)
            .build();
        
        PDFBuilder builder = PDFBuilder.create(config);
        
        assertEquals(PDRectangle.A3, builder.getConfig().getPageSize());
        assertEquals(100f, builder.getConfig().getSafeArea().getMarginTop());
        assertEquals(600, builder.getConfig().getDpi());
    }

    @Test
    @DisplayName("Deve criar PDF com margens personalizadas")
    void shouldCreatePDFWithCustomMargins() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(100f, 100f, 100f, 100f)
            .build();
            
        PDFConfiguration config = PDFConfiguration.create()
            .withSafeArea(safeArea)
            .build();
        
        PDFBuilder builder = PDFBuilder.create(config);
        
        assertEquals(100f, builder.getConfig().getSafeArea().getMarginTop());
    }

    @Test
    @DisplayName("Deve atualizar tamanho da página via PDFBuilder")
    void shouldUpdatePageSizeViaPDFBuilder() {
        PDFBuilder builder = PDFBuilder.create()
            .withPageSize(PDRectangle.A3);
        
        PDRectangle pageSize = builder.getConfig().getPageSize();
        assertEquals(PDRectangle.A3.getWidth(), pageSize.getWidth(), 0.001);
        assertEquals(PDRectangle.A3.getHeight(), pageSize.getHeight(), 0.001);
    }

    @Test
    @DisplayName("Deve adicionar múltiplas páginas")
    void shouldAddMultiplePages() {
        PDFBuilder builder = PDFBuilder.create();
        int initialPageCount = builder.getDocument().getNumberOfPages();
        
        builder.addNewPage()
               .addNewPage();
        
        assertEquals(initialPageCount + 2, builder.getDocument().getNumberOfPages());
    }

    @Test
    @DisplayName("Deve usar novo tamanho de página ao adicionar página")
    void shouldUseNewPageSizeWhenAddingPage() {
        PDFBuilder builder = PDFBuilder.create()
            .withPageSize(PDRectangle.A3)
            .addNewPage();
        
        PDPage currentPage = builder.getCurrentPage();
        assertEquals(PDRectangle.A3.getWidth(), currentPage.getMediaBox().getWidth(), 0.001);
        assertEquals(PDRectangle.A3.getHeight(), currentPage.getMediaBox().getHeight(), 0.001);
    }

    @Test
    @DisplayName("Deve retornar uma instância de ResourceManager")
    void shouldReturnResourceManagerInstance() {
        PDFBuilder builder = PDFBuilder.create();
        assertNotNull(builder.getResourceManager());
    }

    @Test
    @DisplayName("Deve manter a mesma instância de ResourceManager")
    void shouldMaintainSameResourceManagerInstance() {
        PDFBuilder builder = PDFBuilder.create();
        ResourceManager manager1 = builder.getResourceManager();
        ResourceManager manager2 = builder.getResourceManager();
        assertSame(manager1, manager2);
    }

    @Test
    @DisplayName("ResourceManager deve inicializar com fonte padrão")
    void resourceManagerShouldInitializeWithDefaultFont() {
        PDFBuilder builder = PDFBuilder.create();
        ResourceManager manager = builder.getResourceManager();
        PDFont helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        assertEquals(helvetica.getName(), manager.getDefaultFont().getName());
    }

    @Test
    @DisplayName("Deve adicionar texto usando sistema de coordenadas")
    void shouldAddTextUsingCoordinates() throws IOException {
        PDFBuilder builder = PDFBuilder.create();
        Path outputPath = tempDir.resolve("test.pdf");

        builder.moveToTop()
               .moveRight(50.0f)
               .addText("Primeiro texto")
               .moveDown(20.0f)
               .moveToStart()
               .moveRight(100.0f)
               .addText("Segundo texto")
               .moveToBottom()
               .moveToStart()
               .addText("Texto no final");

        builder.save(outputPath.toString());
        assertTrue(outputPath.toFile().exists());
        assertTrue(outputPath.toFile().length() > 0);
    }

    @Test
    @DisplayName("Deve adicionar múltiplas linhas de texto")
    void shouldAddMultipleLines() throws IOException {
        PDFBuilder builder = PDFBuilder.create();
        Path outputPath = tempDir.resolve("test.pdf");

        builder.moveToTop()
               .addLine("Primeira linha")
               .addLine("Segunda linha")
               .addLine("Terceira linha")
               .addLine("Quarta linha")
               .addLine("Quinta linha");

        builder.save(outputPath.toString());
        assertTrue(outputPath.toFile().exists());
        assertTrue(outputPath.toFile().length() > 0);
    }
}
