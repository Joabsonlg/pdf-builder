package io.github.joabsonlg.pdfbuilder.components.list;

import io.github.joabsonlg.pdfbuilder.components.text.StyledText;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Componente para renderizar listas ordenadas e não ordenadas em documentos PDF.
 * Suporta subitens e numeração hierárquica.
 */
public final class List {
    private static final Logger LOGGER = LoggerFactory.getLogger(List.class);

    private final java.util.List<ListItem> items;
    private final boolean ordered;
    private final PDFont font;
    private final float fontSize;
    private final Color textColor;
    private final float indentation;
    private final float lineSpacing;
    private final String bulletCharacter;
    private final float bulletSpacing;
    private final int level;

    private List(Builder builder) {
        this.items = builder.items;
        this.ordered = builder.ordered;
        this.font = builder.font;
        this.fontSize = builder.fontSize;
        this.textColor = builder.textColor;
        this.indentation = builder.indentation;
        this.lineSpacing = builder.lineSpacing;
        this.bulletCharacter = builder.bulletCharacter;
        this.bulletSpacing = builder.bulletSpacing;
        this.level = builder.level;
    }

    /**
     * Renderiza a lista no PDPageContentStream.
     */
    public float render(PDPageContentStream contentStream, float x, float y, float availableWidth) throws IOException {
        float currentY = y;
        float baseIndentation = indentation * level;
        float textX = x + baseIndentation + bulletSpacing;  // Ajustado para incluir bulletSpacing
        float bulletX = x + baseIndentation;

        for (int i = 0; i < items.size(); i++) {
            ListItem item = items.get(i);
            String bullet = getBullet(i + 1, item.getNumber());

            // Desenha o marcador (bullet) ou número
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(textColor);
            contentStream.newLineAtOffset(bulletX, currentY);
            contentStream.showText(bullet);
            contentStream.endText();

            // Calcula a largura disponível para o texto do item
            float textWidth = availableWidth - (baseIndentation + bulletSpacing);

            // Quebra o texto em linhas se necessário
            java.util.List<java.util.List<StyledText>> lines = wrapStyledText(item.getStyledTexts(), textWidth);

            // Desenha cada linha do texto
            float lineY = currentY;
            for (java.util.List<StyledText> line : lines) {
                float currentX = textX;
                for (StyledText styledText : line) {
                    TextStyle style = styledText.getStyle();
                    String text = styledText.getText();

                    contentStream.beginText();
                    contentStream.setFont(style.getFont(), style.getFontSize());
                    contentStream.setNonStrokingColor(style.getColor());
                    contentStream.newLineAtOffset(currentX, lineY);
                    contentStream.showText(text);
                    contentStream.endText();

                    currentX += style.getFont().getStringWidth(text) / 1000 * style.getFontSize();
                }

                lineY -= fontSize + lineSpacing;
            }

            // Renderiza subitens se houver
            if (item.hasSubItems()) {
                List subList = List.builder()
                        .withFont(font)
                        .withFontSize(fontSize)
                        .withTextColor(textColor)
                        .withIndentation(indentation)
                        .withLineSpacing(lineSpacing)
                        .withBulletCharacter(bulletCharacter)
                        .withBulletSpacing(bulletSpacing)
                        .withLevel(level + 1)
                        .ordered(ordered)
                        .withListItems(item.getSubItems())
                        .build();

                lineY = subList.render(contentStream, x, lineY, availableWidth);
            }

            // Atualiza a posição Y para o próximo item
            currentY = lineY;
        }

        return currentY;
    }

    private String getBullet(int index, String number) {
        if (!ordered) {
            return bulletCharacter;
        }

        if (number != null) {
            return number;
        }

        return index + ".";
    }

    /**
     * Quebra o texto estilizado em linhas que cabem dentro da largura máxima especificada.
     */
    private java.util.List<java.util.List<StyledText>> wrapStyledText(java.util.List<StyledText> styledTexts, float maxWidth) throws IOException {
        java.util.List<java.util.List<StyledText>> lines = new ArrayList<>();
        java.util.List<StyledText> currentLine = new ArrayList<>();
        float currentWidth = 0;

        for (StyledText styledText : styledTexts) {
            String[] words = styledText.getText().split(" ");
            TextStyle style = styledText.getStyle();

            for (String word : words) {
                float wordWidth = style.getFont().getStringWidth(word) / 1000 * style.getFontSize();
                float spaceWidth = style.getFont().getStringWidth(" ") / 1000 * style.getFontSize();

                if (currentWidth + wordWidth > maxWidth) {
                    if (!currentLine.isEmpty()) {
                        lines.add(new ArrayList<>(currentLine));
                        currentLine.clear();
                        currentWidth = 0;
                    }
                }

                if (currentWidth > 0) {
                    currentWidth += spaceWidth;
                }

                currentLine.add(new StyledText(currentLine.isEmpty() ? word : " " + word, style));
                currentWidth += wordWidth;
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }

        return lines;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private java.util.List<ListItem> items = new ArrayList<>();
        private boolean ordered = false;
        private PDFont font;
        private float fontSize = 12f;
        private Color textColor = Color.BLACK;
        private float indentation = 20f;
        private float lineSpacing = 5f;
        private String bulletCharacter = "•";
        private float bulletSpacing = 10f;
        private int level = 1;

        public Builder withItems(java.util.List<String> items) {
            this.items = new ArrayList<>();
            for (String item : items) {
                this.items.add(new ListItem(item, font, fontSize, textColor));
            }
            return this;
        }

        public Builder withListItems(java.util.List<ListItem> items) {
            this.items = items;
            return this;
        }

        public Builder ordered(boolean ordered) {
            this.ordered = ordered;
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

        public Builder withTextColor(Color textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder withIndentation(float indentation) {
            this.indentation = indentation;
            return this;
        }

        public Builder withLineSpacing(float lineSpacing) {
            this.lineSpacing = lineSpacing;
            return this;
        }

        public Builder withBulletCharacter(String bulletCharacter) {
            this.bulletCharacter = bulletCharacter;
            return this;
        }

        public Builder withBulletSpacing(float bulletSpacing) {
            this.bulletSpacing = bulletSpacing;
            return this;
        }

        public Builder withLevel(int level) {
            this.level = level;
            return this;
        }

        public List build() {
            if (font == null) {
                throw new IllegalStateException("Font must be set");
            }
            if (items.isEmpty()) {
                throw new IllegalStateException("List must have at least one item");
            }
            return new List(this);
        }
    }

    public static class ListItem {
        private java.util.List<StyledText> styledTexts;
        private String number;
        private java.util.List<ListItem> subItems;

        public ListItem(String text, PDFont font, float fontSize, Color color) {
            this.styledTexts = new ArrayList<>();
            this.styledTexts.add(new StyledText(text, TextStyle.builder()
                    .withFont(font)
                    .withFontSize(fontSize)
                    .withColor(color)
                    .build()));
            this.subItems = new ArrayList<>();
        }

        public ListItem(java.util.List<StyledText> styledTexts) {
            this.styledTexts = styledTexts;
            this.subItems = new ArrayList<>();
        }

        public java.util.List<StyledText> getStyledTexts() {
            return styledTexts;
        }

        public void setStyledTexts(java.util.List<StyledText> styledTexts) {
            this.styledTexts = styledTexts;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public java.util.List<ListItem> getSubItems() {
            return subItems;
        }

        public void setSubItems(java.util.List<ListItem> subItems) {
            this.subItems = subItems;
        }

        public void addSubItem(ListItem item) {
            this.subItems.add(item);
        }

        public boolean hasSubItems() {
            return !subItems.isEmpty();
        }
    }
}
