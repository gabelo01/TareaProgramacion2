package cr.ac.una.sistemafichas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class PreferentialController implements Initializable {

    @FXML
    private PasswordField password;

    @FXML
    private Button getTicket;

    @FXML
    private Button cancel;

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
                getClass().getResource("/cr/ac/una/sistemafichas/view/SelectProcedures.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}