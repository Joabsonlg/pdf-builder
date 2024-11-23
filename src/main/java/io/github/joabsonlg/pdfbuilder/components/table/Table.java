package io.github.joabsonlg.pdfbuilder.components.table;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente para renderização de tabelas em documentos PDF.
 * Suporta células com texto simples, alinhamento e cores de fundo.
 */
public final class Table {
    private final List<List<String>> data;
    private final float[] columnWidths;
    private final float rowHeight;
    private final PDFont font;
    private final float fontSize;
    private final Color textColor;
    private final Color borderColor;
    private final float borderWidth;
    private final boolean drawHeader;
    private final Color headerBackgroundColor;
    private final Color headerTextColor;

    private Table(Builder builder) {
        this.data = builder.data;
        this.columnWidths = builder.columnWidths;
        this.rowHeight = builder.rowHeight;
        this.font = builder.font;
        this.fontSize = builder.fontSize;
        this.textColor = builder.textColor;
        this.borderColor = builder.borderColor;
        this.borderWidth = builder.borderWidth;
        this.drawHeader = builder.drawHeader;
        this.headerBackgroundColor = builder.headerBackgroundColor;
        this.headerTextColor = builder.headerTextColor;
    }

    public float calculateHeight() {
        float totalHeight = 0;

        if (drawHeader) {
            totalHeight += rowHeight;
        }

        totalHeight += data.size() * rowHeight;

        if (borderWidth > 0) {
            totalHeight += (data.size() + (drawHeader ? 1 : 0)) * borderWidth;
        }

        return totalHeight;
    }


    /**
     * Renderiza a tabela no PDPageContentStream.
     */
    public float render(PDPageContentStream contentStream, float x, float y, float availableWidth) throws IOException {
        float currentY = y;
        float tableWidth = 0;
        for (float columnWidth : columnWidths) {
            tableWidth += columnWidth;
        }

        // Ajusta as larguras das colunas se necessário
        float[] adjustedColumnWidths = columnWidths.clone();
        if (tableWidth > availableWidth) {
            float scaleFactor = availableWidth / tableWidth;
            for (int i = 0; i < adjustedColumnWidths.length; i++) {
                adjustedColumnWidths[i] *= scaleFactor;
            }
        }

        // Desenha o cabeçalho
        if (!data.isEmpty() && drawHeader) {
            List<String> headerRow = data.get(0);
            currentY = drawRow(contentStream, headerRow, x, currentY, true, adjustedColumnWidths);
        }

        // Desenha as linhas de dados
        for (int i = drawHeader ? 1 : 0; i < data.size(); i++) {
            List<String> row = data.get(i);
            currentY = drawRow(contentStream, row, x, currentY, false, adjustedColumnWidths);
        }

        return currentY;
    }

    private float drawRow(PDPageContentStream contentStream, List<String> row, float x, float y, boolean isHeader, float[] columnWidths) throws IOException {
        float currentX;
        Color bgColor = isHeader ? headerBackgroundColor : null;
        Color txtColor = isHeader ? headerTextColor : textColor;

        // Calcula a largura total da tabela
        float tableWidth = 0;
        for (float width : columnWidths) {
            tableWidth += width;
        }

        // Primeiro, calcula a altura necessária para a linha
        float maxTextHeight = 0;
        List<List<String>> wrappedTexts = new ArrayList<>();

        for (int i = 0; i < row.size() && i < columnWidths.length; i++) {
            String cellText = row.get(i);
            float columnWidth = columnWidths[i];
            float maxWidth = columnWidth - 10; // 5 pixels de padding de cada lado

            List<String> lines = wrapText(cellText, font, fontSize, maxWidth);
            wrappedTexts.add(lines);

            float textHeight = lines.size() * fontSize;
            maxTextHeight = Math.max(maxTextHeight, textHeight);
        }

        // Adiciona padding vertical
        float actualRowHeight = Math.max(rowHeight, maxTextHeight + 20); // 10 pixels de padding em cima e embaixo

        // Desenha o fundo da linha se necessário
        if (bgColor != null) {
            contentStream.setNonStrokingColor(bgColor);
            contentStream.addRect(x, y - actualRowHeight, tableWidth, actualRowHeight);
            contentStream.fill();
        }

        // Desenha as bordas e o texto de cada célula
        currentX = x;
        for (int i = 0; i < row.size() && i < columnWidths.length; i++) {
            float columnWidth = columnWidths[i];
            List<String> lines = wrappedTexts.get(i);

            // Desenha a borda da célula
            contentStream.setStrokingColor(borderColor);
            contentStream.setLineWidth(borderWidth);
            contentStream.addRect(currentX, y - actualRowHeight, columnWidth, actualRowHeight);
            contentStream.stroke();

            // Calcula a altura total do texto
            float textHeight = lines.size() * fontSize;

            // Calcula a posição Y inicial para centralizar verticalmente todas as linhas
            float startY = y - actualRowHeight + (actualRowHeight - textHeight) / 2;

            // Desenha cada linha do texto
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                String line = lines.get(lineIndex);

                // Calcula a posição X para centralizar a linha horizontalmente
                float textWidth = font.getStringWidth(line) / 1000 * fontSize;
                float textX = currentX + (columnWidth - textWidth) / 2;
                float textY = startY + (lines.size() - 1 - lineIndex) * fontSize;

                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(txtColor);
                contentStream.newLineAtOffset(textX, textY);
                contentStream.showText(line);
                contentStream.endText();
            }

            currentX += columnWidth;
        }

        return y - actualRowHeight;
    }

    /**
     * Quebra o texto em linhas que cabem dentro da largura máxima especificada.
     */
    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // Testa se a palavra sozinha é maior que a largura máxima
            float wordWidth = font.getStringWidth(word) / 1000 * fontSize;
            if (wordWidth > maxWidth) {
                // Se a linha atual não estiver vazia, adiciona ela primeiro
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder();
                }
                // Quebra a palavra em caracteres
                for (int i = 0; i < word.length(); i++) {
                    String part = word.substring(0, i + 1);
                    float partWidth = font.getStringWidth(part) / 1000 * fontSize;
                    if (partWidth > maxWidth) {
                        if (i > 0) {
                            lines.add(word.substring(0, i));
                            word = word.substring(i);
                            i = 0;
                        }
                    }
                }
                if (word.length() > 0) {
                    lines.add(word);
                }
                continue;
            }

            // Testa se adicionar a palavra vai exceder a largura máxima
            String testLine = currentLine.toString() + (currentLine.length() > 0 ? " " : "") + word;
            float lineWidth = font.getStringWidth(testLine) / 1000 * fontSize;

            if (lineWidth <= maxWidth) {
                currentLine.append(currentLine.length() > 0 ? " " : "").append(word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder();
                }
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<List<String>> data = new ArrayList<>();
        private float[] columnWidths = new float[0];
        private float rowHeight = 20f;
        private PDFont font;
        private float fontSize = 12f;
        private Color textColor = Color.BLACK;
        private Color borderColor = Color.BLACK;
        private float borderWidth = 0.5f;
        private boolean drawHeader = true;
        private Color headerBackgroundColor = new Color(240, 240, 240);
        private Color headerTextColor = Color.BLACK;

        private Builder() {
            this.font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        }

        /**
         * Define os dados da tabela.
         * A primeira linha será tratada como cabeçalho se drawHeader for true.
         */
        public Builder withData(List<List<String>> data) {
            this.data = new ArrayList<>(data);
            return this;
        }

        /**
         * Define as larguras das colunas em pontos.
         */
        public Builder withColumnWidths(float... widths) {
            this.columnWidths = widths.clone();
            return this;
        }

        /**
         * Define a altura das linhas em pontos.
         */
        public Builder withRowHeight(float height) {
            this.rowHeight = height;
            return this;
        }

        /**
         * Define a fonte do texto.
         */
        public Builder withFont(PDFont font) {
            this.font = font;
            return this;
        }

        /**
         * Define o tamanho da fonte.
         */
        public Builder withFontSize(float size) {
            this.fontSize = size;
            return this;
        }

        /**
         * Define a cor do texto.
         */
        public Builder withTextColor(Color color) {
            this.textColor = color;
            return this;
        }

        /**
         * Define a cor da borda.
         */
        public Builder withBorderColor(Color color) {
            this.borderColor = color;
            return this;
        }

        /**
         * Define a espessura da borda.
         */
        public Builder withBorderWidth(float width) {
            this.borderWidth = width;
            return this;
        }

        /**
         * Define se deve desenhar a primeira linha como cabeçalho.
         */
        public Builder withHeader(boolean drawHeader) {
            this.drawHeader = drawHeader;
            return this;
        }

        /**
         * Define a cor de fundo do cabeçalho.
         */
        public Builder withHeaderBackgroundColor(Color color) {
            this.headerBackgroundColor = color;
            return this;
        }

        /**
         * Define a cor do texto do cabeçalho.
         */
        public Builder withHeaderTextColor(Color color) {
            this.headerTextColor = color;
            return this;
        }

        public Table build() {
            return new Table(this);
        }
    }
}
