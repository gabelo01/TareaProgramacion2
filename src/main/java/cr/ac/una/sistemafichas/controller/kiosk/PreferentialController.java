package cr.ac.una.sistemafichas.controller.kiosk;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class PreferentialController implements Initializable {

    private PasswordField password;

    private Button getTicket;

    private Button cancel;
    @FXML
    private PasswordField pswPin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        getTicket.setOnAction(e -> validarPin());
        cancel.setOnAction(e -> volver());

    }

    private void validarPin() {

        String pin = password.getText();

        if (pin.equals("123")) {
            System.out.println(1);
        } else {
            System.out.println("PIN incorrecto");
        }
    }

    private void volver() {

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/una/sistemafichas/view/kiosk/SelectProcedures.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OnActionPswPin(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnGetFichaPreferential(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnExit(ActionEvent event) {
    }
}