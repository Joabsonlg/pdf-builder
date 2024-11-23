package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurações globais para documentos PDF.
 * Esta classe usa o padrão Builder para uma configuração fluente.
 */
public final class PDFConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFConfiguration.class);

    // Valores padrão
    private static final PDRectangle DEFAULT_PAGE_SIZE = PDRectangle.A4;
    private static final int DEFAULT_DPI = 300;
    private static final float DEFAULT_COMPRESSION_QUALITY = 0.7f;
    private static final float DEFAULT_FONT_SIZE = 12f;
    private static final float DEFAULT_LINE_SPACING = 14f;

    // Configurações de página
    private final PDRectangle pageSize;

    // Configurações de imagem
    private final int dpi;
    private final float compressionQuality;

    // Configurações de texto
    private final float fontSize;
    private final float lineSpacing;

    // Área segura
    private final SafeArea safeArea;

    private PDFConfiguration(Builder builder) {
        this.pageSize = builder.pageSize;
        this.dpi = builder.dpi;
        this.compressionQuality = builder.compressionQuality;
        this.fontSize = builder.fontSize;
        this.lineSpacing = builder.lineSpacing;
        this.safeArea = builder.safeArea;

        LOGGER.debug("PDFConfiguration criada com: pageSize={}, dpi={}, fontSize={}, lineSpacing={}",
                pageSize, dpi, fontSize, lineSpacing);
    }

    /**
     * Cria uma nova instância de PDFConfiguration com valores padrão.
     *
     * @return Nova instância de PDFConfiguration
     */
    public static Builder create() {
        LOGGER.debug("Criando nova configuração PDF com valores padrão");
        return new Builder();
    }

    /**
     * Define o tamanho padrão da página.
     *
     * @param pageSize Tamanho da página (ex: PDRectangle.A4)
     * @return Nova instância de PDFConfiguration
     * @throws IllegalArgumentException se pageSize for nulo
     */
    public PDFConfiguration withPageSize(PDRectangle pageSize) {
        if (pageSize == null) {
            throw new IllegalArgumentException("Tamanho da página não pode ser nulo");
        }
        Builder builder = new Builder(this);
        builder.withPageSize(pageSize);
        return builder.build();
    }

    /**
     * Define a resolução DPI para imagens.
     *
     * @param dpi Valor DPI (dots per inch)
     * @return Nova instância de PDFConfiguration
     * @throws IllegalArgumentException se dpi for menor ou igual a zero
     */
    public PDFConfiguration withDPI(int dpi) {
        if (dpi <= 0) {
            throw new IllegalArgumentException("DPI deve ser maior que zero");
        }
        Builder builder = new Builder(this);
        builder.withDPI(dpi);
        return builder.build();
    }

    /**
     * Define a qualidade de compressão para imagens.
     *
     * @param quality Valor entre 0.0 e 1.0
     * @return Nova instância de PDFConfiguration
     * @throws IllegalArgumentException se o valor estiver fora do intervalo [0,1]
     */
    public PDFConfiguration withCompressionQuality(float quality) {
        if (quality < 0.0f || quality > 1.0f) {
            throw new IllegalArgumentException("Qualidade de compressão deve estar entre 0.0 e 1.0");
        }
        Builder builder = new Builder(this);
        builder.withCompressionQuality(quality);
        return builder.build();
    }

    /**
     * Define o tamanho da fonte.
     *
     * @param fontSize Tamanho da fonte em pontos
     * @return Nova instância de PDFConfiguration
     * @throws IllegalArgumentException se fontSize for menor ou igual a zero
     */
    public PDFConfiguration withFontSize(float fontSize) {
        if (fontSize <= 0) {
            throw new IllegalArgumentException("Tamanho da fonte deve ser maior que zero");
        }
        Builder builder = new Builder(this);
        builder.withFontSize(fontSize);
        return builder.build();
    }

    /**
     * Define o espaçamento entre linhas.
     *
     * @param lineSpacing Espaçamento em pontos
     * @return Nova instância de PDFConfiguration
     * @throws IllegalArgumentException se lineSpacing for menor ou igual a zero
     */
    public PDFConfiguration withLineSpacing(float lineSpacing) {
        if (lineSpacing <= 0) {
            throw new IllegalArgumentException("Espaçamento entre linhas deve ser maior que zero");
        }
        Builder builder = new Builder(this);
        builder.withLineSpacing(lineSpacing);
        return builder.build();
    }

    /**
     * Define a área segura.
     *
     * @param safeArea Área segura
     * @return Nova instância de PDFConfiguration
     * @throws IllegalArgumentException se safeArea for nulo
     */
    public PDFConfiguration withSafeArea(SafeArea safeArea) {
        if (safeArea == null) {
            throw new IllegalArgumentException("Área segura não pode ser nula");
        }
        Builder builder = new Builder(this);
        builder.withSafeArea(safeArea);
        return builder.build();
    }

    // Getters
    public PDRectangle getPageSize() {
        return pageSize;
    }

    public SafeArea getSafeArea() {
        return safeArea;
    }

    public int getDpi() {
        return dpi;
    }

    public float getCompressionQuality() {
        return compressionQuality;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getLineSpacing() {
        return lineSpacing;
    }

    // Métodos de conveniência para acessar margens
    public float getMarginLeft() {
        return safeArea.getMarginLeft();
    }

    public float getMarginRight() {
        return safeArea.getMarginRight();
    }

    public float getMarginTop() {
        return safeArea.getMarginTop();
    }

    public float getMarginBottom() {
        return safeArea.getMarginBottom();
    }

    /**
     * Calcula a área útil da página (área dentro das margens).
     *
     * @return PDRectangle representando a área útil
     */
    public PDRectangle getContentArea() {
        float width = pageSize.getWidth() - (safeArea.getMarginLeft() + safeArea.getMarginRight());
        float height = pageSize.getHeight() - (safeArea.getMarginTop() + safeArea.getMarginBottom());
        return new PDRectangle(width, height);
    }

    public static class Builder {
        private PDRectangle pageSize = DEFAULT_PAGE_SIZE;
        private SafeArea safeArea = SafeArea.builder().build();
        private int dpi = DEFAULT_DPI;
        private float compressionQuality = DEFAULT_COMPRESSION_QUALITY;
        private float fontSize = DEFAULT_FONT_SIZE;
        private float lineSpacing = DEFAULT_LINE_SPACING;

        public Builder() {
        }

        public Builder(PDFConfiguration pdfConfiguration) {
            this.pageSize = pdfConfiguration.pageSize;
            this.safeArea = pdfConfiguration.safeArea;
            this.dpi = pdfConfiguration.dpi;
            this.compressionQuality = pdfConfiguration.compressionQuality;
            this.fontSize = pdfConfiguration.fontSize;
            this.lineSpacing = pdfConfiguration.lineSpacing;
        }

        public Builder withPageSize(PDRectangle pageSize) {
            if (pageSize == null) {
                throw new IllegalArgumentException("Tamanho da página não pode ser nulo");
            }
            this.pageSize = pageSize;
            return this;
        }

        public Builder withSafeArea(SafeArea safeArea) {
            if (safeArea == null) {
                throw new IllegalArgumentException("SafeArea não pode ser nula");
            }
            this.safeArea = safeArea;
            return this;
        }

        public Builder withDPI(int dpi) {
            if (dpi <= 0) {
                throw new IllegalArgumentException("DPI deve ser maior que zero");
            }
            this.dpi = dpi;
            return this;
        }

        public Builder withCompressionQuality(float quality) {
            if (quality < 0.0f || quality > 1.0f) {
                throw new IllegalArgumentException("Qualidade de compressão deve estar entre 0.0 e 1.0");
            }
            this.compressionQuality = quality;
            return this;
        }

        public Builder withFontSize(float fontSize) {
            if (fontSize <= 0) {
                throw new IllegalArgumentException("Tamanho da fonte deve ser maior que zero");
            }
            this.fontSize = fontSize;
            return this;
        }

        public Builder withLineSpacing(float lineSpacing) {
            if (lineSpacing <= 0) {
                throw new IllegalArgumentException("Espaçamento de linha deve ser maior que zero");
            }
            this.lineSpacing = lineSpacing;
            return this;
        }

        public PDFConfiguration build() {
            if (pageSize == null) {
                throw new IllegalArgumentException("Tamanho da página não pode ser nulo");
            }
            if (safeArea == null) {
                throw new IllegalArgumentException("SafeArea não pode ser nula");
            }
            return new PDFConfiguration(this);
        }
    }
}
