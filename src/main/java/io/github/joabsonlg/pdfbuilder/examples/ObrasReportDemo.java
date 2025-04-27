package io.github.joabsonlg.pdfbuilder.examples;

import io.github.joabsonlg.pdfbuilder.components.logo.LogoStyle;
import io.github.joabsonlg.pdfbuilder.components.page.PageNumbering;
import io.github.joabsonlg.pdfbuilder.components.page.PageSectionStyle;
import io.github.joabsonlg.pdfbuilder.components.table.Table;
import io.github.joabsonlg.pdfbuilder.components.text.*;
import io.github.joabsonlg.pdfbuilder.core.PDFBuilder;
import io.github.joabsonlg.pdfbuilder.core.PDFConfiguration;
import io.github.joabsonlg.pdfbuilder.core.SafeArea;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public final class ObrasReportDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObrasReportDemo.class);

    private ObrasReportDemo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void main(String[] args) {
        try {
            // Define as margens
            float leftMargin = 50f;
            float rightMargin = 40f;
            float topMargin = 40f;
            float bottomMargin = 40f;

            SafeArea safeArea = SafeArea.builder()
                    .withMargins(leftMargin, rightMargin, topMargin, bottomMargin)
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

            // Calcula a largura disponível para as tabelas (A4: 595 pontos de largura)
            float pageWidth = PDRectangle.A4.getWidth();
            float availableWidth = pageWidth - leftMargin - rightMargin;

            PDFBuilder builder = new PDFBuilder(config);
            var defaultFont = builder.getResourceManager().getDefaultFont();

            // Cabeçalho com logo e nome da empresa
            LogoStyle logoStyle = LogoStyle.builder()
                    .withFontSize(18f)
                    .withColor(new Color(0, 102, 51))
                    .withMarginBottom(20f)
                    .withDrawLine(true)
                    .withLineWidth(1f)
                    .withLineColor(new Color(43, 43, 43, 128))
                    .withImageHeight(30f)
                    .withImageMargin(10f)
                    .build();
            String leftLogo = "src/main/resources/logo.png";
            String rightLogo = "src/main/resources/logo.png";
            builder.setLogo("EMPRESA MODELO S.A.", logoStyle, leftLogo, rightLogo);

            // Rodapé e numeração de página
            builder.setFooter(PageSectionStyle.createConfidentialFooter("EMPRESA MODELO S.A."));
            PageNumbering pageNumbering = PageNumbering.builder()
                    .withFont(defaultFont)
                    .withFontSize(10)
                    .withColor(new Color(128, 128, 128))
                    .withFormat(PageNumbering.Format.WITH_TOTAL)
                    .withPosition(PageNumbering.Position.BOTTOM)
                    .withAlignment(TextAlignment.RIGHT)
                    .build();
            builder.setPageNumbering(pageNumbering);

            // Título centralizado
            TextStyle titleStyle = TextStyle.builder()
                    .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                    .withFontSize(20)
                    .withColor(new Color(0, 102, 51))
                    .build();
            builder.addHeading(Heading.builder()
                    .withText("Relatório de Informações de Obras")
                    .withLevel(HeadingLevel.H1)
                    .withStyle(titleStyle)
                    .withAlignment(TextAlignment.CENTER)
                    .withSpacingAfter(20)
                    .build());

            // Parágrafo introdutório
            TextStyle introStyle = TextStyle.builder()
                    .withFont(defaultFont)
                    .withFontSize(13f)
                    .withColor(new Color(44, 62, 80))
                    .build();
            builder.moveDown(10); // Adicionando espaçamento antes do parágrafo
            builder.addParagraph(Paragraph.builder()
                    .addStyledText("Este relatório apresenta um panorama consolidado das obras sob responsabilidade " +
                            "da EMPRESA MODELO S.A., incluindo indicadores financeiros, distribuição por assunto e " +
                            "detalhes das principais obras em andamento.", introStyle)
                    .withAlignment(TextAlignment.JUSTIFIED)
                    .build());
            builder.moveDown(15);

            // Adiciona a seção de filtros aplicados
            FilterParameters filterParams = FilterParameters.builder()
                    .addParameter("UF", "RN")
                    .addParameter("Município", "Natal")
                    .addParameter("Assunto", "Todos")
                    .addParameter("Ano de formalização", "2020-2022")
                    .addParameter("Situação", "Em execução")
                    .addParameter("Indicação", "Emenda Parlamentar")
                    .withFontSize(11)
                    .withLabelColor(new Color(0, 102, 51))  // Cor verde para combinar com o tema
                    .withValueColor(new Color(44, 62, 80))   // Cor escura para os valores
                    .build();

            builder.addFilterParameters(filterParams);

            // Adiciona linha horizontal para separar os filtros do conteúdo
            builder.addHorizontalRule(new Color(200, 200, 200));

            // Seção: Dados Gerais
            builder.addHeading(Heading.builder()
                    .withText("1. Indicadores Gerais")
                    .withLevel(HeadingLevel.H3)
                    .withSpacingBefore(10)
                    .withSpacingAfter(15)
                    .build());
            var dadosGerais = new ArrayList<java.util.List<String>>();
            dadosGerais.add(java.util.Arrays.asList("Indicador", "Valor"));
            dadosGerais.add(java.util.Arrays.asList("Valor Global", "R$ 13.864.511.083,60"));
            dadosGerais.add(java.util.Arrays.asList("Valor de repasse", "R$ 9.984.083.632,65"));
            dadosGerais.add(java.util.Arrays.asList("Valor empenhado total", "R$ 7.943.145.296,54"));
            dadosGerais.add(java.util.Arrays.asList("Valor empenhado 2019-2022", "R$ 5.781.686.323,59"));
            dadosGerais.add(java.util.Arrays.asList("Valor pago total", "R$ 7.112.003.520,63"));
            dadosGerais.add(java.util.Arrays.asList("Valor pago 2019-2022", "R$ 4.921.324.101,45"));

            // Distribuição proporcional das colunas (40% para primeira coluna, 60% para segunda)
            float col1Width = availableWidth * 0.4f;
            float col2Width = availableWidth * 0.6f;

            Table tabelaGerais = Table.builder()
                    .withData(dadosGerais)
                    .withColumnWidths(col1Width, col2Width)
                    .withRowHeight(30)
                    .withFontSize(12)
                    .withHeaderBackgroundColor(new Color(249, 250, 251))
                    .withHeaderTextColor(new Color(0, 102, 51))
                    .withBorderColor(new Color(200, 200, 200))
                    .withBorderWidth(1.2f)
                    .build();
            builder.addTable(tabelaGerais);
            builder.moveDown(30);

            // Seção: Assuntos
            builder.addHeading(Heading.builder()
                    .withText("2. Distribuição por Assunto")
                    .withLevel(HeadingLevel.H3)
                    .withSpacingBefore(10)
                    .withSpacingAfter(15)
                    .build());
            var assuntos = new ArrayList<java.util.List<String>>();
            assuntos.add(java.util.Arrays.asList("Assunto", "Valor Global"));
            assuntos.add(java.util.Arrays.asList("Agricultura", "R$ 10.000,00"));
            assuntos.add(java.util.Arrays.asList("Educação", "R$ 500.000,00"));
            assuntos.add(java.util.Arrays.asList("Apoio Financeiro - COVID 19", "R$ 2.000.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Saúde", "R$ 300.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Habitação", "R$ 5.000.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Máquinas/Equip.", "R$ 100.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Mobilidade", "R$ 1.000.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Outros", "R$ 2.500.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Seg. Pública", "R$ 200.000.000,00"));
            assuntos.add(java.util.Arrays.asList("Desenv. Regional e Urbano", "R$ 250.000.000,00"));

            Table tabelaAssuntos = Table.builder()
                    .withData(assuntos)
                    .withColumnWidths(col1Width, col2Width)
                    .withRowHeight(28)
                    .withFontSize(12)
                    .withHeaderBackgroundColor(new Color(249, 250, 251))
                    .withHeaderTextColor(new Color(0, 102, 51))
                    .withBorderColor(new Color(200, 200, 200))
                    .withBorderWidth(1.2f)
                    .build();
            builder.addTable(tabelaAssuntos);
            builder.moveDown(30);

            // Seção: Obras
            builder.addHeading(Heading.builder()
                    .withText("3. Obras em Andamento")
                    .withLevel(HeadingLevel.H3)
                    .withSpacingBefore(10)
                    .withSpacingAfter(15)
                    .build());
            var obras = new ArrayList<java.util.List<String>>();
            obras.add(java.util.Arrays.asList("Obra", "Valor"));
            obras.add(java.util.Arrays.asList("teste 2050", "R$ 0,00"));
            obras.add(java.util.Arrays.asList("Projeto de Sistema de Produção: Integração com tecnologias de acesso a água.", "R$ 955.000,00"));
            obras.add(java.util.Arrays.asList("estruturar com materiais de escritório, mobiliário e equipamentos uma sala sede para o sistema de inspeção municipal (sim)", "R$ 100.650,90"));
            obras.add(java.util.Arrays.asList("Mercado da Carne Augusto Frade", "R$ 1.913.890,00"));
            obras.add(java.util.Arrays.asList("adequação do mercado público do município de jandaíra/rn.", "R$ 419.326,56"));
            obras.add(java.util.Arrays.asList("obras e serviços de engenharia", "R$ 965.000,00"));

            // Para a tabela de obras, usamos uma proporção diferente (70% nome da obra, 30% valor)
            float obra1Width = availableWidth * 0.7f;
            float obra2Width = availableWidth * 0.3f;

            Table tabelaObras = Table.builder()
                    .withData(obras)
                    .withColumnWidths(obra1Width, obra2Width)
                    .withRowHeight(28)
                    .withFontSize(12)
                    .withHeaderBackgroundColor(new Color(249, 250, 251))
                    .withHeaderTextColor(new Color(0, 102, 51))
                    .withBorderColor(new Color(200, 200, 200))
                    .withBorderWidth(1.2f)
                    .build();
            builder.addTable(tabelaObras);

            builder.save("relatorio_obras.pdf");
            LOGGER.info("PDF gerado com sucesso: relatorio_obras.pdf");
        } catch (IOException e) {
            LOGGER.error("Erro ao gerar o PDF: {}", e.getMessage(), e);
        }
    }
}
