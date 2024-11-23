package io.github.joabsonlg.pdfbuilder.core;

import io.github.joabsonlg.pdfbuilder.components.image.Image;
import io.github.joabsonlg.pdfbuilder.components.list.List;
import io.github.joabsonlg.pdfbuilder.components.logo.Logo;
import io.github.joabsonlg.pdfbuilder.components.logo.LogoStyle;
import io.github.joabsonlg.pdfbuilder.components.page.PageNumbering;
import io.github.joabsonlg.pdfbuilder.components.page.PageSection;
import io.github.joabsonlg.pdfbuilder.components.table.Table;
import io.github.joabsonlg.pdfbuilder.components.text.Heading;
import io.github.joabsonlg.pdfbuilder.components.text.Paragraph;
import io.github.joabsonlg.pdfbuilder.components.text.SimpleText;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

/**
 * Classe principal para construção de documentos PDF.
 * Fornece uma API fluente para criação e manipulação de PDFs.
 */
public class PDFBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFBuilder.class);
    private static final float DEFAULT_FONT_SIZE = 12.0f;

    private final PDDocument document;
    private final PDFConfiguration config;
    private final ResourceManager resourceManager;
    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private Coordinates currentPosition;
    private float currentFontSize;
    private float lineSpacing;
    private PageNumbering pageNumbering;
    private PageSection header;
    private PageSection footer;
    private Logo logo;

    /**
     * Cria uma nova instância do PDFBuilder com a configuração fornecida.
     *
     * @param config Configuração do PDF
     */
    public PDFBuilder(PDFConfiguration config) {
        this.config = config;
        this.document = new PDDocument();
        this.currentPage = new PDPage(config.getPageSize());
        this.document.addPage(currentPage);
        this.lineSpacing = 1.5f;
        this.currentFontSize = DEFAULT_FONT_SIZE;
        this.resourceManager = new ResourceManager(document);

        try {
            this.contentStream = new PDPageContentStream(document, currentPage);
            this.currentPosition = Coordinates.origin(config.getPageSize(), config.getSafeArea())
                    .moveTo(
                            config.getSafeArea().getMarginLeft(),
                            config.getPageSize().getHeight() - config.getSafeArea().getMarginTop()
                    );
        } catch (IOException e) {
            LOGGER.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }

    /**
     * Cria uma nova instância do PDFBuilder com configuração padrão.
     *
     * @return Nova instância do PDFBuilder
     */
    public static PDFBuilder create() {
        return new PDFBuilder(PDFConfiguration.create().build());
    }

    /**
     * Cria uma nova instância do PDFBuilder com configuração personalizada.
     *
     * @param config Configuração personalizada
     * @return Nova instância do PDFBuilder
     */
    public static PDFBuilder create(PDFConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuração não pode ser nula");
        }
        return new PDFBuilder(config);
    }

    /**
     * Adiciona uma nova página ao documento.
     *
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addNewPage() {
        try {
            addNewPageInternal();
            LOGGER.debug("Nova página adicionada ao documento");
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar nova página", e);
        }
    }

    private void addNewPageInternal() throws IOException {
        if (contentStream != null) {
            // Adiciona o rodapé antes de fechar a página atual
            if (footer != null) {
                addFooter();
            }
            contentStream.close();
        }

        // Cria nova página
        currentPage = new PDPage(config.getPageSize());
        document.addPage(currentPage);

        // Reseta a SafeArea para os valores padrão
        config.getSafeArea().reset();

        // Cria novo content stream
        contentStream = new PDPageContentStream(document, currentPage);

        // Reseta a posição para o topo da nova página
        currentPosition = Coordinates.origin(config.getPageSize(), config.getSafeArea())
                .moveTo(
                        config.getSafeArea().getMarginLeft(),
                        config.getPageSize().getHeight() - config.getSafeArea().getMarginTop()
                );

        // Adiciona logo à nova página
        if (logo != null) {
            addLogo();
        }
    }

    /**
     * Move para uma posição específica na página.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveTo(float x, float y) {
        currentPosition = currentPosition.moveTo(x, y);
        return this;
    }

    /**
     * Move relativamente à posição atual.
     *
     * @param deltaX Deslocamento em X
     * @param deltaY Deslocamento em Y
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveBy(float deltaX, float deltaY) {
        currentPosition = currentPosition.moveBy(deltaX, deltaY);
        return this;
    }

    /**
     * Move para uma posição relativa à área de conteúdo.
     *
     * @param percentX Porcentagem da largura (0-100)
     * @param percentY Porcentagem da altura (0-100)
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveToContentPercent(float percentX, float percentY) {
        currentPosition = currentPosition.moveToContentPercent(percentX, percentY);
        return this;
    }

    /**
     * Move para uma posição na área do cabeçalho.
     *
     * @param percentX Porcentagem da largura (0-100)
     * @param percentY Porcentagem da altura do cabeçalho (0-100)
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveToHeader(float percentX, float percentY) {
        currentPosition = currentPosition.moveToHeader(percentX, percentY);
        return this;
    }

    /**
     * Move para uma posição na área do rodapé.
     *
     * @param percentX Porcentagem da largura (0-100)
     * @param percentY Porcentagem da altura do rodapé (0-100)
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveToFooter(float percentX, float percentY) {
        currentPosition = currentPosition.moveToFooter(percentX, percentY);
        return this;
    }

    /**
     * Move para o topo da área de conteúdo.
     *
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveToTop() {
        PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
        float y = contentArea.getUpperRightY();
        currentPosition = currentPosition.moveTo(currentPosition.getX(), y);
        return this;
    }

    /**
     * Move para o início da linha atual.
     *
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveToStart() {
        PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
        float x = contentArea.getLowerLeftX();
        currentPosition = currentPosition.moveTo(x, currentPosition.getY());
        return this;
    }

    /**
     * Move para o final da área de conteúdo.
     *
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveToBottom() {
        PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
        float y = contentArea.getLowerLeftY();
        currentPosition = currentPosition.moveTo(currentPosition.getX(), y);
        return this;
    }

    /**
     * Move para a direita a partir da posição atual.
     *
     * @param distance Distância a mover em pontos
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveRight(float distance) {
        currentPosition = currentPosition.moveBy(distance, 0);
        return this;
    }

    /**
     * Move para baixo a partir da posição atual.
     *
     * @param distance Distância a mover em pontos
     * @return this para chamadas encadeadas
     */
    public PDFBuilder moveDown(float distance) {
        currentPosition = currentPosition.moveBy(0, -distance);
        return this;
    }

    /**
     * Adiciona texto na posição atual.
     *
     * @param text Texto a ser adicionado
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addText(String text) {
        try {
            PDFont font = resourceManager.getDefaultFont();
            contentStream.beginText();
            contentStream.setFont(font, currentFontSize);
            contentStream.newLineAtOffset(currentPosition.getX(), currentPosition.getY());
            contentStream.showText(text);
            contentStream.endText();

            LOGGER.debug("Texto adicionado: {}", text);
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar texto", e);
        }
    }

    /**
     * Adiciona uma linha de texto e move para a próxima linha.
     *
     * @param text Texto a ser adicionado
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addLine(String text) {
        try {
            PDFont font = resourceManager.getDefaultFont();
            contentStream.beginText();
            contentStream.setFont(font, currentFontSize);
            contentStream.newLineAtOffset(currentPosition.getX(), currentPosition.getY());
            contentStream.showText(text);
            contentStream.endText();

            // Move para a próxima linha
            float lineHeight = font.getBoundingBox().getHeight() / 1000 * currentFontSize * lineSpacing;
            moveDown(lineHeight);
            moveToStart();

            LOGGER.debug("Linha de texto adicionada: {}", text);
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar linha de texto", e);
        }
    }

    /**
     * Adiciona um componente de texto simples.
     *
     * @param simpleText Componente de texto
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addSimpleText(SimpleText simpleText) {
        try {
            PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
            float safeWidth = contentArea.getWidth();
            float newY = simpleText.render(contentStream, currentPosition.getX(), currentPosition.getY(), safeWidth);
            currentPosition = currentPosition.moveTo(currentPosition.getX(), newY);
            LOGGER.debug("SimpleText adicionado com quebra de linha automática");
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar SimpleText", e);
        }
    }

    /**
     * Adiciona um parágrafo com alinhamento.
     *
     * @param paragraph Componente de parágrafo
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addParagraph(Paragraph paragraph) {
        try {
            PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
            float safeWidth = contentArea.getWidth();
            
            // Verifica se precisa de nova página
            checkNewPage(paragraph.calculateHeight());
            
            float newY = paragraph.render(contentStream, currentPosition.getX(), currentPosition.getY(), safeWidth);
            currentPosition = currentPosition.moveTo(currentPosition.getX(), newY);
            LOGGER.debug("Parágrafo adicionado com alinhamento");
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar parágrafo", e);
        }
    }

    /**
     * Adiciona um título ao documento.
     *
     * @param heading Componente de título
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addHeading(Heading heading) {
        try {
            PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
            float safeWidth = contentArea.getWidth();
            float newY = heading.render(contentStream, currentPosition.getX(), currentPosition.getY(), safeWidth);
            currentPosition = currentPosition.moveTo(currentPosition.getX(), newY);
            LOGGER.debug("Título adicionado ao documento");
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar título", e);
        }
    }

    /**
     * Verifica se é necessário criar uma nova página baseado na altura necessária.
     *
     * @param heightNeeded Altura necessária para o próximo elemento
     */
    private void checkNewPage(float heightNeeded) throws IOException {
        PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
        float bottomLimit = contentArea.getLowerLeftY();

        if (currentPosition.getY() - heightNeeded < bottomLimit) {
            LOGGER.debug("Espaço insuficiente. Criando nova página...");

            // Fecha o content stream atual e adiciona o rodapé (se configurado)
            if (contentStream != null) {
                addFooter();
                contentStream.close();
            }

            // Cria nova página
            PDPage newPage = new PDPage(config.getPageSize());
            document.addPage(newPage);
            currentPage = newPage;

            // Cria novo content stream
            contentStream = new PDPageContentStream(document, currentPage);

            // Reseta a posição para o topo da nova página
            currentPosition = Coordinates.origin(config.getPageSize(), config.getSafeArea())
                    .moveTo(
                            config.getSafeArea().getMarginLeft(),
                            config.getPageSize().getHeight() - config.getSafeArea().getMarginTop()
                    );

            // Adiciona o cabeçalho e logo após criar o contentStream
            addHeader();
            addLogo();
        }
    }

    /**
     * Adiciona uma imagem ao documento.
     *
     * @param image Componente de imagem
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addImage(Image image) {
        try {
            PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
            float safeWidth = contentArea.getWidth();
            Dimension dimensions = image.getDimensions();
            float aspectRatio = dimensions.height / (float) dimensions.width;
            float imageWidth = Math.min(safeWidth, dimensions.width);
            float imageHeight = imageWidth * aspectRatio;

            // Verifica se precisa de nova página
            checkNewPage(imageHeight);

            // Renderiza a imagem e atualiza a posição Y
            float newY = image.render(contentStream, currentPosition.getX(), currentPosition.getY(), safeWidth, imageWidth);
            currentPosition = currentPosition.moveTo(currentPosition.getX(), newY);

            // Adiciona espaço após a imagem
            moveDown(20); // 20 pontos de espaço após cada imagem

            LOGGER.debug("Imagem adicionada com dimensões: {}x{}", imageWidth, imageHeight);
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar imagem", e);
        }
    }

    /**
     * Adiciona uma tabela ao documento.
     *
     * @param table Componente de tabela
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addTable(Table table) {
        try {
            PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
            float safeWidth = contentArea.getWidth();

            float tableHeight = table.calculateHeight();

            checkNewPage(tableHeight);

            float newY = table.render(contentStream, currentPosition.getX(), currentPosition.getY(), safeWidth);
            currentPosition = currentPosition.moveTo(currentPosition.getX(), newY);

            // Adiciona espaço após a tabela
            moveDown(20); // 20 pontos de espaço após a tabela

            LOGGER.debug("Tabela adicionada ao documento");
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar tabela", e);
        }
    }

    /**
     * Adiciona uma lista ao documento.
     *
     * @param list Componente de lista
     * @return this para chamadas encadeadas
     */
    public PDFBuilder addList(List list) {
        try {
            PDRectangle contentArea = config.getSafeArea().getContentArea(config.getPageSize());
            float safeWidth = contentArea.getWidth();
            float newY = list.render(contentStream, currentPosition.getX(), currentPosition.getY(), safeWidth, this);
            currentPosition = currentPosition.moveTo(currentPosition.getX(), newY);

            // Adiciona espaço após a lista
            moveDown(20); // 20 pontos de espaço após a lista

            LOGGER.debug("Lista adicionada ao documento");
            return this;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar lista", e);
        }
    }

    /**
     * Define o tamanho da fonte.
     *
     * @param fontSize Tamanho da fonte em pontos
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setFontSize(float fontSize) {
        if (fontSize <= 0) {
            throw new IllegalArgumentException("Tamanho da fonte deve ser maior que zero");
        }
        this.currentFontSize = fontSize;
        return this;
    }

    /**
     * Define o espaçamento entre linhas.
     *
     * @param spacing Fator de espaçamento (1.0 = espaçamento simples)
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setLineSpacing(float spacing) {
        if (spacing <= 0) {
            throw new IllegalArgumentException("Espaçamento deve ser maior que zero");
        }
        this.lineSpacing = spacing;
        return this;
    }

    /**
     * Atualiza a configuração do builder.
     *
     * @param config Nova configuração
     * @return this para chamadas encadeadas
     */
    public PDFBuilder withConfiguration(PDFConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuração não pode ser nula");
        }
        return new PDFBuilder(config);
    }

    /**
     * Atualiza o tamanho da página.
     *
     * @param pageSize Novo tamanho de página
     * @return this para chamadas encadeadas
     */
    public PDFBuilder withPageSize(PDRectangle pageSize) {
        if (pageSize == null) {
            throw new IllegalArgumentException("Tamanho da página não pode ser nulo");
        }
        PDFConfiguration newConfig = config.withPageSize(pageSize);
        return new PDFBuilder(newConfig);
    }

    /**
     * Define a numeração de páginas.
     *
     * @param pageNumbering Configuração de numeração de páginas
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setPageNumbering(PageNumbering pageNumbering) {
        this.pageNumbering = pageNumbering;
        return this;
    }

    /**
     * Define o cabeçalho do documento.
     *
     * @param header Configuração do cabeçalho
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setHeader(PageSection header) {
        this.header = header;
        try {
            // Renderiza o header na página atual quando ele é definido
            if (contentStream != null) {
                addHeader();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar cabeçalho", e);
        }
        return this;
    }

    /**
     * Define o rodapé do documento.
     *
     * @param footer Configuração do rodapé
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setFooter(PageSection footer) {
        this.footer = footer;
        return this;
    }

    /**
     * Define o logo do documento usando apenas um título.
     *
     * @param title Título do logo
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setLogo(String title) {
        this.logo = Logo.builder()
                .withTitle(title)
                .build();
        try {
            addLogo();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar logo", e);
        }
        return this;
    }

    /**
     * Define o logo do documento com título e estilo personalizado.
     *
     * @param title Título do logo
     * @param style Estilo do logo
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setLogo(String title, LogoStyle style) {
        this.logo = Logo.builder()
                .withTitle(title)
                .withStyle(style)
                .build();
        try {
            addLogo();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar logo", e);
        }
        return this;
    }

    /**
     * Define o logo do documento com imagens nas laterais.
     *
     * @param title          Título do logo
     * @param style          Estilo do logo
     * @param leftImagePath  Caminho da imagem da esquerda
     * @param rightImagePath Caminho da imagem da direita
     * @return this para chamadas encadeadas
     */
    public PDFBuilder setLogo(String title, LogoStyle style, String leftImagePath, String rightImagePath) throws IOException {
        PDImageXObject leftImage = null;
        PDImageXObject rightImage = null;

        if (leftImagePath != null) {
            leftImage = PDImageXObject.createFromFile(leftImagePath, document);
        }
        if (rightImagePath != null) {
            rightImage = PDImageXObject.createFromFile(rightImagePath, document);
        }

        this.logo = Logo.builder()
                .withTitle(title)
                .withStyle(style)
                .withLeftImage(leftImage)
                .withRightImage(rightImage)
                .build();

        try {
            // Reseta a posição e adiciona o logo
            currentPosition = Coordinates.origin(config.getPageSize(), config.getSafeArea())
                    .moveTo(
                            config.getSafeArea().getMarginLeft(),
                            config.getPageSize().getHeight() - config.getSafeArea().getMarginTop()
                    );
            addLogo();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao adicionar logo", e);
        }
        return this;
    }

    /**
     * Renderiza o cabeçalho na página atual.
     */
    private void addHeader() throws IOException {
        if (header != null) {
            // Posiciona o header na margem superior
            float y = config.getPageSize().getHeight() - config.getMarginTop() - 5;
            header.render(contentStream, config.getPageSize().getWidth(), y, config.getMarginLeft(), config.getMarginRight());
        }
    }

    /**
     * Adiciona o rodapé à página atual.
     */
    private void addFooter() throws IOException {
        if (footer != null) {
            float footerY = config.getSafeArea().getMarginBottom();
            footer.render(contentStream, config.getPageSize().getWidth(), footerY,
                    config.getSafeArea().getMarginLeft(), config.getSafeArea().getMarginRight());

            // Adiciona numeração de página se configurada
            if (pageNumbering != null) {
                int pageNumber = document.getPages().indexOf(currentPage) + 1;
                int totalPages = document.getNumberOfPages();
                pageNumbering.render(contentStream, config.getPageSize().getWidth(),
                        config.getPageSize().getHeight(), pageNumber, totalPages);
            }
        }
    }

    /**
     * Adiciona o logo à página atual.
     */
    private void addLogo() throws IOException {
        if (logo != null) {
            float y = currentPosition.getY();

            // Renderiza o logo
            logo.render(contentStream, config.getPageSize().getWidth(), y,
                    config.getSafeArea().getMarginLeft(), config.getSafeArea().getMarginRight());

            // Atualiza a posição atual para logo abaixo do logo
            float logoHeight = logo.getTotalHeight();
            currentPosition = currentPosition.moveBy(0, -logoHeight);
        }
    }

    /**
     * Salva o documento no caminho especificado.
     *
     * @param path Caminho para salvar o documento
     */
    public void save(String path) {
        try {
            // Add footer to the last page before saving
            if (contentStream != null) {
                addFooter();
                contentStream.close();
                contentStream = null;
            }
            document.save(path);
            LOGGER.debug("Documento salvo em: {}", path);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar documento", e);
        }
    }

    /**
     * Fecha o builder e libera recursos.
     */
    public void close() {
        try {
            if (contentStream != null) {
                addFooter();
                contentStream.close();
                contentStream = null;
            }
            document.close();
            LOGGER.debug("Builder fechado e recursos liberados");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fechar recursos", e);
        }
    }

    /**
     * Retorna o gerenciador de recursos do documento.
     *
     * @return Gerenciador de recursos
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    // Getters
    public PDDocument getDocument() {
        return document;
    }

    public PDFConfiguration getConfig() {
        return config;
    }

    public PDPage getCurrentPage() {
        return currentPage;
    }

    public Coordinates getCurrentPosition() {
        return currentPosition;
    }
}
