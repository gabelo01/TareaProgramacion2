package cr.ac.una.sistemafichas.service;

import net.sourceforge.tess4j.Tesseract;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class OcrService {

    private final Tesseract tesseract;

    public OcrService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("data/tessdata");

        tesseract.setLanguage("spa");

        tesseract.setPageSegMode(6);
        tesseract.setOcrEngineMode(1);
    }

    public String extractText(File imageFile) {
        try {
            BufferedImage img = ImageIO.read(imageFile);
            BufferedImage processed = preprocess(img);

            return tesseract.doOCR(processed);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private BufferedImage preprocess(BufferedImage img) {
        BufferedImage gray = new BufferedImage(
                img.getWidth(),
                img.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );

        Graphics2D g = gray.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return gray;
    }

    public String extractId(File imageFile) {
        String text = extractText(imageFile);

        System.out.println("OCR OUTPUT: [" + text + "]");

        if (text == null || text.isBlank()) {
            return "";
        }

        String digits = text.replaceAll("[^0-9]", "");

        System.out.println("DIGITS: [" + digits + "]");

        String result = digits.length() >= 8 ? digits : "";

        System.out.println("ID detectado: " + result);

        return result;
    }
}
