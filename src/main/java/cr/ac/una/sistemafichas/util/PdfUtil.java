package cr.ac.una.sistemafichas.util;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Ticket;
import java.awt.Desktop;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfUtil {

    public static void generateTicketPdf(Ticket ticket, CompanyConfig config) {
        try {
            File folder = new File("data/tickets_pdf");
            if (!folder.exists()) folder.mkdirs();

            String fileName = "ticket_" + ticket.getNumber() + ".pdf";
            File pdfFile = new File(folder, fileName);

            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A6);
            document.setMargins(20, 20, 20, 20);

            // Esta es para intentar cargar el Logo al PDF
            if (config != null && config.getLogoPath() != null) {
                File logoFile = new File(config.getLogoPath());
                if (logoFile.exists()) {
                    Image logo = new Image(
                        ImageDataFactory.create(logoFile.getAbsolutePath()));
                    logo.setWidth(80); //hay que ver si las dimensiones van a ser ajustables en el proyecto final
                    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(logo);
                }
            }

            // Esta es para lograr poner el nombre de la empresa
            String companyName = config != null
                ? config.getCompanyName() : "Sistema de Fichas";
            document.add(new Paragraph(companyName)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14)
                .setBold());

            document.add(new Paragraph(" "));

            // Para el numero de ticket 
            document.add(new Paragraph("TICKET")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16));

            document.add(new Paragraph(String.valueOf(ticket.getNumber()))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(48)
                .setBold());

            document.add(new Paragraph(" "));

            // Para poner el tramite
            if (ticket.getProcedure() != null) {
                document.add(new Paragraph("Trámite: " + ticket.getProcedure().getName())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            }

            // Para poner si es preferencial
            if (ticket.getPriority()) {
                document.add(new Paragraph("PREFERENCIAL")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(13)
                    .setBold()
                    .setFontColor(ColorConstants.RED));
            }

            // Poner info cliente si esta registrado con la cedula
            if (ticket.getClient() != null) {
                document.add(new Paragraph("Cliente: " + ticket.getClient().getName())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(11));
            }

            document.add(new Paragraph(" "));

            // Para poner la fecha y hora
            String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            document.add(new Paragraph(dateTime)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY));

            document.close();

            // Lograr Abrir el pdf
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
        }
    }
}