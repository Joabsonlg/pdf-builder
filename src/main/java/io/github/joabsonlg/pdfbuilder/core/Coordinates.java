package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gerencia coordenadas e posicionamento no documento PDF.
 */
public final class Coordinates {
    private static final Logger LOGGER = LoggerFactory.getLogger(Coordinates.class);

    private final float x;
    private final float y;
    private final PDRectangle pageSize;
    private final SafeArea safeArea;

    private Coordinates(float x, float y, PDRectangle pageSize, SafeArea safeArea) {
        this.x = x;
        this.y = y;
        this.pageSize = pageSize;
        this.safeArea = safeArea;
    }

    /**
     * Cria uma nova instância de Coordinates na origem (0,0).
     *
     * @param pageSize Tamanho da página
     * @param safeArea Área segura
     * @return Nova instância de Coordinates
     */
    public static Coordinates origin(PDRectangle pageSize, SafeArea safeArea) {
        return new Coordinates(0, 0, pageSize, safeArea);
    }

    /**
     * Move para uma posição específica.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return Nova instância de Coordinates
     */
    public Coordinates moveTo(float x, float y) {
        return new Coordinates(x, y, pageSize, safeArea);
    }

    /**
     * Move relativamente à posição atual.
     *
     * @param deltaX Deslocamento em X
     * @param deltaY Deslocamento em Y
     * @return Nova instância de Coordinates
     */
    public Coordinates moveBy(float deltaX, float deltaY) {
        return new Coordinates(x + deltaX, y + deltaY, pageSize, safeArea);
    }

    /**
     * Move para uma posição relativa à área de conteúdo.
     *
     * @param percentX Porcentagem da largura (0-100)
     * @param percentY Porcentagem da altura (0-100)
     * @return Nova instância de Coordinates
     */
    public Coordinates moveToContentPercent(float percentX, float percentY) {
        if (percentX < 0 || percentX > 100 || percentY < 0 || percentY > 100) {
            throw new IllegalArgumentException("Percentagens devem estar entre 0 e 100");
        }

        PDRectangle contentArea = safeArea.getContentArea(pageSize);
        float newX = contentArea.getLowerLeftX() + (contentArea.getWidth() * percentX / 100);
        float newY = contentArea.getLowerLeftY() + (contentArea.getHeight() * percentY / 100);

        return new Coordinates(newX, newY, pageSize, safeArea);
    }

    /**
     * Move para uma posição na área do cabeçalho.
     *
     * @param percentX Porcentagem da largura (0-100)
     * @param percentY Porcentagem da altura do cabeçalho (0-100)
     * @return Nova instância de Coordinates
     */
    public Coordinates moveToHeader(float percentX, float percentY) {
        if (percentX < 0 || percentX > 100 || percentY < 0 || percentY > 100) {
            throw new IllegalArgumentException("Percentagens devem estar entre 0 e 100");
        }

        if (!safeArea.hasHeader()) {
            throw new IllegalStateException("Área de cabeçalho não definida");
        }

        PDRectangle headerArea = safeArea.getHeaderArea(pageSize);
        float newX = headerArea.getLowerLeftX() + (headerArea.getWidth() * percentX / 100);
        float newY = headerArea.getLowerLeftY() + (headerArea.getHeight() * percentY / 100);

        return new Coordinates(newX, newY, pageSize, safeArea);
    }

    /**
     * Move para uma posição na área do rodapé.
     *
     * @param percentX Porcentagem da largura (0-100)
     * @param percentY Porcentagem da altura do rodapé (0-100)
     * @return Nova instância de Coordinates
     */
    public Coordinates moveToFooter(float percentX, float percentY) {
        if (percentX < 0 || percentX > 100 || percentY < 0 || percentY > 100) {
            throw new IllegalArgumentException("Percentagens devem estar entre 0 e 100");
        }

        if (!safeArea.hasFooter()) {
            throw new IllegalStateException("Área de rodapé não definida");
        }

        PDRectangle footerArea = safeArea.getFooterArea(pageSize);
        float newX = footerArea.getLowerLeftX() + (footerArea.getWidth() * percentX / 100);
        float newY = footerArea.getLowerLeftY() + (footerArea.getHeight() * percentY / 100);

        return new Coordinates(newX, newY, pageSize, safeArea);
    }

    /**
     * Move para o topo da área de conteúdo.
     *
     * @return Nova instância de Coordinates no topo da área de conteúdo
     */
    public Coordinates moveToTop() {
        PDRectangle contentArea = safeArea.getContentArea(pageSize);
        float newY = contentArea.getUpperRightY();
        return new Coordinates(x, newY, pageSize, safeArea);
    }

    /**
     * Verifica se a posição atual está dentro da área segura.
     *
     * @return true se estiver dentro da área segura
     */
    public boolean isInSafeArea() {
        return safeArea.isPointInSafeArea(x, y, pageSize);
    }

    /**
     * Retorna a posição atual ajustada para estar dentro da área segura.
     * Se a posição atual estiver fora da área segura, move para o ponto mais próximo dentro dela.
     *
     * @return Nova instância de Coordinates dentro da área segura
     */
    public Coordinates ensureInSafeArea() {
        if (isInSafeArea()) {
            return this;
        }

        PDRectangle contentArea = safeArea.getContentArea(pageSize);
        float newX = x;
        float newY = y;

        // Ajusta X se necessário
        if (x < contentArea.getLowerLeftX()) {
            newX = contentArea.getLowerLeftX();
        } else if (x > contentArea.getUpperRightX()) {
            newX = contentArea.getUpperRightX();
        }

        // Ajusta Y se necessário
        if (y < contentArea.getLowerLeftY()) {
            newY = contentArea.getLowerLeftY();
        } else if (y > contentArea.getUpperRightY()) {
            newY = contentArea.getUpperRightY();
        }

        LOGGER.debug("Ajustando coordenadas de ({},{}) para ({},{}) para respeitar área segura",
                x, y, newX, newY);
        return new Coordinates(newX, newY, pageSize, safeArea);
    }

    // Getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public PDRectangle getPageSize() {
        return pageSize;
    }

    public SafeArea getSafeArea() {
        return safeArea;
    }
}
