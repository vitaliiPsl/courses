package com.example.courses.utils;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class CertificateUtils {
    private static final String FILE = "certificate.pdf";

    private static final Font titleFont = FontFactory.getFont(FontFactory.COURIER, 30, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY);
    private static final Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font nameFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font courseFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font serialNumberFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.WHITE);

    public static byte[] makeCertificate(Course course, User user, int score) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        addTitle(document);
        addContent(document, course, user, score);
        addSerialNumber(document, course.getId() + "" + user.getId());

        document.close();

        return outputStream.toByteArray();
    }

    private static void addTitle(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);

        Paragraph title = new Paragraph("Certificate of completion".toUpperCase(Locale.ROOT), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(title);

        addEmptyLine(paragraph, 2);

        document.add(paragraph);
    }

    private static void addContent(Document document, Course course, User user, int score) throws DocumentException {
        Paragraph text = new Paragraph("This acknowledges that", textFont);
        text.setAlignment(Element.ALIGN_CENTER);
        document.add(text);

        Paragraph name = new Paragraph(user.getFullName(), nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        document.add(name);

        text = new Paragraph("has completed", textFont);
        text.setAlignment(Element.ALIGN_CENTER);
        document.add(text);

        Paragraph courseParagraph = new Paragraph(course.getTitle(), courseFont);
        courseParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(courseParagraph);

        Paragraph scoreParagraph = makeLabelValuePair("Score: ", score + "/" + course.getMaxScore());
        scoreParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(scoreParagraph);

        Paragraph dateParagraph = makeLabelValuePair("Date: ", course.getEndDate().toLocalDate().toString());
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(dateParagraph, 2);
        document.add(dateParagraph);
    }

    private static void addSerialNumber(Document document, String serialNumber) throws DocumentException {
        Chunk chunk = new Chunk("Serial number: " + serialNumber, serialNumberFont);
        chunk.setBackground(BaseColor.GRAY, 10, 10, 10, 10);
        document.add(chunk);
    }

    private static Paragraph makeLabelValuePair(String label, String value) {
        Paragraph paragraph = new Paragraph();

        Chunk labelChunk = new Chunk(label, textFont);
        Chunk valueChunk = new Chunk(value, valueFont);

        paragraph.add(labelChunk);
        paragraph.add(valueChunk);

        return paragraph;

    }
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
