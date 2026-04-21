package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.service.CamaraService;
import cr.ac.una.sistemafichas.service.OcrService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class CameraIdController implements Initializable {

    @FXML
    private ImageView imgCamera;

    private CamaraService camaraService;
    private OcrService ocrService;
    private AnimationTimer liveTimer;
    private BiConsumer<String, String> onDataCaptured;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        camaraService = new CamaraService();
        ocrService = new OcrService();
        camaraService.openCamera();
        startLivePreview();

        // Apaga cámara si cierran la ventana con la X
        javafx.application.Platform.runLater(() -> {
            Stage stage = (Stage) imgCamera.getScene().getWindow();
            if (stage != null) {
                stage.setOnCloseRequest(e -> stopAndClose());
            }
        });
    }

    private void startLivePreview() {
        liveTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate < 33_000_000L) {
                    return;
                }
                lastUpdate = now;
                BufferedImage frame = camaraService.captureImage();
                if (frame != null) {
                    imgCamera.setImage(SwingFXUtils.toFXImage(frame, null));
                }
            }
        };
        liveTimer.start();
    }

    private void stopAndClose() {
        if (liveTimer != null) {
            liveTimer.stop();
        }
        camaraService.closeCamera();
    }

    public void setOnDataCaptured(BiConsumer<String, String> onDataCaptured) {
        this.onDataCaptured = onDataCaptured;
    }

    @FXML
    private void onCapture() {
        try {
            if (liveTimer != null) {
                liveTimer.stop();
            }

            BufferedImage frame = camaraService.captureImage();
            camaraService.closeCamera();

            if (frame == null) {
                return;
            }

            File temp = File.createTempFile("ocr_capture", ".png");
            ImageIO.write(frame, "png", temp);

            String id = ocrService.extractId(temp);

            System.out.println("ID detectado: " + id);

            if (onDataCaptured != null && !id.isBlank()) {
                onDataCaptured.accept(id, ""); // nombre vacío por ahora
            }

            ((Stage) imgCamera.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        stopAndClose();
        Stage stage = (Stage) imgCamera.getScene().getWindow();
        stage.close();
    }
}
