package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class SafeAreaTest {

    @Test
    @DisplayName("Deve criar SafeArea com valores padrão")
    void shouldCreateWithDefaultValues() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4).build();
        
        assertEquals(50f, safeArea.getMarginTop());
        assertEquals(50f, safeArea.getMarginRight());
        assertEquals(50f, safeArea.getMarginBottom());
        assertEquals(50f, safeArea.getMarginLeft());
        assertEquals(0f, safeArea.getHeaderHeight());
        assertEquals(0f, safeArea.getFooterHeight());
        assertEquals(0f, safeArea.getBleedMargin());
        assertFalse(safeArea.isMirrored());
    }

    @Test
    @DisplayName("Deve criar SafeArea com margens personalizadas")
    void shouldCreateWithCustomMargins() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(70f, 60f, 50f, 40f)
            .build();
        
        assertEquals(70f, safeArea.getMarginTop());
        assertEquals(60f, safeArea.getMarginRight());
        assertEquals(50f, safeArea.getMarginBottom());
        assertEquals(40f, safeArea.getMarginLeft());
    }

    @Test
    @DisplayName("Deve criar SafeArea com cabeçalho e rodapé")
    void shouldCreateWithHeaderAndFooter() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .headerHeight(50f)
            .footerHeight(30f)
            .build();
        
        PDRectangle headerArea = safeArea.getHeaderArea();
        PDRectangle footerArea = safeArea.getFooterArea();
        
        assertNotNull(headerArea);
        assertNotNull(footerArea);
        assertEquals(50f, headerArea.getHeight());
        assertEquals(30f, footerArea.getHeight());
    }

    @Test
    @DisplayName("Deve calcular área de conteúdo corretamente")
    void shouldCalculateContentArea() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 50f, 50f, 50f)
            .headerHeight(30f)
            .footerHeight(20f)
            .build();
        
        PDRectangle contentArea = safeArea.getContentArea();
        
        assertEquals(50f, contentArea.getLowerLeftX());
        assertEquals(70f, contentArea.getLowerLeftY()); // marginBottom + footerHeight
        assertEquals(495.28f, contentArea.getWidth(), 0.01f);
        assertEquals(691.89f, contentArea.getHeight(), 0.01f);
    }

    @Test
    @DisplayName("Deve validar pontos na área segura")
    void shouldValidatePointsInSafeArea() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 50f, 50f, 50f)
            .build();
        
        assertTrue(safeArea.isPointInSafeArea(100f, 100f));
        assertFalse(safeArea.isPointInSafeArea(10f, 10f));
    }

    @Test
    @DisplayName("Deve calcular margens espelhadas corretamente")
    void shouldCalculateMirroredMargins() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 60f, 50f, 40f)
            .mirrored(true)
            .build();
        
        float[] normalMargins = safeArea.getMirrordMargins(false);
        float[] mirroredMargins = safeArea.getMirrordMargins(true);
        
        // Página ímpar (normal)
        assertEquals(50f, normalMargins[0]); // top
        assertEquals(60f, normalMargins[1]); // right
        assertEquals(50f, normalMargins[2]); // bottom
        assertEquals(40f, normalMargins[3]); // left
        
        // Página par (espelhada)
        assertEquals(50f, mirroredMargins[0]); // top
        assertEquals(40f, mirroredMargins[1]); // right (trocado com left)
        assertEquals(50f, mirroredMargins[2]); // bottom
        assertEquals(60f, mirroredMargins[3]); // left (trocado com right)
    }

    @Test
    @DisplayName("Deve rejeitar valores negativos")
    void shouldRejectNegativeValues() {
        SafeArea.Builder builder = new SafeArea.Builder(PDRectangle.A4);
        
        assertThrows(IllegalArgumentException.class, () -> 
            builder.margins(-1f, 0f, 0f, 0f));
            
        assertThrows(IllegalArgumentException.class, () -> 
            builder.headerHeight(-1f));
            
        assertThrows(IllegalArgumentException.class, () -> 
            builder.footerHeight(-1f));
            
        assertThrows(IllegalArgumentException.class, () -> 
            builder.bleedMargin(-1f));
    }

    @Test
    @DisplayName("Deve rejeitar página nula")
    void shouldRejectNullPage() {
        assertThrows(IllegalArgumentException.class, () -> 
            new SafeArea.Builder(null));
    }

    @Test
    @DisplayName("Deve calcular área com sangria")
    void shouldCalculateAreaWithBleed() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(50f, 50f, 50f, 50f)
            .bleedMargin(10f)
            .build();
        
        PDRectangle contentArea = safeArea.getContentArea();
        
        assertEquals(60f, contentArea.getLowerLeftX()); // marginLeft + bleed
        assertEquals(60f, contentArea.getLowerLeftY()); // marginBottom + bleed
        assertEquals(475.28f, contentArea.getWidth(), 0.01f); // pageWidth - (margins + 2*bleed)
        assertEquals(721.89f, contentArea.getHeight(), 0.01f); // pageHeight - (margins + 2*bleed)
    }

    @Test
    @DisplayName("Não deve retornar áreas de cabeçalho/rodapé quando altura é zero")
    void shouldNotReturnHeaderFooterAreasWhenHeightIsZero() {
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4).build();
        
        assertNull(safeArea.getHeaderArea());
        assertNull(safeArea.getFooterArea());
    }

    @Test
    @DisplayName("Deve calcular posição do topo da área de conteúdo")
    void shouldCalculateTopPosition() {
        // Given
        float marginTop = 50f;
        float marginRight = 50f;
        float marginBottom = 50f;
        float marginLeft = 50f;
        float headerHeight = 30f;
        SafeArea safeArea = new SafeArea.Builder(PDRectangle.A4)
            .margins(marginTop, marginRight, marginBottom, marginLeft)
            .headerHeight(headerHeight)
            .build();
        
        // When
        float topY = safeArea.moveToTop();
        
        // Then
        float expectedY = PDRectangle.A4.getHeight() - marginTop - headerHeight;
        assertEquals(expectedY, topY, 0.1);
    }
}
