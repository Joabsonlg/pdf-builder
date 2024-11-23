package io.github.joabsonlg.pdfbuilder.components.text;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.io.IOException;

/**
 * Componente para renderização de títulos e subtítulos.
 * Suporta numeração automática, diferentes níveis e espaçamento personalizado.
 */
public class Heading {
    private final HeadingLevel level;
    private final String text;
    private final TextStyle style;
    private final boolean numbered;
    private final String number;
    private final float spacingBefore;
    private final float spacingAfter;
    private final TextAlignment alignment;

    private Heading(Builder builder) {
        this.level = builder.level;
        this.text = builder.text;
        this.style = builder.style;
        this.numbered = builder.numbered;
        this.number = builder.number;
        this.spacingBefore = builder.level.getSpacingBefore();
        this.spacingAfter = builder.level.getSpacingAfter();
        this.alignment = builder.alignment;
    }

    /**
     * Retorna o nível do título.
     */
    public HeadingLevel getLevel() {
        return level;
    }

    /**
     * Retorna o texto do título.
     */
    public String getText() {
        return text;
    }

    /**
     * Retorna o estilo do título.
     */
    public TextStyle getStyle() {
        return style;
    }

    /**
     * Retorna o alinhamento do título.
     */
    public TextAlignment getAlignment() {
        return alignment;
    }

    /**
     * Renderiza o título no PDPageContentStream.
     */
    public float render(PDPageContentStream contentStream, float x, float y, float maxWidth) throws IOException {
        // Aplica o estilo
        PDFont font = style != null ? style.getFont() : new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        float fontSize = style != null ? style.getFontSize() : level.getFontSize();
        Color color = style != null ? style.getColor() : Color.BLACK;

        // Configura a fonte e cor
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);

        // Prepara o texto completo (incluindo numeração se necessário)
        String fullText = numbered && number != null ? number + " " + text : text;

        // Calcula a largura do texto
        float textWidth = font.getStringWidth(fullText) / 1000 * fontSize;

        // Calcula a posição X baseada no alinhamento
        float startX = x;
        if (alignment == TextAlignment.CENTER) {
            startX = x + (maxWidth - textWidth) / 2;
        } else if (alignment == TextAlignment.RIGHT) {
            startX = x + maxWidth - textWidth;
        }

        // Renderiza o texto
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, y);
        contentStream.showText(fullText);
        contentStream.endText();

        // Retorna a nova posição Y
        return y - spacingAfter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HeadingLevel level = HeadingLevel.H1;
        private String text;
        private TextStyle style;
        private boolean numbered;
        private String number;
        private TextAlignment alignment = TextAlignment.LEFT;

        public Builder withLevel(HeadingLevel level) {
            this.level = level;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withStyle(TextStyle style) {
            this.style = style;
            return this;
        }

        public Builder withNumbering(boolean numbered) {
            this.numbered = numbered;
            return this;
        }

        public Builder withNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder withAlignment(TextAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public Heading build() {
            if (text == null || text.trim().isEmpty()) {
                throw new IllegalStateException("Text must be set");
            }
            if (level == null) {
                throw new IllegalStateException("Level must be set");
            }
            return new Heading(this);
        }
    }
}
