package io.github.joabsonlg.pdfbuilder.components.text;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente para renderização de texto simples.
 */
public final class SimpleText {
    private final String text;
    private final PDFont font;
    private final float fontSize;
    private final float lineSpacing;
    private final Color color;

    private SimpleText(String text, PDFont font, float fontSize, float lineSpacing, Color color) {
        this.text = text;
        this.font = font;
        this.fontSize = fontSize;
        this.lineSpacing = lineSpacing;
        this.color = color;
    }

    /**
     * Renderiza o texto no PDPageContentStream respeitando a largura máxima.
     *
     * @param contentStream Stream de conteúdo do PDF
     * @param x             Posição X inicial
     * @param y             Posição Y inicial
     * @param maxWidth      Largura máxima disponível
     * @return Posição Y final após renderizar todo o texto
     * @throws IOException se houver erro ao renderizar
     */
    public float render(PDPageContentStream contentStream, float x, float y, float maxWidth) throws IOException {
        List<String> lines = breakTextIntoLines(maxWidth);
        float currentY = y;

        contentStream.setFont(font, fontSize);
        if (color != null) {
            contentStream.setNonStrokingColor(color);
        }

        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= getHeight();
        }

        return currentY;
    }

    /**
     * Quebra o texto em linhas respeitando a largura máxima.
     *
     * @param maxWidth Largura máxima disponível
     * @return Lista de linhas
     * @throws IOException se houver erro no cálculo
     */
    private List<String> breakTextIntoLines(float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() > 0
                    ? currentLine + " " + word
                    : word;

            float lineWidth = getStringWidth(testLine);

            if (lineWidth <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Palavra única maior que maxWidth, força quebra
                    lines.add(word);
                }
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    /**
     * Calcula a largura de uma string específica com a fonte atual.
     */
    private float getStringWidth(String str) throws IOException {
        return font.getStringWidth(str) / 1000 * fontSize;
    }

    /**
     * Calcula a altura da linha incluindo o espaçamento.
     *
     * @return Altura em pontos
     */
    public float getHeight() {
        return fontSize * lineSpacing;
    }

    /**
     * Builder para criar instâncias de SimpleText.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String text;
        private PDFont font;
        private float fontSize = 12f;
        private float lineSpacing = 1.2f;
        private Color color = Color.BLACK;

        public Builder withText(String text) {
            this.text = text;
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

        public Builder withLineSpacing(float lineSpacing) {
            this.lineSpacing = lineSpacing;
            return this;
        }

        public Builder withColor(Color color) {
            this.color = color;
            return this;
        }

        public SimpleText build() {
            if (text == null || text.isEmpty()) {
                throw new IllegalStateException("Text cannot be null or empty");
            }
            if (font == null) {
                throw new IllegalStateException("Font must be set");
            }
            return new SimpleText(text, font, fontSize, lineSpacing, color);
        }
    }
}
