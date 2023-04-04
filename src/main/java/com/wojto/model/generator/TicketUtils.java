package com.wojto.model.generator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.wojto.exception.TicketPdfCreationException;
import com.wojto.model.Ticket;
import com.wojto.model.unmarshaller.Tickets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class TicketUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketUtils.class);

    public static byte[] createPdfFromTicketList(List<Ticket> tickets) throws DocumentException {
        LOGGER.debug("createPdfFromTicketList method called");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        LOGGER.debug("document opened");

        Font font = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        tickets.stream().forEach(ticket -> {
            LOGGER.debug("Adding ticket to pdf document: " + ticket.toString());
            Paragraph paragraph = new Paragraph("Ticket #" + ticket.getId(), font);
            paragraph.setSpacingAfter(10);
            try {
                document.add(paragraph);
                document.add(new Paragraph("Event ID: " + ticket.getEventId()));
                document.add(new Paragraph("User ID: " + ticket.getUserId()));
                document.add(new Paragraph("Category: " + ticket.getCategory()));
                document.add(new Paragraph("Place: " + ticket.getPlace()));
                document.add(new Paragraph(" "));
            } catch (DocumentException e) {
                document.close();
                String errorMessage = "Error while creating PDF document: " + e.getMessage();
                LOGGER.error(errorMessage);
                throw new TicketPdfCreationException(errorMessage);
            }
        });

        document.close();
        LOGGER.debug("document closed, returning pdf as byte stream");
        return baos.toByteArray();
    }

    public static List<Ticket> createTicketListFromMultipartFile(MultipartFile file) throws Exception {
        LOGGER.debug("createTicketListFromMultipartFile method called");
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Tickets.class);
        marshaller.afterPropertiesSet();
        Unmarshaller unmarshaller = marshaller.createUnmarshaller();
        Tickets ticketsWrapper = (Tickets) unmarshaller.unmarshal(new StreamSource(file.getInputStream()));
        List<Ticket> ticketList = ticketsWrapper.getTickets();
        LOGGER.debug("Tickets unmarshalled: " + ticketList.toString());
        return ticketList;
    }

}
