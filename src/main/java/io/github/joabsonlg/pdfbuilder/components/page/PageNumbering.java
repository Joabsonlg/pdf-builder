package io.github.joabsonlg.pdfbuilder.components.page;

import io.github.joabsonlg.pdfbuilder.components.text.TextAlignment;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.IOException;

/**
 * Componente para adicionar numeração de páginas em documentos PDF.
 * Suporta diferentes formatos e posicionamentos.
 */
public class PageNumbering {
    private static final Logger logger = LoggerFactory.getLogger(PageNumbering.class);

    private final Format format;
    private final Position position;
    private final PDFont font;
    private final float fontSize;
    private final Color color;
    private final TextAlignment alignment;
    private final float marginX;
    private final float marginY;

    private PageNumbering(Builder builder) {
        this.format = builder.format;
        this.position = builder.position;
        this.font = builder.font;
        this.fontSize = builder.fontSize;
        this.color = builder.color;
        this.alignment = builder.alignment;
        this.marginX = builder.marginX;
        this.marginY = builder.marginY;
    }

    /**
     * Renderiza o número da página no documento.
     *
     * @param contentStream Stream do conteúdo da página
     * @param pageWidth Largura da página
     * @param pageHeight Altura da página
     * @param pageNumber Número da página atual
     * @param totalPages Total de páginas no documento
     */
    public void render(PDPageContentStream contentStream, float pageWidth, float pageHeight, int pageNumber, int totalPages) throws IOException {
        String text = formatPageNumber(pageNumber, totalPages);
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float x = calculateX(pageWidth, textWidth);
        float y = calculateY(pageHeight);

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    public String formatPageNumber(int pageNumber, int totalPages) {
        return switch (format) {
            case SIMPLE -> String.valueOf(pageNumber);
            case WITH_TOTAL -> pageNumber + " de " + totalPages;
            case DASH_TOTAL -> pageNumber + " - " + totalPages;
            case PARENTHESES_TOTAL -> pageNumber + " (" + totalPages + ")";
        };
    }

    private float calculateX(float pageWidth, float textWidth) {
        return switch (alignment) {
            case LEFT -> marginX;
            case CENTER -> (pageWidth - textWidth) / 2;
            case RIGHT -> pageWidth - textWidth - marginX;
            case JUSTIFIED -> marginX; // Para numeração de página, JUSTIFIED é igual a LEFT
        };
    }

    private float calculateY(float pageHeight) {
        return switch (position) {
            case TOP -> pageHeight - marginY;
            case BOTTOM -> marginY + fontSize; // Adiciona altura da fonte para evitar sobreposição
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Format format = Format.SIMPLE;
        private Position position = Position.BOTTOM;
        private PDFont font;
        private float fontSize = 10f;
        private Color color = Color.BLACK;
        private TextAlignment alignment = TextAlignment.CENTER;
        private float marginX = 50f;
        private float marginY = 30f;

        public Builder withFormat(Format format) {
            this.format = format;
            return this;
        }

        public Builder withPosition(Position position) {
            this.position = position;
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

        public Builder withAlignment(TextAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public Builder withMarginX(float marginX) {
            this.marginX = marginX;
            return this;
        }

        public Builder withMarginY(float marginY) {
            this.marginY = marginY;
            return this;
        }

        public PageNumbering build() {
            if (font == null) {
                throw new IllegalStateException("Font must be set");
            }
            return new PageNumbering(this);
        }
    }

    /**
     * Formato da numeração de página.
     */
    public enum Format {
        /** Apenas o número da página (ex: "1") */
        SIMPLE,
        /** Número da página com total (ex: "1 de 10") */
        WITH_TOTAL,
        /** Número da página com traço e total (ex: "1 - 10") */
        DASH_TOTAL,
        /** Número da página com total entre parênteses (ex: "1 (10)") */
        PARENTHESES_TOTAL
    }

    /**
     * Posição da numeração na página.
     */
    public enum Position {
        /** No topo da página */
        TOP,
        /** No rodapé da página */
        BOTTOM
    }
}
