package cr.ac.una.sistemafichas.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import java.awt.Desktop;

public class PdfUtil {

    public static void generateTicketPdf(Ticket ticket, CompanyConfig config) {

        try {
            File folder = new File("data/tickets_pdf");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = "ticket_" + ticket.getNumber() + ".pdf";
            File pdfFile = new File(folder, fileName);

            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A6);
            document.setMargins(20, 20, 20, 20);

            if (config != null && config.getLogoPath() != null) {
                File logoFile = new File(config.getLogoPath());
                if (logoFile.exists()) {
                    Image logo = new Image(
                            ImageDataFactory.create(logoFile.getAbsolutePath()));
                    logo.setWidth(80);
                    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(logo);
                }
            }

            String companyName = config != null
                    ? config.getCompanyName()
                    : "Sistema de Fichas";

            document.add(new Paragraph(companyName)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14)
                    .setBold());

            document.add(new Paragraph(" "));

            document.add(new Paragraph("TICKET")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16));

            document.add(new Paragraph(String.valueOf(ticket.getNumber()))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(48)
                    .setBold());

            document.add(new Paragraph(" "));

            if (ticket.getProcedure() != null) {
                document.add(new Paragraph("Trámite: " + ticket.getProcedure().getName())
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(12));
            }

            if (ticket.getPriority()) {
                document.add(new Paragraph("PREFERENCIAL")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(13)
                        .setBold()
                        .setFontColor(ColorConstants.RED));
            }

            if (ticket.getClient() != null) {
                document.add(new Paragraph("Cliente: " + ticket.getClient().getName())
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(11));
            }

            document.add(new Paragraph(" "));

            // datos del qr
            String qrData = ticket.getNumber()
                    + "|" + ticket.getBranchName()
                    + "|" + (ticket.getProcedure() != null
                    ? ticket.getProcedure().getName()
                    : "N/A");

            BufferedImage qrImage = generateQR(qrData, 150, 150);

            if (qrImage != null) {
                Image qr = new Image(ImageDataFactory.create(toByteArray(qrImage)));
                qr.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(qr);
            }

            document.add(new Paragraph(" "));

            // fecha
            String dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            document.add(new Paragraph(dateTime)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY));

            document.close();

            //abrir pdf
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
        }
    }

    //generar lo del qr
    private static BufferedImage generateQR(String text, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }

            return image;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] toByteArray(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
