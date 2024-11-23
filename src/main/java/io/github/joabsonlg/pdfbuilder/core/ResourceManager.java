package io.github.joabsonlg.pdfbuilder.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de recursos para documentos PDF.
 * Responsável por carregar e gerenciar fontes e imagens.
 */
public class ResourceManager {
    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

    private final PDDocument document;
    private final Map<String, PDFont> fonts;
    private final Map<String, PDImageXObject> images;
    private PDFont defaultFont;

    /**
     * Cria um novo gerenciador de recursos.
     * @param document Documento PDF associado
     */
    public ResourceManager(PDDocument document) {
        this.document = document;
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();
        this.defaultFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        registerStandardFonts();
        logger.debug("ResourceManager inicializado");
    }

    /**
     * Registra as fontes padrão do PDFBox.
     */
    private void registerStandardFonts() {
        fonts.put("Helvetica", new PDType1Font(Standard14Fonts.FontName.HELVETICA));
        fonts.put("Helvetica-Bold", new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD));
        fonts.put("Helvetica-Oblique", new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE));
        fonts.put("Helvetica-BoldOblique", new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE));
        fonts.put("Times-Roman", new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN));
        fonts.put("Times-Bold", new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD));
        fonts.put("Times-Italic", new PDType1Font(Standard14Fonts.FontName.TIMES_ITALIC));
        fonts.put("Times-BoldItalic", new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD_ITALIC));
        fonts.put("Courier", new PDType1Font(Standard14Fonts.FontName.COURIER));
        fonts.put("Courier-Bold", new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD));
        fonts.put("Courier-Oblique", new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE));
        fonts.put("Courier-BoldOblique", new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE));
        
        logger.debug("Fontes padrão registradas");
    }

    /**
     * Define a fonte padrão.
     * @param fontName Nome da fonte (deve estar registrada)
     * @throws IllegalArgumentException se a fonte não estiver registrada
     */
    public void setDefaultFont(String fontName) {
        PDFont font = fonts.get(fontName);
        if (font == null) {
            throw new IllegalArgumentException("Fonte não registrada: " + fontName);
        }
        this.defaultFont = font;
        logger.debug("Fonte padrão definida para: {}", fontName);
    }

    /**
     * Retorna a fonte padrão atual.
     * @return A fonte padrão
     */
    public PDFont getDefaultFont() {
        return defaultFont;
    }

    /**
     * Retorna uma fonte registrada pelo nome.
     * @param name Nome da fonte
     * @return A fonte ou null se não encontrada
     */
    public PDFont getFont(String name) {
        return fonts.get(name);
    }

    /**
     * Carrega uma imagem de um arquivo.
     * @param name Nome para referenciar a imagem
     * @param imagePath Caminho do arquivo de imagem
     * @throws IOException se houver erro ao carregar a imagem
     */
    public void loadImage(String name, Path imagePath) throws IOException {
        try {
            PDImageXObject image = PDImageXObject.createFromFile(imagePath.toString(), document);
            images.put(name, image);
            logger.debug("Imagem carregada: {} de {}", name, imagePath);
        } catch (IOException e) {
            logger.error("Erro ao carregar imagem {}: {}", imagePath, e.getMessage());
            throw e;
        }
    }

    /**
     * Carrega uma imagem de um InputStream.
     * @param name Nome para referenciar a imagem
     * @param inputStream Stream contendo a imagem
     * @throws IOException se houver erro ao carregar a imagem
     */
    public void loadImage(String name, InputStream inputStream) throws IOException {
        try {
            PDImageXObject image = PDImageXObject.createFromByteArray(document, inputStream.readAllBytes(), name);
            images.put(name, image);
            logger.debug("Imagem carregada: {} do InputStream", name);
        } catch (IOException e) {
            logger.error("Erro ao carregar imagem do InputStream: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Retorna uma imagem registrada pelo nome.
     * @param name Nome da imagem
     * @return A imagem ou null se não encontrada
     */
    public PDImageXObject getImage(String name) {
        return images.get(name);
    }

    /**
     * Remove uma imagem do gerenciador.
     * @param name Nome da imagem
     * @return true se a imagem foi removida, false caso contrário
     */
    public boolean removeImage(String name) {
        PDImageXObject removed = images.remove(name);
        if (removed != null) {
            logger.debug("Imagem removida: {}", name);
            return true;
        }
        return false;
    }

    /**
     * Cria um novo PDResources com os recursos gerenciados.
     * @return Um novo PDResources
     */
    public PDResources createPageResources() {
        PDResources resources = new PDResources();
        // Aqui podemos adicionar recursos específicos para a página
        return resources;
    }
}
