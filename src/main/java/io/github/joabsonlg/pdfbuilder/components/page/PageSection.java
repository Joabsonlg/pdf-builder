package io.github.joabsonlg.pdfbuilder.components.page;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.io.IOException;

/**
 * Componente para renderização de cabeçalhos e rodapés em documentos PDF.
 */
public class PageSection {
    private final String leftText;
    private final String centerText;
    private final String rightText;
    private final PDFont font;
    private final float fontSize;
    private final Color color;
    private final boolean drawLine;
    private final float lineWidth;
    private final Color lineColor;
    private final PageNumbering pageNumbering;

    private PageSection(Builder builder) {
        this.leftText = builder.leftText;
        this.centerText = builder.centerText;
        this.rightText = builder.rightText;
        this.font = builder.font;
        this.fontSize = builder.fontSize;
        this.color = builder.color;
        this.drawLine = builder.drawLine;
        this.lineWidth = builder.lineWidth;
        this.lineColor = builder.lineColor;
        this.pageNumbering = builder.pageNumbering;
    }

    // Getters
    public String getLeftText() {
        return leftText;
    }

    public String getCenterText() {
        return centerText;
    }

    public String getRightText() {
        return rightText;
    }

    public PDFont getFont() {
        return font;
    }

    public float getFontSize() {
        return fontSize;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasLine() {
        return drawLine;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public PageNumbering getPageNumbering() {
        return pageNumbering;
    }

    /**
     * Renderiza o cabeçalho ou rodapé na página.
     * @param contentStream Stream de conteúdo do PDF
     * @param pageWidth Largura da página
     * @param y Posição Y onde renderizar
     * @param marginLeft Margem esquerda
     * @param marginRight Margem direita
     * @param pageNumber Número da página atual (opcional, usado apenas se tiver PageNumbering)
     * @param totalPages Total de páginas (opcional, usado apenas se tiver PageNumbering)
     * @throws IOException em caso de erro na renderização
     */
    public void render(PDPageContentStream contentStream, float pageWidth, float y, float marginLeft, float marginRight,
                      int pageNumber, int totalPages) throws IOException {
        float contentWidth = pageWidth - marginLeft - marginRight;
        float textY = y;
        float lineY = y - (fontSize / 2); // Move a linha para baixo do texto
        
        // Configura a fonte e cor
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);

        // Renderiza o texto à esquerda
        if (leftText != null && !leftText.isEmpty()) {
            contentStream.beginText();
            contentStream.newLineAtOffset(marginLeft, textY);
            contentStream.showText(leftText);
            contentStream.endText();
        }

        // Renderiza o texto centralizado ou a numeração de página
        if (pageNumbering != null) {
            String pageText = pageNumbering.formatPageNumber(pageNumber, totalPages);
            float textWidth = font.getStringWidth(pageText) / 1000 * fontSize;
            float rightX = pageWidth - marginRight - textWidth;
            contentStream.beginText();
            contentStream.newLineAtOffset(rightX, textY);
            contentStream.showText(pageText);
            contentStream.endText();
        } else if (centerText != null && !centerText.isEmpty()) {
            float textWidth = font.getStringWidth(centerText) / 1000 * fontSize;
            float centerX = marginLeft + (contentWidth - textWidth) / 2;
            contentStream.beginText();
            contentStream.newLineAtOffset(centerX, textY);
            contentStream.showText(centerText);
            contentStream.endText();
        }

        // Renderiza o texto à direita (apenas se não tiver PageNumbering)
        if (pageNumbering == null && rightText != null && !rightText.isEmpty()) {
            float textWidth = font.getStringWidth(rightText) / 1000 * fontSize;
            float rightX = pageWidth - marginRight - textWidth;
            contentStream.beginText();
            contentStream.newLineAtOffset(rightX, textY);
            contentStream.showText(rightText);
            contentStream.endText();
        }

        // Desenha a linha separadora por último e abaixo do texto
        if (drawLine) {
            contentStream.setStrokingColor(lineColor);
            contentStream.setLineWidth(lineWidth);
            contentStream.moveTo(marginLeft, lineY);
            contentStream.lineTo(pageWidth - marginRight, lineY);
            contentStream.stroke();
        }
    }

    // Sobrecarga do método render para manter compatibilidade
    public void render(PDPageContentStream contentStream, float pageWidth, float y, float marginLeft, float marginRight) throws IOException {
        render(contentStream, pageWidth, y, marginLeft, marginRight, 0, 0);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String leftText;
        private String centerText;
        private String rightText;
        private PDFont font;
        private float fontSize = 10f;
        private Color color = Color.BLACK;
        private boolean drawLine = true;
        private float lineWidth = 0.5f;
        private Color lineColor = Color.BLACK;
        private PageNumbering pageNumbering;

        private Builder() {
            // Valores padrão
            font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        }

        public Builder withLeftText(String leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder withCenterText(String centerText) {
            this.centerText = centerText;
            return this;
        }

        public Builder withRightText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public Builder withFont(PDFont font) {
            this.font = font;
            return this;
        }

        public Builder withFontSize(float fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder withColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder withDrawLine(boolean drawLine) {
            this.drawLine = drawLine;
            return this;
        }

        public Builder withLineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public Builder withLineColor(Color lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public Builder withPageNumbering(PageNumbering pageNumbering) {
            this.pageNumbering = pageNumbering;
            return this;
        }

        public PageSection build() {
            return new PageSection(this);
        }
    }
}
