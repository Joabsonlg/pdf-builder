package io.github.joabsonlg.pdfbuilder.components.text;

import io.github.joabsonlg.pdfbuilder.core.SafeArea;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente para renderização de parágrafos com alinhamento e formatação rica.
 */
public final class Paragraph {
    private final List<StyledText> styledTexts;
    private final TextAlignment alignment;
    private final float lineSpacing;

    private Paragraph(Builder builder) {
        this.styledTexts = builder.styledTexts;
        this.alignment = builder.alignment;
        this.lineSpacing = builder.lineSpacing;
    }

    /**
     * Calcula a altura total necessária para renderizar o parágrafo.
     */
    public float calculateHeight() throws IOException {
        List<List<StyledText>> lines = breakTextIntoLines(Float.MAX_VALUE);
        return lines.size() * getHeight();
    }

    /**
     * Renderiza o parágrafo no PDPageContentStream respeitando a largura máxima e alinhamento.
     */
    public float render(PDPageContentStream contentStream, float x, float y, float maxWidth) throws IOException {
        List<List<StyledText>> lines = breakTextIntoLines(maxWidth);
        float currentY = y;

        for (List<StyledText> line : lines) {
            float startX = calculateStartX(line, x, maxWidth);
            float wordSpacing = calculateWordSpacing(line, maxWidth);

            float currentX = startX;
            for (int i = 0; i < line.size(); i++) {
                StyledText styledText = line.get(i);
                TextStyle style = styledText.getStyle();
                String text = styledText.getText();

                // Configura estilo
                contentStream.setFont(style.getFont(), style.getFontSize());
                contentStream.setNonStrokingColor(style.getColor());

                // Renderiza texto
                contentStream.beginText();
                contentStream.newLineAtOffset(currentX, currentY);
                contentStream.showText(text);
                contentStream.endText();

                // Sublinhado se necessário
                if (style.isUnderline()) {
                    float textWidth = getStringWidth(text, style.getFont(), style.getFontSize());
                    contentStream.setLineWidth(style.getUnderlineThickness());
                    contentStream.moveTo(currentX, currentY + style.getUnderlineOffset());
                    contentStream.lineTo(currentX + textWidth, currentY + style.getUnderlineOffset());
                    contentStream.stroke();
                }

                // Atualiza posição X
                float advance = getStringWidth(text, style.getFont(), style.getFontSize());
                currentX += advance;

                // Adiciona espaço entre palavras
                if (i < line.size() - 1) {
                    currentX += (alignment == TextAlignment.JUSTIFIED ? wordSpacing : getSpaceWidth(style.getFont(), style.getFontSize()));
                }
            }
            currentY -= getHeight();
        }

        return currentY;
    }

    private float calculateWordSpacing(List<StyledText> line, float maxWidth) throws IOException {
        if (alignment != TextAlignment.JUSTIFIED || line.size() <= 1) {
            return getSpaceWidth(line.get(0).getStyle().getFont(), line.get(0).getStyle().getFontSize());
        }

        float totalTextWidth = 0;
        int spaces = line.size() - 1;

        for (StyledText styledText : line) {
            totalTextWidth += getStringWidth(styledText.getText(),
                    styledText.getStyle().getFont(),
                    styledText.getStyle().getFontSize());
        }

        float totalSpaceWidth = maxWidth - totalTextWidth;

        return totalSpaceWidth / spaces;
    }

    private float calculateStartX(List<StyledText> line, float baseX, float maxWidth) throws IOException {
        float lineWidth = 0;
        float spaceWidth = getSpaceWidth(line.get(0).getStyle().getFont(), line.get(0).getStyle().getFontSize());

        for (int i = 0; i < line.size(); i++) {
            StyledText styledText = line.get(i);
            lineWidth += getStringWidth(styledText.getText(),
                    styledText.getStyle().getFont(),
                    styledText.getStyle().getFontSize());

            if (i < line.size() - 1) {
                lineWidth += spaceWidth;
            }
        }

        return switch (alignment) {
            case LEFT -> baseX;
            case CENTER -> baseX + (maxWidth - lineWidth) / 2;
            case RIGHT -> baseX + (maxWidth - lineWidth);
            case JUSTIFIED -> baseX;
        };
    }

    private List<List<StyledText>> breakTextIntoLines(float maxWidth) throws IOException {
        List<List<StyledText>> lines = new ArrayList<>();
        List<StyledText> currentLine = new ArrayList<>();
        float currentWidth = 0;

        for (StyledText styledText : styledTexts) {
            String[] words = styledText.getText().split("\\s+");
            TextStyle style = styledText.getStyle();

            for (String word : words) {
                float wordWidth = getStringWidth(word, style.getFont(), style.getFontSize());
                float spaceWidth = getSpaceWidth(style.getFont(), style.getFontSize());

                if (currentWidth + wordWidth <= maxWidth || currentLine.isEmpty()) {
                    currentLine.add(new StyledText(word, style));
                    currentWidth += wordWidth + spaceWidth;
                } else {
                    lines.add(new ArrayList<>(currentLine));
                    currentLine.clear();
                    currentLine.add(new StyledText(word, style));
                    currentWidth = wordWidth + spaceWidth;
                }
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }

        return lines;
    }

    private float getStringWidth(String str, PDFont font, float fontSize) throws IOException {
        return font.getStringWidth(str) / 1000 * fontSize;
    }

    private float getSpaceWidth(PDFont font, float fontSize) throws IOException {
        return font.getSpaceWidth() / 1000 * fontSize;
    }

    public float getHeight() {
        if (styledTexts.isEmpty()) {
            return 0;
        }
        return styledTexts.get(0).getStyle().getFontSize() * lineSpacing;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<StyledText> styledTexts = new ArrayList<>();
        private TextAlignment alignment = TextAlignment.LEFT;
        private float lineSpacing = 1.2f;

        public Builder addStyledText(String text, TextStyle style) {
            this.styledTexts.add(new StyledText(text, style));
            return this;
        }

        public Builder withAlignment(TextAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public Builder withLineSpacing(float lineSpacing) {
            this.lineSpacing = lineSpacing;
            return this;
        }

        public Paragraph build() {
            if (styledTexts.isEmpty()) {
                throw new IllegalStateException("Paragraph must contain at least one text segment");
            }
            return new Paragraph(this);
        }
    }
}
