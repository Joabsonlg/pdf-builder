package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.image.Image;
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

    public static void main(String[] args) throws IOException {
        SafeArea safeArea = SafeArea.builder()
                .withMargins(50f, 40f, 40f, 40f)
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
                .withFontSize(24)
                .build();

        TextStyle subtitleStyle = TextStyle.builder()
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                .withFontSize(18)
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

        String imagePath = "src/main/java/io/github/joabsonlg/pdfbuilder/examples/chart.jpg";
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
                .withText("2024 Election Results Report")
                .withLevel(HeadingLevel.H1)
                .withStyle(titleStyle)
                .withSpacingBefore(10)
                .withSpacingAfter(10)
                .build());

        builder.addHorizontalRule(new Color(72, 183, 208));

        builder.moveDown(10);

        builder.addHeading(Heading.builder()
                .withText("Executive Summary")
                .withLevel(HeadingLevel.H2)
                .withStyle(subtitleStyle)
                .withSpacingBefore(10)
                .withSpacingAfter(30)
                .build());

        TextStyle defaultStyle = TextStyle.builder()
                .withFont(defaultFont)
                .withFontSize(12f)
                .withColor(Color.BLACK)
                .build();

        builder.addParagraph(Paragraph.builder()
                .addStyledText("This report presents the comprehensive results of the 2024 election, including " +
                        "detailed analysis of voting patterns, candidate performance, and final outcomes for all contested " +
                        "positions. The election saw a total turnout of ", defaultStyle)
                .addStyledText("3,840", boldStyle)
                .addStyledText(" voters, representing ", defaultStyle)
                .addStyledText("78%", boldStyle)
                .addStyledText(" of eligible voters.", defaultStyle)
                .withAlignment(TextAlignment.JUSTIFIED)
                .build());

        builder.moveDown(10);

        builder.addHeading(Heading.builder()
                .withText("President")
                .withLevel(HeadingLevel.H2)
                .withStyle(subtitleStyle)
                .withSpacingBefore(10)
                .withSpacingAfter(30)
                .build());

        File imageFile = new File(imagePath);
        Image mainImage = Image.builder(builder.getDocument(), imageFile)
                .withWidth(560)
                .withAlignment(Image.Alignment.LEFT)
                .withCaption("Image 1: 2024 Election Results")
                .build();

        builder.addImage(mainImage);
        builder.moveDown(10);

        java.util.List<java.util.List<String>> tableData = new ArrayList<>();
        tableData.add(java.util.Arrays.asList("Candidate", "Votes", "Percentage"));
        tableData.add(java.util.Arrays.asList("John Doe", "1,920", "50%"));
        tableData.add(java.util.Arrays.asList("Jane Smith", "1,440", "37.5%"));
        tableData.add(java.util.Arrays.asList("Alice Johnson", "480", "12.5%"));

        Table table = Table.builder()
                .withData(tableData)
                .withColumnWidths(150, 200, 150)
                .withRowHeight(30)
                .withFontSize(12)
                .withHeaderBackgroundColor(new Color(249, 250, 251))
                .withHeaderTextColor(Color.BLACK)
                .withBorderColor(new Color(249, 250, 251))
                .withBorderWidth(1.5f)
                .build();

        builder.addTable(table);
        builder.moveDown(20);

        builder.save("complete-example.pdf");
    }
}
