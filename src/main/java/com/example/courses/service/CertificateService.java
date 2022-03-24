package com.example.courses.service;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class CertificateService {
    private static final String FONT = "/fonts/Arial.ttf";

    private final Font titleFont;
    private final Font textFont;
    private final Font valueFont;
    private final Font nameFont;
    private final Font serialNumberFont;
    private final ResourceBundle bundle;

    public CertificateService(String langCode) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        titleFont = new Font(baseFont, 30, Font.BOLD, BaseColor.DARK_GRAY);
        textFont = new Font(baseFont, 12, Font.NORMAL, BaseColor.GRAY);
        valueFont = new Font(baseFont, 12, Font.BOLD, BaseColor.DARK_GRAY);
        nameFont = new Font(baseFont, 16, Font.BOLD, BaseColor.DARK_GRAY);
        serialNumberFont = new Font(baseFont, 10, Font.NORMAL, BaseColor.WHITE);

        Locale locale = new Locale(langCode);
        bundle = ResourceBundle.getBundle("i18n/certificate/certificate", locale);
    }

    public byte[] makeCertificate(Course course, User user, int score) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        addTitle(document);
        addContent(document, course, user, score);
        addSerialNumber(document, UUID.randomUUID().toString().toUpperCase());

        document.close();

        return outputStream.toByteArray();
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);

        Paragraph title = new Paragraph(bundle.getString("label.title").toUpperCase(Locale.ROOT), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(title);

        addEmptyLine(paragraph, 2);

        document.add(paragraph);
    }

    private void addContent(Document document, Course course, User user, int score) throws DocumentException {
        Paragraph text = new Paragraph(bundle.getString("label.acknowledge"), textFont);
        text.setAlignment(Element.ALIGN_CENTER);
        document.add(text);

        Paragraph name = new Paragraph(user.getFullName(), nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        document.add(name);

        text = new Paragraph(bundle.getString("label.completion"), textFont);
        text.setAlignment(Element.ALIGN_CENTER);
        document.add(text);

        Paragraph courseParagraph = new Paragraph(course.getTitle(), nameFont);
        courseParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(courseParagraph);

        Paragraph scoreParagraph = makeLabelValuePair( bundle.getString("label.score") + ": ", score + "/" + course.getMaxScore());
        scoreParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(scoreParagraph);

        Paragraph dateParagraph = makeLabelValuePair(bundle.getString("label.date") + ": ", course.getEndDate().toLocalDate().toString());
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(dateParagraph, 2);
        document.add(dateParagraph);
    }

    private void addSerialNumber(Document document, String serialNumber) throws DocumentException {
        Chunk chunk = new Chunk(bundle.getString("label.serial_number") + ": " + serialNumber, serialNumberFont);
        chunk.setBackground(BaseColor.GRAY, 10, 10, 10, 10);
        document.add(chunk);
    }

    private Paragraph makeLabelValuePair(String label, String value) {
        Paragraph paragraph = new Paragraph();

        Chunk labelChunk = new Chunk(label, textFont);
        Chunk valueChunk = new Chunk(value, valueFont);

        paragraph.add(labelChunk);
        paragraph.add(valueChunk);

        return paragraph;
    }
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
