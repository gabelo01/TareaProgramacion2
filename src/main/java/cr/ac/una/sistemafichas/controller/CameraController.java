package cr.ac.una.sistemafichas.controller;

import cr.ac.una.sistemafichas.service.CamaraService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CameraController implements Initializable {

    @FXML
    private ImageView imgImageView;

    private CamaraService camaraService;
    private AnimationTimer liveTimer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        camaraService = new CamaraService();
        camaraService.openCamera();
        startLivePreview();
    }

    /** Streams live frames into the ImageView using an AnimationTimer. */
    private void startLivePreview() {
        liveTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // ~30 fps → only update every ~33 ms
                if (now - lastUpdate < 33_000_000L) return;
                lastUpdate = now;

                BufferedImage frame = camaraService.captureImage();
                if (frame != null) {
                    imgImageView.setImage(SwingFXUtils.toFXImage(frame, null));
                }
            }
        };
        liveTimer.start();
    }

    private void stopLivePreview() {
        if (liveTimer != null) {
            liveTimer.stop();
        }
    }

    @FXML
    private void onActionBtnTakePhoto(ActionEvent event) {
        // Stop streaming and grab the current frame
        stopLivePreview();
        BufferedImage captured = camaraService.captureImage();
        camaraService.closeCamera();

        if (captured == null) {
            // If capture failed, re-start the preview and do nothing
            camaraService.openCamera();
            startLivePreview();
            return;
        }

        // Close the camera window
        Stage cameraStage = (Stage) imgImageView.getScene().getWindow();
        cameraStage.close();

        // Open the photo-preview window
        openPhotoPreview(captured);
    }

    /** Opens a new Stage with the captured photo and Save/Retake options. */
    private void openPhotoPreview(BufferedImage capturedImage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cr/ac/una/sistemafichas/view/PhotoPreviewView.fxml"));
            Parent root = loader.load();

            PhotoPreviewController previewCtrl = loader.getController();
            previewCtrl.setImage(capturedImage);

            Stage stage = new Stage();
            stage.setTitle("Foto tomada");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(CameraController.class.getName())
                    .log(Level.SEVERE, "Error al abrir la vista de previsualización", ex);
        }
    }
}
