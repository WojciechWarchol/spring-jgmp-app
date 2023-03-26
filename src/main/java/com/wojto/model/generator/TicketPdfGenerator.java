package com.wojto.model.generator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.wojto.model.Ticket;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class TicketPdfGenerator {

    public static byte[] generatePdf(List<Ticket> tickets) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font font = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        for (Ticket ticket : tickets) {
            Paragraph paragraph = new Paragraph("Ticket #" + ticket.getId(), font);
            paragraph.setSpacingAfter(10);
            document.add(paragraph);

            document.add(new Paragraph("Event ID: " + ticket.getEventId()));
            document.add(new Paragraph("User ID: " + ticket.getUserId()));
            document.add(new Paragraph("Category: " + ticket.getCategory()));
            document.add(new Paragraph("Place: " + ticket.getPlace()));
            document.add(new Paragraph(" "));
        }

        document.close();
        return baos.toByteArray();
    }

}
