package cr.ac.una.sistemafichas.controller.kiosk;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class SelectProceduresController implements Initializable {

    @FXML
    private Button getTicketButton;

    @FXML
    private Button preferentialButton;

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
}