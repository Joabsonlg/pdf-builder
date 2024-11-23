package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.image.Image;
import io.github.joabsonlg.pdfbuilder.components.list.List.ListItem;
import io.github.joabsonlg.pdfbuilder.components.logo.LogoStyle;
import io.github.joabsonlg.pdfbuilder.components.page.PageNumbering;
import io.github.joabsonlg.pdfbuilder.components.page.PageSectionStyle;
import io.github.joabsonlg.pdfbuilder.components.table.Table;
import io.github.joabsonlg.pdfbuilder.components.text.Heading;
import io.github.joabsonlg.pdfbuilder.components.text.HeadingLevel;
import io.github.joabsonlg.pdfbuilder.components.text.Paragraph;
import io.github.joabsonlg.pdfbuilder.components.text.TextAlignment;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class CompleteExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteExample.class);

    private CompleteExample() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void main(String[] args) {
        try {
            // Configuração da área segura com header e footer
            SafeArea safeArea = SafeArea.builder()
                    .withMargins(50f, 40f, 30f, 40f)
                    .withHeader(true)
                    .withFooter(true)
                    .build();

            // Configuração do documento
            PDFConfiguration config = PDFConfiguration.create()
                    .withPageSize(PDRectangle.A4)
                    .withSafeArea(safeArea)
                    .withDPI(300)
                    .withCompressionQuality(0.8f)
                    .withFontSize(12f)
                    .withLineSpacing(14f)
                    .build();

            // Cria o builder do PDF
            PDFBuilder builder = new PDFBuilder(config);

            // Fonte padrão
            PDType1Font defaultFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            // Estilos de texto
            TextStyle titleStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(24f)
                    .withColor(new Color(0, 51, 102))
                    .build();

            TextStyle boldStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(12)
                    .withColor(Color.BLACK)
                    .build();

            // Configura o logo
            LogoStyle logoStyle = LogoStyle.builder()
                    .withFontSize(16f)
                    .withColor(Color.BLACK)
                    .withMarginBottom(20f)
                    .withDrawLine(true)
                    .withLineWidth(1f)
                    .withLineColor(new Color(43, 43, 43, 128))
                    .withImageHeight(30f)
                    .withImageMargin(10f)
                    .build();

            // Configura o logo com imagens
            String imagePath = "src/main/java/io/github/joabsonlg/pdfbuilder/examples/sample-image.jpg";
            builder.setLogo("PDF Builder - Exemplo Completo", logoStyle, imagePath, imagePath);

            // Configura a numeração de páginas
            PageNumbering pageNumbering = PageNumbering.builder()
                    .withFont(defaultFont)
                    .withFontSize(10)
                    .withColor(new Color(128, 128, 128))
                    .withFormat(PageNumbering.Format.WITH_TOTAL)
                    .withPosition(PageNumbering.Position.BOTTOM)
                    .withAlignment(TextAlignment.RIGHT)
                    .build();

            // Configura o rodapé
            builder.setFooter(PageSectionStyle.createConfidentialFooter("Nut IT"));
            builder.setPageNumbering(pageNumbering);

            // Adiciona título principal
            builder.addHeading(Heading.builder()
                    .withText("Demonstração Completa do PDF Builder")
                    .withLevel(HeadingLevel.H1)
                    .withStyle(titleStyle)
                    .withAlignment(TextAlignment.CENTER)
                    .build());

            // Move para baixo para dar espaço
            builder.moveDown(20);

            // Adiciona texto introdutório
            builder.addParagraph(Paragraph.builder()
                    .addStyledText("Este é um exemplo completo que demonstra todas as funcionalidades do ",
                            TextStyle.builder()
                                    .withFont(defaultFont)
                                    .withFontSize(12f)
                                    .withColor(Color.BLACK)
                                    .build())
                    .addStyledText("PDF Builder", boldStyle)
                    .addStyledText(". Abaixo você verá exemplos de cabeçalhos, rodapés, listas, tabelas e imagens.",
                            TextStyle.builder()
                                    .withFont(defaultFont)
                                    .withFontSize(12f)
                                    .withColor(Color.BLACK)
                                    .build())
                    .build());

            builder.moveDown(20);

            // Adiciona uma imagem
            File imageFile = new File(imagePath);
            Image mainImage = Image.builder(builder.getDocument(), imageFile)
                    .withWidth(400)
                    .withAlignment(Image.Alignment.CENTER)
                    .withCaption("Figura 1: Exemplo de imagem centralizada")
                    .build();

            builder.addImage(mainImage);
            builder.moveDown(20);

            // Cria uma lista com itens estilizados
            java.util.List<ListItem> items = new ArrayList<>();

            ListItem item1 = new ListItem("Recursos do PDF Builder:", defaultFont, 12, Color.BLACK);
            item1.addSubItem(new ListItem("Cabeçalhos e Rodapés personalizados", defaultFont, 12, Color.BLACK));
            item1.addSubItem(new ListItem("Numeração automática de páginas", defaultFont, 12, Color.BLACK));
            item1.addSubItem(new ListItem("Suporte a imagens com legendas", defaultFont, 12, Color.BLACK));
            items.add(item1);

            ListItem item2 = new ListItem("Formatação de Texto:", defaultFont, 12, Color.BLACK);
            item2.addSubItem(new ListItem("Diferentes estilos e cores", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Alinhamento personalizado", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Suporte a múltiplas fontes", defaultFont, 12, Color.BLACK));
            items.add(item2);

            io.github.joabsonlg.pdfbuilder.components.list.List list = io.github.joabsonlg.pdfbuilder.components.list.List.builder()
                    .withFont(defaultFont)
                    .withFontSize(12)
                    .withListItems(items)
                    .withTextColor(Color.BLACK)
                    .withIndentation(30)
                    .withLineSpacing(8)
                    .build();

            builder.addList(list);
            builder.moveDown(20);

            // Adiciona uma tabela estilizada
            java.util.List<java.util.List<String>> tableData = new ArrayList<>();
            tableData.add(java.util.Arrays.asList("Funcionalidade", "Descrição", "Status"));
            tableData.add(java.util.Arrays.asList("Cabeçalhos", "Suporte a logos e títulos", "Implementado"));
            tableData.add(java.util.Arrays.asList("Rodapés", "Numeração de páginas e textos", "Implementado"));
            tableData.add(java.util.Arrays.asList("Imagens", "Suporte a diferentes formatos", "Implementado"));
            tableData.add(java.util.Arrays.asList("Tabelas", "Formatação completa", "Implementado"));

            Table table = Table.builder()
                    .withData(tableData)
                    .withColumnWidths(150, 200, 150)
                    .withRowHeight(30)
                    .withFontSize(12)
                    .withHeaderBackgroundColor(new Color(51, 122, 183))
                    .withHeaderTextColor(Color.WHITE)
                    .withBorderColor(new Color(51, 122, 183))
                    .withBorderWidth(1.5f)
                    .build();

            builder.addTable(table);
            builder.moveDown(20);

            // Adiciona uma imagem final
            Image footerImage = Image.builder(builder.getDocument(), imageFile)
                    .withWidth(200)
                    .withAlignment(Image.Alignment.RIGHT)
                    .withCaption("Figura 2: Exemplo de imagem alinhada à direita")
                    .build();

            builder.addImage(footerImage);

            // Salva o documento
            builder.save("complete-example.pdf");

        } catch (IOException e) {
            LOGGER.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }
}
