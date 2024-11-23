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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Complete example demonstrating all features of the PDF Builder library.
 *
 * <p>This example demonstrates how to create a PDF document with a custom header, footer, images, lists, tables, and more.</p>
 *
 * @author Joabson Arley do Nascimento
 * @since 1.0.0
 */
public final class CompleteExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteExample.class);

    private CompleteExample() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void main(String[] args) {
        try {
            SafeArea safeArea = SafeArea.builder()
                    .withMargins(50f, 40f, 30f, 40f)
                    .withHeader(true)
                    .withFooter(true)
                    .build();

            PDFConfiguration config = PDFConfiguration.create()
                    .withPageSize(PDRectangle.A4)
                    .withSafeArea(safeArea)
                    .withDPI(300)
                    .withCompressionQuality(0.8f)
                    .withFontSize(12f)
                    .withLineSpacing(14f)
                    .build();

            PDFBuilder builder = new PDFBuilder(config);

            PDFont defaultFont = builder.getResourceManager().getDefaultFont();

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

            String imagePath = "src/main/java/io/github/joabsonlg/pdfbuilder/examples/sample-image.jpg";
            builder.setLogo("PDF Builder - Complete example", logoStyle, imagePath, imagePath);

            PageNumbering pageNumbering = PageNumbering.builder()
                    .withFont(defaultFont)
                    .withFontSize(10)
                    .withColor(new Color(128, 128, 128))
                    .withFormat(PageNumbering.Format.WITH_TOTAL)
                    .withPosition(PageNumbering.Position.BOTTOM)
                    .withAlignment(TextAlignment.RIGHT)
                    .build();

            builder.setFooter(PageSectionStyle.createConfidentialFooter("joabsonlg"));
            builder.setPageNumbering(pageNumbering);

            builder.addHeading(Heading.builder()
                    .withText("Complete demonstration of the PDF Builder library")
                    .withLevel(HeadingLevel.H1)
                    .withStyle(titleStyle)
                    .withAlignment(TextAlignment.CENTER)
                    .build());

            TextStyle defaultStyle = TextStyle.builder()
                    .withFont(defaultFont)
                    .withFontSize(12f)
                    .withColor(Color.BLACK)
                    .build();

            builder.addParagraph(Paragraph.builder()
                    .addStyledText("This is a complete example demonstrating all the features of the ", defaultStyle)
                    .addStyledText("PDF Builder", boldStyle)
                    .addStyledText(". Below you will see examples of headers, footers, lists, tables, and images.", defaultStyle)
                    .build());

            builder.moveDown(10);

            for (int i = 0; i < 15; i++) {
                LOGGER.info("Adding paragraph {}", i);
                builder.addParagraph(Paragraph.builder()
                        .withAlignment(TextAlignment.JUSTIFIED)
                        .addStyledText(i + ": Mussum Ipsum, cacilds vidis litro abertis.  Interagi no mé, cursus quis, " +
                                "vehicula ac nisi. Mé faiz elementum girarzis, nisi eros vermeio. Manduma pindureta quium " +
                                "dia nois paga. Vehicula non. Ut sed ex eros. Vivamus sit amet nibh non tellus tristique " +
                                "interdum.", defaultStyle)
                        .build());
                builder.moveDown(10);
            }

            builder.moveDown(20);

            File imageFile = new File(imagePath);
            Image mainImage = Image.builder(builder.getDocument(), imageFile)
                    .withWidth(400)
                    .withAlignment(Image.Alignment.CENTER)
                    .withCaption("Figure 1: Example of a centered image")
                    .build();

            builder.addImage(mainImage);
            builder.moveDown(20);

            // Cria uma lista com itens estilizados
            java.util.List<ListItem> items = new ArrayList<>();

            ListItem item1 = new ListItem("PDF Builder Features:", defaultFont, 12, Color.BLACK);
            item1.addSubItem(new ListItem("Custom Headers and Footers", defaultFont, 12, Color.BLACK));
            item1.addSubItem(new ListItem("Automatic Page Numbering", defaultFont, 12, Color.BLACK));
            item1.addSubItem(new ListItem("Support for Images with Captions", defaultFont, 12, Color.BLACK));
            items.add(item1);

            ListItem item2 = new ListItem("Text Formatting:", defaultFont, 12, Color.BLACK);
            item2.addSubItem(new ListItem("Different Styles and Colors", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Custom Alignment", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Support for Multiple Fonts", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Different Styles and Colors", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Custom Alignment", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Support for Multiple Fonts", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Different Styles and Colors", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Custom Alignment", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Support for Multiple Fonts", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Different Styles and Colors", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Custom Alignment", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Support for Multiple Fonts", defaultFont, 12, Color.BLACK));
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
            java.util.List<java.util.List<String>> tableData = new ArrayList<>();
            tableData.add(java.util.Arrays.asList("Feature", "Description", "Status"));
            tableData.add(java.util.Arrays.asList("Headers", "Support for logos and titles", "Implemented"));
            tableData.add(java.util.Arrays.asList("Footers", "Page numbering and texts", "Implemented"));
            tableData.add(java.util.Arrays.asList("Images", "Support for different formats", "Implemented"));
            tableData.add(java.util.Arrays.asList("Tables", "Complete formatting", "Implemented"));

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

            Image footerImage = Image.builder(builder.getDocument(), imageFile)
                    .withWidth(200)
                    .withAlignment(Image.Alignment.RIGHT)
                    .build();

            builder.addImage(footerImage);

            builder.save("complete-example.pdf");
        } catch (IOException e) {
            LOGGER.error("Error generating PDF: {}", e.getMessage(), e);
        }
    }
}
