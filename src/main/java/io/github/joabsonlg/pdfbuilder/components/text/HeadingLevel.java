package io.github.joabsonlg.pdfbuilder.components.text;

/**
 * Define os níveis de título disponíveis e suas características padrão.
 */
public enum HeadingLevel {
    H1(24f, 30f, 20f),
    H2(20f, 25f, 15f),
    H3(16f, 20f, 12f),
    H4(14f, 16f, 10f),
    H5(12f, 14f, 8f),
    H6(11f, 12f, 6f);

    private final float fontSize;
    private final float spacingBefore;
    private final float spacingAfter;

    HeadingLevel(float fontSize, float spacingBefore, float spacingAfter) {
        this.fontSize = fontSize;
        this.spacingBefore = spacingBefore;
        this.spacingAfter = spacingAfter;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getSpacingBefore() {
        return spacingBefore;
    }

    public float getSpacingAfter() {
        return spacingAfter;
    }
}
