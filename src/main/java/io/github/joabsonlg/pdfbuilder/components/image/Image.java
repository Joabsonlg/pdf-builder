package io.github.joabsonlg.pdfbuilder.components.image;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.awt.Dimension;

/**
 * Componente para renderização de imagens em documentos PDF.
 * Suporta redimensionamento, rotação e diferentes formatos de imagem.
 */
public class Image {
    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    private final PDImageXObject image;
    private final float width;
    private final float height;
    private final float rotation;
    private final float quality;
    private final Alignment alignment;
    private final String caption;
    private final float captionFontSize;

    private Image(Builder builder) {
        this.image = builder.image;
        this.width = builder.width;
        this.height = builder.height;
        this.rotation = builder.rotation;
        this.quality = builder.quality;
        this.alignment = builder.alignment;
        this.caption = builder.caption;
        this.captionFontSize = builder.captionFontSize;
    }

    /**
     * Renderiza a imagem no PDPageContentStream.
     */
    public float render(PDPageContentStream contentStream, float x, float y, float availableWidth, float imageWidth) throws IOException {
        // Calcula a posição X baseada no alinhamento
        float xPos = x;
        switch (alignment) {
            case CENTER:
                xPos = x + (availableWidth - imageWidth) / 2;
                break;
            case RIGHT:
                xPos = x + (availableWidth - imageWidth);
                break;
        }

        float imageHeight = (imageWidth / width) * height;

        // Salva o estado gráfico atual
        contentStream.saveGraphicsState();

        // Aplica rotação se necessário
        if (rotation != 0) {
            float centerX = xPos + imageWidth / 2;
            float centerY = y - imageHeight / 2;
            contentStream.transform(
                Matrix.getRotateInstance(Math.toRadians(rotation), centerX, centerY)
            );
        }

        // Renderiza a imagem
        contentStream.drawImage(image, xPos, y - imageHeight, imageWidth, imageHeight);

        // Restaura o estado gráfico
        contentStream.restoreGraphicsState();

        float newY = y - imageHeight;

        // Adiciona legenda se existir
        if (caption != null && !caption.isEmpty()) {
            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            contentStream.beginText();
            contentStream.setFont(font, captionFontSize);
            
            float captionWidth = font.getStringWidth(caption) / 1000 * captionFontSize;
            float captionX;
            
            // Alinha a legenda com a imagem
            switch (alignment) {
                case CENTER:
                    captionX = xPos + (imageWidth - captionWidth) / 2;
                    break;
                case RIGHT:
                    captionX = xPos + imageWidth - captionWidth;
                    break;
                default: // LEFT
                    captionX = xPos;
                    break;
            }
            
            contentStream.newLineAtOffset(captionX, newY - captionFontSize - 5);
            contentStream.showText(caption);
            contentStream.endText();
            
            newY -= (captionFontSize + 10); // Espaço extra após a legenda
        }

        return newY;
    }

    /**
     * Retorna as dimensões atuais da imagem.
     */
    public Dimension getDimensions() {
        return new Dimension((int)width, (int)height);
    }

    public static Builder builder(PDDocument document, File imageFile) throws IOException {
        return new Builder(document, imageFile);
    }

    public static Builder builder(PDDocument document, String imagePath) throws IOException {
        return new Builder(document, new File(imagePath));
    }

    public static class Builder {
        private PDImageXObject image;
        private float width;
        private float height;
        private float rotation = 0;
        private float quality = 0.9f;
        private Alignment alignment = Alignment.LEFT;
        private String caption = null;
        private float captionFontSize = 10f;

        private Builder(PDDocument document, File imageFile) throws IOException {
            this.image = PDImageXObject.createFromFileByContent(imageFile, document);
            this.width = image.getWidth();
            this.height = image.getHeight();
        }

        /**
         * Define a largura da imagem. A altura será ajustada proporcionalmente.
         */
        public Builder withWidth(float width) {
            float ratio = width / this.width;
            this.width = width;
            this.height *= ratio;
            return this;
        }

        /**
         * Define a altura da imagem. A largura será ajustada proporcionalmente.
         */
        public Builder withHeight(float height) {
            float ratio = height / this.height;
            this.height = height;
            this.width *= ratio;
            return this;
        }

        /**
         * Define o ângulo de rotação da imagem em graus.
         */
        public Builder withRotation(float degrees) {
            this.rotation = degrees;
            return this;
        }

        /**
         * Define a qualidade da imagem (0.0 - 1.0).
         */
        public Builder withQuality(float quality) {
            this.quality = Math.max(0.0f, Math.min(1.0f, quality));
            return this;
        }

        /**
         * Define o alinhamento horizontal da imagem.
         */
        public Builder withAlignment(Alignment alignment) {
            this.alignment = alignment;
            return this;
        }

        /**
         * Define a legenda da imagem.
         */
        public Builder withCaption(String caption) {
            this.caption = caption;
            return this;
        }

        /**
         * Define o tamanho da fonte da legenda.
         */
        public Builder withCaptionFontSize(float fontSize) {
            this.captionFontSize = fontSize;
            return this;
        }

        public Image build() {
            return new Image(this);
        }
    }
}
