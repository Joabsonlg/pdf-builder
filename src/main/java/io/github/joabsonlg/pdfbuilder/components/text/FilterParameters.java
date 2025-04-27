package io.github.joabsonlg.pdfbuilder.components.text;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Componente para exibir os parâmetros de filtro utilizados na geração do relatório.
 */
public final class FilterParameters {
    private final Map<String, String> parameters;
    private final PDFont labelFont;
    private final PDFont valueFont;
    private final float fontSize;
    private final Color labelColor;
    private final Color valueColor;
    private final float spacing;

    private FilterParameters(Builder builder) {
        this.parameters = builder.parameters;
        this.labelFont = builder.labelFont;
        this.valueFont = builder.valueFont;
        this.fontSize = builder.fontSize;
        this.labelColor = builder.labelColor;
        this.valueColor = builder.valueColor;
        this.spacing = builder.spacing;
    }

    /**
     * Renderiza os parâmetros de filtro no PDF.
     *
     * @param contentStream Stream de conteúdo
     * @param x Posição X inicial
     * @param y Posição Y inicial
     * @param maxWidth Largura máxima disponível
     * @return Nova posição Y após renderização
     * @throws IOException Em caso de erro de I/O
     */
    public float render(PDPageContentStream contentStream, float x, float y, float maxWidth) throws IOException {
        float currentY = y;
        
        // Renderiza o título
        contentStream.beginText();
        contentStream.setFont(labelFont, fontSize + 2);
        contentStream.setNonStrokingColor(labelColor);
        contentStream.newLineAtOffset(x, currentY);
        contentStream.showText("Filtros Aplicados:");
        contentStream.endText();
        
        currentY -= fontSize + spacing;
        
        // Renderiza cada parâmetro
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String label = entry.getKey() + ": ";
            String value = entry.getValue();
            
            // Renderiza o label
            contentStream.beginText();
            contentStream.setFont(labelFont, fontSize);
            contentStream.setNonStrokingColor(labelColor);
            contentStream.newLineAtOffset(x, currentY);
            contentStream.showText(label);
            contentStream.endText();
            
            // Calcula a posição X para o valor
            float labelWidth = labelFont.getStringWidth(label) / 1000 * fontSize;
            
            // Renderiza o valor
            contentStream.beginText();
            contentStream.setFont(valueFont, fontSize);
            contentStream.setNonStrokingColor(valueColor);
            contentStream.newLineAtOffset(x + labelWidth, currentY);
            contentStream.showText(value);
            contentStream.endText();
            
            // Move para a próxima linha
            currentY -= fontSize + spacing;
        }
        
        // Adiciona um espaço extra após todos os filtros
        currentY -= spacing;
        
        return currentY;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static final class Builder {
        private final Map<String, String> parameters = new LinkedHashMap<>();
        private PDFont labelFont;
        private PDFont valueFont;
        private float fontSize = 10f;
        private Color labelColor = Color.BLACK;
        private Color valueColor = new Color(51, 51, 51);
        private float spacing = 5f;
        
        private Builder() {
            this.labelFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            this.valueFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        }
        
        /**
         * Adiciona um parâmetro ao conjunto de filtros.
         *
         * @param name O nome do filtro
         * @param value O valor do filtro
         * @return this para chamadas encadeadas
         */
        public Builder addParameter(String name, String value) {
            this.parameters.put(name, value != null && !value.trim().isEmpty() ? value : "Todos");
            return this;
        }
        
        /**
         * Define a fonte para os nomes dos filtros.
         *
         * @param font A fonte a ser usada
         * @return this para chamadas encadeadas
         */
        public Builder withLabelFont(PDFont font) {
            this.labelFont = font;
            return this;
        }
        
        /**
         * Define a fonte para os valores dos filtros.
         *
         * @param font A fonte a ser usada
         * @return this para chamadas encadeadas
         */
        public Builder withValueFont(PDFont font) {
            this.valueFont = font;
            return this;
        }
        
        /**
         * Define o tamanho da fonte.
         *
         * @param size O tamanho da fonte
         * @return this para chamadas encadeadas
         */
        public Builder withFontSize(float size) {
            this.fontSize = size;
            return this;
        }
        
        /**
         * Define a cor do texto dos nomes dos filtros.
         *
         * @param color A cor do texto
         * @return this para chamadas encadeadas
         */
        public Builder withLabelColor(Color color) {
            this.labelColor = color;
            return this;
        }
        
        /**
         * Define a cor do texto dos valores dos filtros.
         *
         * @param color A cor do texto
         * @return this para chamadas encadeadas
         */
        public Builder withValueColor(Color color) {
            this.valueColor = color;
            return this;
        }
        
        /**
         * Define o espaçamento entre linhas.
         *
         * @param spacing O espaçamento
         * @return this para chamadas encadeadas
         */
        public Builder withSpacing(float spacing) {
            this.spacing = spacing;
            return this;
        }
        
        public FilterParameters build() {
            return new FilterParameters(this);
        }
    }
}