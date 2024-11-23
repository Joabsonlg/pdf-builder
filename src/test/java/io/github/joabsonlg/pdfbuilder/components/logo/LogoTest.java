package io.github.joabsonlg.pdfbuilder.components.logo;

import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LogoTest {
    private PDFBuilder builder;
    private Logo logo;
    private LogoStyle style;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.A4)
            .withMargins(50f, 40f, 30f, 40f)
            .build();
        builder = new PDFBuilder(config);

        style = LogoStyle.builder()
            .withFontSize(16f)
            .withColor(Color.BLACK)
            .withImageWidth(50f)
            .withImageHeight(30f)
            .withMaintainAspectRatio(true)
            .withMarginBottom(20f)
            .withDrawLine(true)
            .build();
    }

    @Test
    void testLogoBuilder() {
        logo = Logo.builder()
            .withTitle("Test Title")
            .withStyle(style)
            .build();

        assertNotNull(logo);
        assertEquals("Test Title", logo.getTitle());
        assertEquals(style, logo.getStyle());
    }

    @Test
    void testLogoWithImages() {
        logo = Logo.builder()
            .withTitle("Test Title")
            .withStyle(style)
            .withLeftImage("path/to/left.png")
            .withRightImage("path/to/right.png")
            .build();

        assertNotNull(logo);
        assertEquals("path/to/left.png", logo.getLeftImagePath());
        assertEquals("path/to/right.png", logo.getRightImagePath());
    }

    @Test
    void testLogoStyleBuilder() {
        LogoStyle style = LogoStyle.builder()
            .withFontSize(16f)
            .withColor(Color.BLACK)
            .withImageWidth(50f)
            .withImageHeight(30f)
            .withMaintainAspectRatio(true)
            .withMarginBottom(20f)
            .withDrawLine(true)
            .build();

        assertNotNull(style);
        assertEquals(16f, style.getFontSize());
        assertEquals(Color.BLACK, style.getColor());
        assertEquals(50f, style.getImageWidth());
        assertEquals(30f, style.getImageHeight());
        assertTrue(style.isMaintainAspectRatio());
        assertEquals(20f, style.getMarginBottom());
        assertTrue(style.isDrawLine());
    }

    @Test
    void testLogoRender() throws IOException {
        logo = Logo.builder()
            .withTitle("Test Title")
            .withStyle(style)
            .build();

        File outputFile = tempDir.resolve("test-logo.pdf").toFile();
        builder.setLogo(logo);
        builder.save(outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    @Test
    void testLogoWithImagesRender() throws IOException {
        // Este teste requer imagens reais para funcionar corretamente
        // Você pode criar arquivos de imagem temporários ou usar recursos de teste
        String leftImagePath = getClass().getResource("/test-left-logo.png").getPath();
        String rightImagePath = getClass().getResource("/test-right-logo.png").getPath();

        logo = Logo.builder()
            .withTitle("Test Title")
            .withStyle(style)
            .withLeftImage(leftImagePath)
            .withRightImage(rightImagePath)
            .build();

        File outputFile = tempDir.resolve("test-logo-with-images.pdf").toFile();
        builder.setLogo(logo);
        builder.save(outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }
}
