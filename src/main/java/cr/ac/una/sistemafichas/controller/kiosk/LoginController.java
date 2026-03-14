package cr.ac.una.sistemafichas.controller.kiosk;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author agamg
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private PasswordField password;
    @FXML
    private Button ingresar;

    /**
     * Initializes the controller class.
     */
   @Override
public void initialize(URL url, ResourceBundle rb) {
    
    ingresar.setOnAction(event -> validarID());
    
}

@FXML
private void validarID(){

    String id = password.getText();

    if(id.equals("123")){

        try{

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/una/sistemafichas/view/kiosk/SelectProcedures.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) ingresar.getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }

    }else{
        System.out.println("ID incorrecto");
    }

}
    
}
