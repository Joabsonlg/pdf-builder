package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.list.List;
import io.github.joabsonlg.pdfbuilder.components.list.List.ListItem;
import io.github.joabsonlg.pdfbuilder.components.text.Heading;
import io.github.joabsonlg.pdfbuilder.components.text.HeadingLevel;
import io.github.joabsonlg.pdfbuilder.components.text.StyledText;
import io.github.joabsonlg.pdfbuilder.components.text.TextAlignment;
import io.github.joabsonlg.pdfbuilder.components.text.TextStyle;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public final class ListDemo {

    private ListDemo() {
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
                    .build();

            // Cria o builder do PDF
            PDFBuilder builder = new PDFBuilder(config);

            // Fonte padrão
            PDFont defaultFont = builder.getResourceManager().getDefaultFont();

            // Estilos de texto
            TextStyle blueStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(HeadingLevel.H1.getFontSize())
                    .withColor(new Color(0, 102, 204))
                    .build();

            TextStyle normalStyle = TextStyle.builder()
                    .withFont(defaultFont)
                    .withFontSize(12)
                    .withColor(Color.BLACK)
                    .build();

            TextStyle boldStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(12)
                    .withColor(Color.BLACK)
                    .build();

            // Adiciona um título
            builder.addHeading(Heading.builder()
                    .withText("Demonstração de Listas")
                    .withLevel(HeadingLevel.H1)
                    .withStyle(blueStyle)
                    .withAlignment(TextAlignment.CENTER)
                    .build());

            // Move para baixo para dar espaço
            builder.moveDown(20);

            // Cria uma lista não ordenada com subitens
            java.util.List<ListItem> items = new ArrayList<>();

            // Item 1 com subitens
            ListItem item1 = new ListItem("Frutas", defaultFont, 12, Color.BLACK);
            item1.addSubItem(new ListItem("Maçã", defaultFont, 12, Color.BLACK));
            item1.addSubItem(new ListItem("Banana", defaultFont, 12, Color.BLACK));
            item1.addSubItem(new ListItem("Laranja", defaultFont, 12, Color.BLACK));
            items.add(item1);

            // Item 2 com subitens
            ListItem item2 = new ListItem("Vegetais", defaultFont, 12, Color.BLACK);
            item2.addSubItem(new ListItem("Cenoura", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Brócolis", defaultFont, 12, Color.BLACK));
            item2.addSubItem(new ListItem("Alface", defaultFont, 12, Color.BLACK));
            items.add(item2);

            // Item 3 sem subitens
            items.add(new ListItem("Grãos", defaultFont, 12, Color.BLACK));

            List unorderedList = List.builder()
                    .withFont(defaultFont)
                    .withFontSize(12)
                    .withListItems(items)
                    .withTextColor(Color.BLACK)
                    .withIndentation(30)
                    .withLineSpacing(8)
                    .withBulletCharacter("•")
                    .build();

            // Adiciona a lista não ordenada ao documento
            builder.addList(unorderedList);

            // Move para baixo
            builder.moveDown(20);

            // Adiciona um subtítulo
            builder.addHeading(Heading.builder()
                    .withText("Lista Ordenada com Subitens")
                    .withLevel(HeadingLevel.H2)
                    .build());

            // Move para baixo
            builder.moveDown(20);

            // Cria uma lista ordenada com subitens
            java.util.List<ListItem> orderedItems = new ArrayList<>();

            // Item 1 com subitens numerados
            ListItem step1 = new ListItem("Preparar a massa", defaultFont, 12, new Color(44, 62, 80));
            step1.setNumber("1.");

            ListItem step1s1 = new ListItem("Misturar farinha e sal", defaultFont, 12, new Color(44, 62, 80));
            step1s1.setNumber("1.1.");

            ListItem step1s2 = new ListItem("Adicionar água aos poucos", defaultFont, 12, new Color(44, 62, 80));
            step1s2.setNumber("1.2.");

            // Item com texto estilizado
            String text = "O entusiasmo é a maior força da ";
            String highlightedWord = "alma";
            String remainingText = ". Conserva-o e nunca te faltará poder para conseguires o que desejas.";

            java.util.List<StyledText> styledTexts = Arrays.asList(
                new StyledText(text, normalStyle),
                new StyledText(highlightedWord, boldStyle),
                new StyledText(remainingText, normalStyle)
            );

            ListItem step1s3 = new ListItem(styledTexts);
            step1s3.setNumber("1.3.");

            step1.addSubItem(step1s1);
            step1.addSubItem(step1s2);
            step1.addSubItem(step1s3);
            orderedItems.add(step1);

            // Item 2 com subitens numerados
            ListItem step2 = new ListItem("Preparar o recheio", defaultFont, 12, new Color(44, 62, 80));
            step2.setNumber("2.");

            ListItem step2s1 = new ListItem("Cortar os ingredientes", defaultFont, 12, new Color(44, 62, 80));
            step2s1.setNumber("2.1.");

            ListItem step2s2 = new ListItem("Temperar a gosto", defaultFont, 12, new Color(44, 62, 80));
            step2s2.setNumber("2.2.");

            step2.addSubItem(step2s1);
            step2.addSubItem(step2s2);
            orderedItems.add(step2);

            // Item 3 com subitens numerados
            ListItem step3 = new ListItem("Finalizar", defaultFont, 12, new Color(44, 62, 80));
            step3.setNumber("3.");

            ListItem step3s1 = new ListItem("Montar a receita", defaultFont, 12, new Color(44, 62, 80));
            step3s1.setNumber("3.1.");

            ListItem step3s2 = new ListItem("Levar ao forno", defaultFont, 12, new Color(44, 62, 80));
            step3s2.setNumber("3.2.");

            step3.addSubItem(step3s1);
            step3.addSubItem(step3s2);
            orderedItems.add(step3);

            List orderedList = List.builder()
                    .withFont(defaultFont)
                    .withFontSize(12)
                    .withListItems(orderedItems)
                    .ordered(true)
                    .withTextColor(new Color(44, 62, 80))
                    .withIndentation(40)
                    .withLineSpacing(10)
                    .withBulletSpacing(20)  // Aumentado o espaçamento do bullet para evitar sobreposição
                    .build();

            // Adiciona a lista ordenada ao documento
            builder.addList(orderedList);

            // Salva o documento
            builder.save("list-demo.pdf");

            System.out.println("PDF gerado com sucesso: list-demo.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
