package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Define a área segura para renderização de conteúdo no PDF.
 */
public class SafeArea {
    private static final float HEADER_HEIGHT = 40f;
    private static final float FOOTER_HEIGHT = 40f;
    
    private final float originalMarginLeft;
    private final float originalMarginRight;
    private final float originalMarginTop;
    private final float originalMarginBottom;
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float marginBottom;
    private final boolean hasHeader;
    private final boolean hasFooter;

    private SafeArea(Builder builder) {
        this.originalMarginLeft = builder.marginLeft;
        this.originalMarginRight = builder.marginRight;
        this.originalMarginTop = builder.marginTop;
        this.originalMarginBottom = builder.marginBottom;
        this.marginLeft = builder.marginLeft;
        this.marginRight = builder.marginRight;
        this.marginTop = builder.marginTop;
        this.marginBottom = builder.marginBottom;
        this.hasHeader = builder.hasHeader;
        this.hasFooter = builder.hasFooter;
    }

    /**
     * Retorna a área de conteúdo segura considerando as margens e áreas de cabeçalho/rodapé.
     * @param pageSize Tamanho da página
     * @return Retângulo representando a área segura
     */
    public PDRectangle getContentArea(PDRectangle pageSize) {
        if (pageSize == null) {
            throw new IllegalArgumentException("pageSize não pode ser nulo");
        }
        
        float extraTopMargin = hasHeader ? HEADER_HEIGHT : 0;
        float extraBottomMargin = hasFooter ? FOOTER_HEIGHT : 0;
        
        return new PDRectangle(
            marginLeft,
            marginBottom + extraBottomMargin,
            pageSize.getWidth() - marginLeft - marginRight,
            pageSize.getHeight() - marginTop - marginBottom - extraTopMargin - extraBottomMargin
        );
    }

    /**
     * Retorna a área do cabeçalho.
     * @param pageSize Tamanho da página
     * @return Retângulo representando a área do cabeçalho
     * @throws IllegalStateException se o cabeçalho não estiver habilitado
     */
    public PDRectangle getHeaderArea(PDRectangle pageSize) {
        if (pageSize == null) {
            throw new IllegalArgumentException("pageSize não pode ser nulo");
        }
        if (!hasHeader) {
            throw new IllegalStateException("Cabeçalho não está habilitado");
        }
        
        return new PDRectangle(
            marginLeft,
            pageSize.getHeight() - marginTop - HEADER_HEIGHT,
            pageSize.getWidth() - marginLeft - marginRight,
            HEADER_HEIGHT
        );
    }

    /**
     * Retorna a área do rodapé.
     * @param pageSize Tamanho da página
     * @return Retângulo representando a área do rodapé
     * @throws IllegalStateException se o rodapé não estiver habilitado
     */
    public PDRectangle getFooterArea(PDRectangle pageSize) {
        if (pageSize == null) {
            throw new IllegalArgumentException("pageSize não pode ser nulo");
        }
        if (!hasFooter) {
            throw new IllegalStateException("Rodapé não está habilitado");
        }
        
        return new PDRectangle(
            marginLeft,
            marginBottom,
            pageSize.getWidth() - marginLeft - marginRight,
            FOOTER_HEIGHT
        );
    }

    /**
     * Verifica se um ponto está dentro da área segura.
     * @param x Coordenada X do ponto
     * @param y Coordenada Y do ponto
     * @param pageSize Tamanho da página
     * @return true se o ponto estiver dentro da área segura
     */
    public boolean isPointInSafeArea(float x, float y, PDRectangle pageSize) {
        PDRectangle contentArea = getContentArea(pageSize);
        return x >= contentArea.getLowerLeftX() && 
               x <= contentArea.getUpperRightX() && 
               y >= contentArea.getLowerLeftY() && 
               y <= contentArea.getUpperRightY();
    }

    public void reset() {
        this.marginTop = this.originalMarginTop;
        this.marginRight = this.originalMarginRight;
        this.marginBottom = this.originalMarginBottom;
        this.marginLeft = this.originalMarginLeft;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public boolean hasHeader() { return hasHeader; }
    public boolean hasFooter() { return hasFooter; }
    public float getHeaderHeight() { return HEADER_HEIGHT; }
    public float getFooterHeight() { return FOOTER_HEIGHT; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private float marginLeft = 50f;
        private float marginRight = 50f;
        private float marginTop = 50f;
        private float marginBottom = 50f;
        private boolean hasHeader = false;
        private boolean hasFooter = false;

        public Builder withMargins(float left, float right, float top, float bottom) {
            this.marginLeft = left;
            this.marginRight = right;
            this.marginTop = top;
            this.marginBottom = bottom;
            return this;
        }

        public Builder withHeader(boolean hasHeader) {
            this.hasHeader = hasHeader;
            return this;
        }

        public Builder withFooter(boolean hasFooter) {
            this.hasFooter = hasFooter;
            return this;
        }

        public SafeArea build() {
            return new SafeArea(this);
        }
    }
}
