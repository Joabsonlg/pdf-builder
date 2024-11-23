package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class PDFConfigurationTest {

    @Test
    @DisplayName("Deve criar configuração com valores padrão")
    void shouldCreateWithDefaultValues() {
        // When
        PDFConfiguration config = PDFConfiguration.create().build();
        
        // Then
        assertEquals(PDRectangle.A4, config.getPageSize());
        assertEquals(50f, config.getMarginTop());
        assertEquals(50f, config.getMarginRight());
        assertEquals(50f, config.getMarginBottom());
        assertEquals(50f, config.getMarginLeft());
        assertEquals(300, config.getDpi());
        assertEquals(0.7f, config.getCompressionQuality(), 0.001f);
    }

    @Test
    @DisplayName("Deve configurar tamanho de página personalizado")
    void shouldSetCustomPageSize() {
        // When
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.A3)
            .build();
        
        // Then
        assertEquals(PDRectangle.A3.getWidth(), config.getPageSize().getWidth(), 0.001f);
        assertEquals(PDRectangle.A3.getHeight(), config.getPageSize().getHeight(), 0.001f);
    }

    @Test
    @DisplayName("Deve configurar margem uniforme")
    void shouldSetUniformMargin() {
        // Given
        float margin = 100f;
        
        // When
        PDFConfiguration config = PDFConfiguration.create()
            .withMargin(margin)
            .build();
        
        // Then
        assertEquals(margin, config.getMarginTop());
        assertEquals(margin, config.getMarginRight());
        assertEquals(margin, config.getMarginBottom());
        assertEquals(margin, config.getMarginLeft());
    }

    @Test
    @DisplayName("Deve configurar margens individuais")
    void shouldSetIndividualMargins() {
        // Given
        float top = 100f;
        float right = 90f;
        float bottom = 120f;
        float left = 80f;
        
        // When
        PDFConfiguration config = PDFConfiguration.create()
            .withMargins(top, right, bottom, left)
            .build();
        
        // Then
        assertEquals(top, config.getMarginTop());
        assertEquals(right, config.getMarginRight());
        assertEquals(bottom, config.getMarginBottom());
        assertEquals(left, config.getMarginLeft());
    }

    @Test
    @DisplayName("Deve calcular área de conteúdo corretamente")
    void shouldCalculateContentArea() {
        // Given
        PDFConfiguration config = PDFConfiguration.create()
            .withPageSize(PDRectangle.A4)
            .withMargin(50f)
            .build();
        
        // When
        PDRectangle contentArea = config.getContentArea();
        
        // Then
        float expectedWidth = PDRectangle.A4.getWidth() - (2 * 50f);
        float expectedHeight = PDRectangle.A4.getHeight() - (2 * 50f);
        
        assertEquals(expectedWidth, contentArea.getWidth(), 0.001f);
        assertEquals(expectedHeight, contentArea.getHeight(), 0.001f);
    }

    @Test
    @DisplayName("Deve configurar DPI")
    void shouldSetDPI() {
        // Given
        int dpi = 600;
        
        // When
        PDFConfiguration config = PDFConfiguration.create()
            .withDPI(dpi)
            .build();
        
        // Then
        assertEquals(dpi, config.getDpi());
    }

    @Test
    @DisplayName("Deve configurar qualidade de compressão")
    void shouldSetCompressionQuality() {
        // Given
        float quality = 0.5f;
        
        // When
        PDFConfiguration config = PDFConfiguration.create()
            .withCompressionQuality(quality)
            .build();
        
        // Then
        assertEquals(quality, config.getCompressionQuality(), 0.001f);
    }

    @Test
    @DisplayName("Deve rejeitar qualidade de compressão inválida")
    void shouldRejectInvalidCompressionQuality() {
        // Given
        float invalidQuality = 1.5f;
        
        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withCompressionQuality(invalidQuality).build();
        });
    }

    @Test
    @DisplayName("Deve criar configuração com margem uniforme")
    void shouldCreateConfigurationWithUniformMargin() {
        PDFConfiguration config = PDFConfiguration.create()
            .withMargin(100)
            .build();
        assertEquals(100, config.getMarginTop());
        assertEquals(100, config.getMarginRight());
        assertEquals(100, config.getMarginBottom());
        assertEquals(100, config.getMarginLeft());
    }

    @Test
    @DisplayName("Deve criar configuração com margens individuais")
    void shouldCreateConfigurationWithIndividualMargins() {
        PDFConfiguration config = PDFConfiguration.create()
            .withMargins(50, 60, 70, 80)
            .build();
        assertEquals(50, config.getMarginTop());
        assertEquals(60, config.getMarginRight());
        assertEquals(70, config.getMarginBottom());
        assertEquals(80, config.getMarginLeft());
    }

    @Test
    @DisplayName("Deve criar configuração com tamanho de fonte")
    void shouldCreateConfigurationWithFontSize() {
        PDFConfiguration config = PDFConfiguration.create()
            .withFontSize(14)
            .build();
        assertEquals(14, config.getFontSize());
    }

    @Test
    @DisplayName("Deve criar configuração com espaçamento entre linhas")
    void shouldCreateConfigurationWithLineSpacing() {
        PDFConfiguration config = PDFConfiguration.create()
            .withLineSpacing(16)
            .build();
        assertEquals(16, config.getLineSpacing());
    }

    @Test
    @DisplayName("Deve manter valores padrão quando não configurados")
    void shouldKeepDefaultValuesWhenNotConfigured() {
        PDFConfiguration config = PDFConfiguration.create().build();
        assertEquals(50, config.getMarginTop()); // DEFAULT_MARGIN
        assertEquals(12, config.getFontSize()); // DEFAULT_FONT_SIZE
        assertEquals(14, config.getLineSpacing()); // DEFAULT_LINE_SPACING
    }

    @Test
    @DisplayName("Deve permitir configuração em cadeia")
    void shouldAllowChainedConfiguration() {
        PDFConfiguration config = PDFConfiguration.create()
            .withMargin(100)
            .withFontSize(16)
            .withLineSpacing(18)
            .build();

        assertEquals(100, config.getMarginTop());
        assertEquals(16, config.getFontSize());
        assertEquals(18, config.getLineSpacing());
    }

    @Test
    @DisplayName("Deve rejeitar margens negativas")
    void shouldRejectNegativeMargins() {
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withMargin(-1).build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withMargins(-1, 0, 0, 0).build();
        });
    }

    @Test
    @DisplayName("Deve rejeitar tamanho de fonte negativo ou zero")
    void shouldRejectNegativeOrZeroFontSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withFontSize(-1).build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withFontSize(0).build();
        });
    }

    @Test
    @DisplayName("Deve rejeitar espaçamento entre linhas negativo ou zero")
    void shouldRejectNegativeOrZeroLineSpacing() {
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withLineSpacing(-1).build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withLineSpacing(0).build();
        });
    }

    @Test
    @DisplayName("Deve rejeitar DPI menor ou igual a zero")
    void shouldRejectZeroOrNegativeDPI() {
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withDPI(0).build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withDPI(-1).build();
        });
    }

    @Test
    @DisplayName("Deve rejeitar tamanho de página nulo")
    void shouldRejectNullPageSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withPageSize(null).build();
        });
    }

    @Test
    @DisplayName("Deve rejeitar margens negativas em qualquer posição")
    void shouldRejectNegativeMarginsInAnyPosition() {
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withMargins(0, -1, 0, 0).build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withMargins(0, 0, -1, 0).build();
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PDFConfiguration.create().withMargins(0, 0, 0, -1).build();
        });
    }

    @Test
    @DisplayName("Deve validar que getMargin retorna margem superior quando todas são iguais")
    void shouldValidateUniformMarginGetter() {
        PDFConfiguration config = PDFConfiguration.create()
            .withMargin(100)
            .build();
            
        assertEquals(100, config.getMargin());
        assertEquals(config.getMarginTop(), config.getMargin());
    }

    @Test
    @DisplayName("Deve manter valores padrão ao alterar apenas algumas configurações")
    void shouldKeepDefaultValuesWhenPartiallyConfigured() {
        PDFConfiguration config = PDFConfiguration.create()
            .withMargin(100)
            .withFontSize(16)
            .build();
            
        assertEquals(100, config.getMarginTop());
        assertEquals(16, config.getFontSize());
        assertEquals(14, config.getLineSpacing()); // valor padrão do lineSpacing
        assertEquals(300, config.getDpi()); // valor padrão do DPI
        assertEquals(0.7f, config.getCompressionQuality(), 0.001f); // valor padrão da qualidade de compressão
    }

    @Test
    @DisplayName("Deve validar consistência dos valores padrão")
    void shouldValidateDefaultValuesConsistency() {
        PDFConfiguration config = PDFConfiguration.create().build();
        
        // Verifica se todos os valores padrão são consistentes
        assertEquals(PDRectangle.A4, config.getPageSize());
        assertEquals(50f, config.getMarginTop());
        assertEquals(50f, config.getMarginRight());
        assertEquals(50f, config.getMarginBottom());
        assertEquals(50f, config.getMarginLeft());
        assertEquals(300, config.getDpi());
        assertEquals(0.7f, config.getCompressionQuality(), 0.001f);
        assertEquals(12f, config.getFontSize());
        assertEquals(14f, config.getLineSpacing());
    }

    @Test
    @DisplayName("Deve configurar área segura")
    void shouldSetSafeArea() {
        // Given
        SafeArea safeArea = SafeArea.builder()
            .withMargins(30f, 50f, 40f, 30f)
            .withHeader(true)
            .withFooter(true)
            .build();

        // When
        PDFConfiguration config = PDFConfiguration.create()
            .withSafeArea(safeArea)
            .build();

        // Then
        assertEquals(safeArea, config.getSafeArea());
        assertEquals(30f, config.getSafeArea().getMarginLeft());
        assertEquals(50f, config.getSafeArea().getMarginRight());
        assertEquals(40f, config.getSafeArea().getMarginBottom());
        assertEquals(30f, config.getSafeArea().getMarginTop());
        assertTrue(config.getSafeArea().hasHeader());
        assertTrue(config.getSafeArea().hasFooter());
    }

    @Test
    @DisplayName("Deve atualizar área segura ao alterar tamanho da página")
    void shouldUpdateSafeAreaWhenChangingPageSize() {
        // Given
        PDFConfiguration configA4 = PDFConfiguration.create()
            .withPageSize(PDRectangle.A4)
            .build();
            
        // When
        PDFConfiguration configA3 = PDFConfiguration.create()
            .withPageSize(PDRectangle.A3)
            .build();
        
        // Then
        // Verifica se as margens foram mantidas
        assertEquals(50f, configA4.getSafeArea().getMarginTop());
        assertEquals(50f, configA4.getSafeArea().getMarginRight());
        assertEquals(50f, configA4.getSafeArea().getMarginBottom());
        assertEquals(50f, configA4.getSafeArea().getMarginLeft());
        
        assertEquals(50f, configA3.getSafeArea().getMarginTop());
        assertEquals(50f, configA3.getSafeArea().getMarginRight());
        assertEquals(50f, configA3.getSafeArea().getMarginBottom());
        assertEquals(50f, configA3.getSafeArea().getMarginLeft());
        
        // Verifica que as áreas de conteúdo são diferentes
        assertNotEquals(
            configA4.getSafeArea().getContentArea(PDRectangle.A4).getWidth(),
            configA3.getSafeArea().getContentArea(PDRectangle.A3).getWidth()
        );
    }

    @Test
    @DisplayName("Deve rejeitar área segura nula")
    void shouldRejectNullSafeArea() {
        assertThrows(IllegalArgumentException.class, () -> 
            PDFConfiguration.create()
                .withSafeArea(null)
                .build());
    }
}
