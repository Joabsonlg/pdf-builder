package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    private PDRectangle pageSize;
    private SafeArea safeArea;
    private Coordinates coordinates;

    @BeforeEach
    void setUp() {
        pageSize = PDRectangle.A4;
        safeArea = new SafeArea.Builder(pageSize)
            .margins(50f, 50f, 50f, 50f)
            .headerHeight(30f)
            .footerHeight(20f)
            .build();
        coordinates = Coordinates.origin(pageSize, safeArea);
    }

    @Test
    @DisplayName("Deve criar coordenadas na origem")
    void shouldCreateAtOrigin() {
        assertEquals(0f, coordinates.getX());
        assertEquals(0f, coordinates.getY());
        assertEquals(pageSize, coordinates.getPageSize());
        assertNotNull(coordinates.getSafeArea());
    }

    @Test
    @DisplayName("Deve mover para posição específica")
    void shouldMoveToPosition() {
        Coordinates moved = coordinates.moveTo(100f, 200f);
        
        assertEquals(100f, moved.getX());
        assertEquals(200f, moved.getY());
    }

    @Test
    @DisplayName("Deve mover relativamente")
    void shouldMoveRelatively() {
        Coordinates initial = coordinates.moveTo(50f, 50f);
        Coordinates moved = initial.moveBy(25f, -25f);
        
        assertEquals(75f, moved.getX());
        assertEquals(25f, moved.getY());
    }

    @Test
    @DisplayName("Deve mover para porcentagem da área de conteúdo")
    void shouldMoveToContentPercent() {
        Coordinates moved = coordinates.moveToContentPercent(50f, 50f);
        PDRectangle contentArea = safeArea.getContentArea();
        
        float expectedX = contentArea.getLowerLeftX() + (contentArea.getWidth() * 0.5f);
        float expectedY = contentArea.getLowerLeftY() + (contentArea.getHeight() * 0.5f);
        
        assertEquals(expectedX, moved.getX(), 0.01f);
        assertEquals(expectedY, moved.getY(), 0.01f);
    }

    @Test
    @DisplayName("Deve mover para área do cabeçalho")
    void shouldMoveToHeader() {
        Coordinates moved = coordinates.moveToHeader(50f, 50f);
        PDRectangle headerArea = safeArea.getHeaderArea();
        
        float expectedX = headerArea.getLowerLeftX() + (headerArea.getWidth() * 0.5f);
        float expectedY = headerArea.getLowerLeftY() + (headerArea.getHeight() * 0.5f);
        
        assertEquals(expectedX, moved.getX(), 0.01f);
        assertEquals(expectedY, moved.getY(), 0.01f);
    }

    @Test
    @DisplayName("Deve mover para área do rodapé")
    void shouldMoveToFooter() {
        Coordinates moved = coordinates.moveToFooter(50f, 50f);
        PDRectangle footerArea = safeArea.getFooterArea();
        
        float expectedX = footerArea.getLowerLeftX() + (footerArea.getWidth() * 0.5f);
        float expectedY = footerArea.getLowerLeftY() + (footerArea.getHeight() * 0.5f);
        
        assertEquals(expectedX, moved.getX(), 0.01f);
        assertEquals(expectedY, moved.getY(), 0.01f);
    }

    @Test
    @DisplayName("Deve validar ponto na área segura")
    void shouldValidatePointInSafeArea() {
        PDRectangle contentArea = safeArea.getContentArea();
        
        // Ponto dentro da área segura
        Coordinates inside = coordinates.moveTo(
            contentArea.getLowerLeftX() + 10,
            contentArea.getLowerLeftY() + 10
        );
        assertTrue(inside.isInSafeArea());
        
        // Ponto fora da área segura
        Coordinates outside = coordinates.moveTo(10f, 10f);
        assertFalse(outside.isInSafeArea());
    }

    @Test
    @DisplayName("Deve ajustar coordenadas para área segura")
    void shouldAdjustToSafeArea() {
        PDRectangle contentArea = safeArea.getContentArea();
        
        // Ponto fora da área segura (muito à esquerda e abaixo)
        Coordinates outside = coordinates.moveTo(10f, 10f);
        Coordinates adjusted = outside.ensureInSafeArea();
        
        assertEquals(contentArea.getLowerLeftX(), adjusted.getX());
        assertEquals(contentArea.getLowerLeftY(), adjusted.getY());
        
        // Ponto fora da área segura (muito à direita e acima)
        outside = coordinates.moveTo(
            pageSize.getWidth() + 10,
            pageSize.getHeight() + 10
        );
        adjusted = outside.ensureInSafeArea();
        
        assertEquals(contentArea.getUpperRightX(), adjusted.getX());
        assertEquals(contentArea.getUpperRightY(), adjusted.getY());
    }

    @Test
    @DisplayName("Deve rejeitar percentagens inválidas")
    void shouldRejectInvalidPercentages() {
        assertThrows(IllegalArgumentException.class, () ->
            coordinates.moveToContentPercent(-1f, 50f));
            
        assertThrows(IllegalArgumentException.class, () ->
            coordinates.moveToContentPercent(50f, 101f));
            
        assertThrows(IllegalArgumentException.class, () ->
            coordinates.moveToHeader(-1f, 50f));
            
        assertThrows(IllegalArgumentException.class, () ->
            coordinates.moveToFooter(50f, 101f));
    }

    @Test
    @DisplayName("Deve rejeitar movimento para cabeçalho quando não definido")
    void shouldRejectHeaderMoveWhenNotDefined() {
        SafeArea safeAreaNoHeader = new SafeArea.Builder(pageSize)
            .margins(50f, 50f, 50f, 50f)
            .build();
        Coordinates coords = Coordinates.origin(pageSize, safeAreaNoHeader);
        
        assertThrows(IllegalStateException.class, () ->
            coords.moveToHeader(50f, 50f));
    }

    @Test
    @DisplayName("Deve rejeitar movimento para rodapé quando não definido")
    void shouldRejectFooterMoveWhenNotDefined() {
        SafeArea safeAreaNoFooter = new SafeArea.Builder(pageSize)
            .margins(50f, 50f, 50f, 50f)
            .build();
        Coordinates coords = Coordinates.origin(pageSize, safeAreaNoFooter);
        
        assertThrows(IllegalStateException.class, () ->
            coords.moveToFooter(50f, 50f));
    }

    @Test
    @DisplayName("Deve mover para o topo da área de conteúdo")
    void shouldMoveToTop() {
        // Given
        float marginTop = 50f;
        float marginRight = 50f;
        float marginBottom = 50f;
        float marginLeft = 50f;
        float headerHeight = 30f;
        SafeArea safeArea = new SafeArea.Builder(pageSize)
            .margins(marginTop, marginRight, marginBottom, marginLeft)
            .headerHeight(headerHeight)
            .build();
        Coordinates coords = Coordinates.origin(pageSize, safeArea);
        
        // When
        Coordinates moved = coords.moveToTop();
        
        // Then
        float expectedY = pageSize.getHeight() - marginTop - headerHeight;
        assertEquals(coords.getX(), moved.getX(), 0.1); // X não deve mudar
        assertEquals(expectedY, moved.getY(), 0.1);
    }
}
