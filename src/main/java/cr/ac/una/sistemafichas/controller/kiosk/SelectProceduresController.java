package cr.ac.una.sistemafichas.controller.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class SelectProceduresController implements Initializable {

    private Button getTicketButton;

    private Button preferentialButton;
    @FXML
    private Label lblName;
    @FXML
    private MFXButton OnActionBtnPreferential;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        getTicketButton.setOnAction(e ->getPin());
        preferentialButton.setOnAction(event -> {
            try {
                openPreferential();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void openPreferential() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/una/sistemafichas/view/kiosk/Preferential.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) preferentialButton.getScene().getWindow();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    private void getPin(){
    
        System.out.println(2);
    }

    @FXML
    private void OnActionBtnGetTicket(ActionEvent event) {
    }
}