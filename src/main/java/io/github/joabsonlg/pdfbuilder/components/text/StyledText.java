package io.github.joabsonlg.pdfbuilder.components.text;

/**
 * Representa um trecho de texto com seu estilo associado.
 */
public class StyledText {
    private final String text;
    private final TextStyle style;

    public StyledText(String text, TextStyle style) {
        this.text = text;
        this.style = style;
    }

    public String getText() { return text; }
    public TextStyle getStyle() { return style; }
}
