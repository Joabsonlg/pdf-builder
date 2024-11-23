package io.github.joabsonlg.pdfbuilder.components.page;

import io.github.joabsonlg.pdfbuilder.components.text.TextAlignment;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Estilos predefinidos para cabeçalhos e rodapés.
 */
public class PageSectionStyle {

    /**
     * Estilo minimalista com linha separadora fina.
     */
    public static PageSection createMinimal(String title) {
        return PageSection.builder()
                .withCenterText(title)
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                .withFontSize(10)
                .withColor(new Color(128, 128, 128))
                .withDrawLine(true)
                .withLineWidth(0.5f)
                .withLineColor(new Color(200, 200, 200))
                .build();
    }

    /**
     * Estilo corporativo com data e numeração de página.
     */
    public static PageSection createCorporate(String company, String documentTitle) {
        return PageSection.builder()
                .withLeftText(company)
                .withCenterText(documentTitle)
                .withRightText(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
                .withFontSize(10)
                .withColor(new Color(68, 68, 68))
                .withDrawLine(true)
                .withLineWidth(0.5f)
                .withLineColor(new Color(68, 68, 68))
                .build();
    }

    /**
     * Estilo moderno com cores mais vivas.
     */
    public static PageSection createModern(String title) {
        return PageSection.builder()
                .withCenterText(title)
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                .withFontSize(12)
                .withColor(new Color(41, 128, 185))
                .withDrawLine(true)
                .withLineWidth(2.0f)
                .withLineColor(new Color(41, 128, 185, 128))
                .build();
    }

    /**
     * Rodapé com numeração de página integrada.
     */
    public static PageSection createPageNumberFooter(String leftText, String centerText, PageNumbering pageNumbering) {
        return PageSection.builder()
                .withLeftText(leftText)
                .withCenterText(centerText)
                .withRightText(pageNumbering != null ? "{pageNumber}" : "")
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                .withFontSize(8)
                .withColor(new Color(128, 128, 128))
                .withDrawLine(true)
                .withLineWidth(0.5f)
                .withLineColor(new Color(200, 200, 200))
                .build();
    }

    /**
     * Rodapé com informações de confidencialidade.
     */
    public static PageSection createConfidentialFooter(String company) {
        return PageSection.builder()
                .withLeftText("© " + LocalDate.now().getYear() + " " + company)
                .withCenterText("CONFIDENCIAL")
                .withRightText("")
                .withFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA))
                .withFontSize(8)
                .withColor(new Color(128, 128, 128))
                .withDrawLine(true)
                .withLineWidth(0.5f)
                .withLineColor(new Color(200, 200, 200))
                .build();
    }
}
