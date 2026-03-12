package cr.ac.una.sistemafichas;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class LoginController {

    @FXML
    private AnchorPane root;

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
