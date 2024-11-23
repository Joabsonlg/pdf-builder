package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.table.Table;
import io.github.joabsonlg.pdfbuilder.components.text.Heading;
import io.github.joabsonlg.pdfbuilder.components.text.HeadingLevel;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public final class TableDemo {
    private TableDemo() {
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

            // Adiciona um título
            builder.addHeading(Heading.builder()
                    .withText("Demonstração de Tabelas")
                    .withLevel(HeadingLevel.H1)
                    .build());

            // Move para baixo para dar espaço
            builder.moveDown(20);

            // Cria os dados da tabela simples
            List<List<String>> tableData = Arrays.asList(
                    Arrays.asList("Nome", "Idade", "Cidade"),
                    Arrays.asList("João", "25", "São Paulo"),
                    Arrays.asList("Maria", "30", "Rio de Janeiro"),
                    Arrays.asList("Pedro", "28", "Belo Horizonte")
            );

            // Cria uma tabela simples
            Table simpleTable = Table.builder()
                    .withData(tableData)
                    .withColumnWidths(200, 100, 200)
                    .withRowHeight(30)
                    .withFontSize(12)
                    .withHeaderBackgroundColor(new Color(200, 200, 200))
                    .build();

            // Adiciona a tabela ao documento
            builder.addTable(simpleTable);

            // Move para baixo
            builder.moveDown(40);

            // Cria uma tabela mais estilizada
            Table styledTable = Table.builder()
                    .withData(tableData)
                    .withColumnWidths(200, 100, 200)
                    .withRowHeight(40)
                    .withFontSize(14)
                    .withHeaderBackgroundColor(new Color(51, 122, 183))
                    .withHeaderTextColor(Color.WHITE)
                    .withBorderColor(new Color(51, 122, 183))
                    .withBorderWidth(1.5f)
                    .build();

            // Adiciona a tabela estilizada ao documento
            builder.addTable(styledTable);

            // Move para baixo
            builder.moveDown(40);

            // Cria os dados da tabela com muitas colunas
            List<List<String>> wideTableData = Arrays.asList(
                    Arrays.asList("ID", "Nome", "Idade", "Cidade", "Estado", "País", "Profissão", "Departamento",
                            "Salário", "Data de Admissão"),
                    Arrays.asList("1", "João Silva", "25", "São Paulo", "SP", "Brasil", "Desenvolvedor", "TI", "5000",
                            "01/01/2020"),
                    Arrays.asList("2", "Maria Santos", "30", "Rio de Janeiro", "RJ", "Brasil", "Gerente", "RH", "7000",
                            "15/03/2018"),
                    Arrays.asList("3", "Pedro Costa", "28", "Belo Horizonte", "MG", "Brasil", "Analista", "Financeiro",
                            "6000", "22/07/2019")
            );

            // Cria uma tabela com muitas colunas
            Table wideTable = Table.builder()
                    .withData(wideTableData)
                    .withColumnWidths(50, 150, 50, 100, 50, 50, 100, 100, 100, 100)
                    .withRowHeight(40)
                    .withFontSize(12)
                    .withHeaderBackgroundColor(new Color(46, 139, 87))
                    .withHeaderTextColor(Color.WHITE)
                    .withBorderColor(new Color(46, 139, 87))
                    .withBorderWidth(1.0f)
                    .build();

            // Adiciona a tabela larga ao documento
            builder.addTable(wideTable);

            // Salva o documento
            builder.save("table-demo.pdf");

            System.out.println("PDF gerado com sucesso: table-demo.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
