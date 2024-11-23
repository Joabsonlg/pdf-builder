package io.github.joabsonlg.pdfbuilder.components.logo;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;

/**
 * Configurações de estilo para o logo do documento.
 */
public final class LogoStyle {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoStyle.class);

    private final PDFont font;
    private final float fontSize;
    private final Color color;
    private final float imageWidth;
    private final float imageHeight;
    private final float imageMargin;
    private final boolean maintainAspectRatio;
    private final float marginBottom;
    private final boolean drawLine;
    private final float lineWidth;
    private final Color lineColor;

    private LogoStyle(Builder builder) {
        this.font = builder.font;
        this.fontSize = builder.fontSize;
        this.color = builder.color;
        this.imageWidth = builder.imageWidth;
        this.imageHeight = builder.imageHeight;
        this.imageMargin = builder.imageMargin;
        this.maintainAspectRatio = builder.maintainAspectRatio;
        this.marginBottom = builder.marginBottom;
        this.drawLine = builder.drawLine;
        this.lineWidth = builder.lineWidth;
        this.lineColor = builder.lineColor;
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

    public float getImageWidth() {
        return imageWidth;
    }

    public float getImageHeight() {
        return imageHeight;
    }

    public float getImageMargin() {
        return imageMargin;
    }

    public boolean isMaintainAspectRatio() {
        return maintainAspectRatio;
    }

    public float getMarginBottom() {
        return marginBottom;
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

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder para criar instâncias de LogoStyle.
     */
    public static class Builder {
        private PDFont font;
        private float fontSize = 12f;
        private Color color = Color.BLACK;
        private float imageWidth = 30f;
        private float imageHeight = 30f;
        private float imageMargin = 10f;
        private boolean maintainAspectRatio = true;
        private float marginBottom = 20f;
        private boolean drawLine = true;
        private float lineWidth = 1f;
        private Color lineColor = new Color(128, 128, 128);

        public Builder() {
            try {
                this.font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            } catch (Exception e) {
                LOGGER.error("Erro ao criar fonte padrão", e);
                throw new RuntimeException("Erro ao criar fonte padrão", e);
            }
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

        public Builder withImageWidth(float imageWidth) {
            this.imageWidth = imageWidth;
            return this;
        }

        public Builder withImageHeight(float imageHeight) {
            this.imageHeight = imageHeight;
            return this;
        }

        public Builder withImageMargin(float imageMargin) {
            this.imageMargin = imageMargin;
            return this;
        }

        public Builder withMaintainAspectRatio(boolean maintainAspectRatio) {
            this.maintainAspectRatio = maintainAspectRatio;
            return this;
        }

        public Builder withMarginBottom(float marginBottom) {
            this.marginBottom = marginBottom;
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

        public LogoStyle build() {
            return new LogoStyle(this);
        }
    }
}
