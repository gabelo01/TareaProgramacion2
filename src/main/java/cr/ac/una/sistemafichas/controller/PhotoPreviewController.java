package cr.ac.una.sistemafichas.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class PhotoPreviewController implements Initializable {

    @FXML
    private ImageView imgPreview;

    /** The captured image received from CameraController. */
    private BufferedImage capturedImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Image is set after load via setImage()
    }

    /**
     * Called by CameraController right after loading this controller
     * to inject the captured photo.
     */
    public void setImage(BufferedImage image) {
        this.capturedImage = image;
        if (image != null) {
            imgPreview.setImage(SwingFXUtils.toFXImage(image, null));
        }
    }

    /**
     * "Tomar de nuevo" — close this preview window and reopen the camera.
     */
    @FXML
    private void onActionBtnRetake(ActionEvent event) {
        // Close the preview window
        Stage previewStage = (Stage) imgPreview.getScene().getWindow();
        previewStage.close();

        // Reopen the camera window
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cr/ac/una/sistemafichas/view/CameraView.fxml"));
            Parent root = loader.load();

            Stage cameraStage = new Stage();
            cameraStage.setTitle("Cámara");
            cameraStage.setScene(new Scene(root));
            cameraStage.show();

        } catch (IOException ex) {
            Logger.getLogger(PhotoPreviewController.class.getName())
                    .log(Level.SEVERE, "Error al reabrir la cámara", ex);
        }
    }

    /**
     * "Guardar" — save the photo to the fotos-empleados folder and close this window.
     */
    @FXML
    private void onActionBtnSave(ActionEvent event) {
        if (capturedImage == null) return;

        try {
            // Ensure the destination folder exists
            File folder = new File("fotos-empleados");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Unique filename based on timestamp
            String fileName = "empleado_" + System.currentTimeMillis() + ".png";
            File destinationFile = new File(folder, fileName);

            ImageIO.write(capturedImage, "PNG", destinationFile);

            // Notify success and close
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Foto guardada");
            alert.setHeaderText(null);
            alert.setContentText("La foto fue guardada correctamente en:\n" + destinationFile.getAbsolutePath());
            alert.showAndWait();

            Stage previewStage = (Stage) imgPreview.getScene().getWindow();
            previewStage.close();

        } catch (IOException ex) {
            Logger.getLogger(PhotoPreviewController.class.getName())
                    .log(Level.SEVERE, "Error al guardar la foto", ex);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al guardar la foto.");
            alert.showAndWait();
        }
    }
}
