package cr.ac.una.sistemafichas.service;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;

public class CamaraService {

    private Webcam webcam;

    public Webcam openCamera() {
        if (webcam == null) {
            webcam = Webcam.getDefault();
        }

        if (!webcam.isOpen()) {
            webcam.open();
        }

        return webcam;
    }

    public BufferedImage captureImage() {
        if (webcam != null && webcam.isOpen()) {
            return webcam.getImage();
        }
        return null;
    }

    public void closeCamera() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }
}