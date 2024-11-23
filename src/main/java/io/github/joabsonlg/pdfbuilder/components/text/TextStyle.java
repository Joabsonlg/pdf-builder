package io.github.joabsonlg.pdfbuilder.components.text;

import java.awt.Color;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * Define o estilo de formatação para um trecho de texto.
 */
public class TextStyle {
    private final PDFont font;
    private final float fontSize;
    private final Color color;
    private final boolean underline;
    private final float underlineThickness;
    private final float underlineOffset;

    private TextStyle(Builder builder) {
        this.font = builder.font;
        this.fontSize = builder.fontSize;
        this.color = builder.color;
        this.underline = builder.underline;
        this.underlineThickness = builder.underlineThickness;
        this.underlineOffset = builder.underlineOffset;
    }

    public PDFont getFont() { return font; }
    public float getFontSize() { return fontSize; }
    public Color getColor() { return color; }
    public boolean isUnderline() { return underline; }
    public float getUnderlineThickness() { return underlineThickness; }
    public float getUnderlineOffset() { return underlineOffset; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PDFont font;
        private float fontSize = 12f;
        private Color color = Color.BLACK;
        private boolean underline = false;
        private float underlineThickness = 0.5f;
        private float underlineOffset = -2.5f;

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

        public Builder withUnderline(boolean underline) {
            this.underline = underline;
            return this;
        }

        public Builder withUnderlineThickness(float thickness) {
            this.underlineThickness = thickness;
            return this;
        }

        public Builder withUnderlineOffset(float offset) {
            this.underlineOffset = offset;
            return this;
        }

        public TextStyle build() {
            if (font == null) {
                throw new IllegalStateException("Font must be set");
            }
            return new TextStyle(this);
        }
    }
}
