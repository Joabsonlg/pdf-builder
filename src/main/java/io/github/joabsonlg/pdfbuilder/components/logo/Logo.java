package io.github.joabsonlg.pdfbuilder.components.logo;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

/**
 * Componente para renderizar o logo do documento.
 */
public final class Logo {
    private final String title;
    private final LogoStyle style;
    private final PDImageXObject leftImage;
    private final PDImageXObject rightImage;

    private Logo(String title, LogoStyle style, PDImageXObject leftImage, PDImageXObject rightImage) {
        this.title = title;
        this.style = style;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    /**
     * Renderiza o logo na página.
     */
    public void render(PDPageContentStream contentStream, float pageWidth, float y, float marginLeft, float marginRight) throws IOException {
        float contentWidth = pageWidth - marginLeft - marginRight;
        float textY = y;
        float lineY = y - (style.getFontSize() / 2) - 5;

        // Configura a fonte e cor
        contentStream.setFont(style.getFont(), style.getFontSize());
        contentStream.setNonStrokingColor(style.getColor());

        // Calcula a largura do texto
        float textWidth = style.getFont().getStringWidth(title) / 1000 * style.getFontSize();

        // Calcula o espaço disponível para o texto considerando as imagens
        float availableWidth = contentWidth;
        float imageSpace = 0;
        if (leftImage != null || rightImage != null) {
            imageSpace = style.getImageWidth() + (2 * style.getImageMargin());
            availableWidth -= (2 * imageSpace); // Espaço para ambas as imagens
        }

        // Renderiza a imagem da esquerda
        if (leftImage != null) {
            float imageX = marginLeft + style.getImageMargin();
            float imageY = y - style.getImageHeight() + (style.getFontSize() / 2);

            // Calcula as dimensões da imagem mantendo a proporção se necessário
            float imageWidth = style.getImageWidth();
            float imageHeight = style.getImageHeight();

            if (style.isMaintainAspectRatio()) {
                float aspectRatio = (float) leftImage.getHeight() / leftImage.getWidth();
                if (imageWidth > 0) {
                    imageHeight = imageWidth * aspectRatio;
                } else if (imageHeight > 0) {
                    imageWidth = imageHeight / aspectRatio;
                }
            }

            contentStream.drawImage(leftImage, imageX, imageY, imageWidth, imageHeight);
        }

        // Renderiza o texto centralizado
        float textX = marginLeft + (contentWidth - textWidth) / 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(textX, textY);
        contentStream.showText(title);
        contentStream.endText();

        // Renderiza a imagem da direita
        if (rightImage != null) {
            float imageX = pageWidth - marginRight - style.getImageWidth() - style.getImageMargin();
            float imageY = y - style.getImageHeight() + (style.getFontSize() / 2);

            // Calcula as dimensões da imagem mantendo a proporção se necessário
            float imageWidth = style.getImageWidth();
            float imageHeight = style.getImageHeight();

            if (style.isMaintainAspectRatio()) {
                float aspectRatio = (float) rightImage.getHeight() / rightImage.getWidth();
                if (imageWidth > 0) {
                    imageHeight = imageWidth * aspectRatio;
                } else if (imageHeight > 0) {
                    imageWidth = imageHeight / aspectRatio;
                }
            }

            contentStream.drawImage(rightImage, imageX, imageY, imageWidth, imageHeight);
        }

        // Desenha a linha separadora
        if (style.hasLine()) {
            contentStream.setStrokingColor(style.getLineColor());
            contentStream.setLineWidth(style.getLineWidth());
            contentStream.moveTo(marginLeft, lineY);
            contentStream.lineTo(pageWidth - marginRight, lineY);
            contentStream.stroke();
        }
    }

    /**
     * Retorna a altura total do logo, incluindo margens.
     */
    public float getTotalHeight() {
        float baseHeight = Math.max(style.getFontSize(), style.getImageHeight());
        return baseHeight + style.getMarginBottom() + (style.hasLine() ? style.getLineWidth() + 10 : 0);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private LogoStyle style;
        private PDImageXObject leftImage;
        private PDImageXObject rightImage;

        private Builder() {}

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withStyle(LogoStyle style) {
            this.style = style;
            return this;
        }

        public Builder withLeftImage(PDImageXObject leftImage) {
            this.leftImage = leftImage;
            return this;
        }

        public Builder withRightImage(PDImageXObject rightImage) {
            this.rightImage = rightImage;
            return this;
        }

        public Logo build() {
            if (style == null) {
                style = LogoStyle.builder().build();
            }
            return new Logo(title, style, leftImage, rightImage);
        }
    }
}
