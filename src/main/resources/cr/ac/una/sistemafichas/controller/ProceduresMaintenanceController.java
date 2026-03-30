
package cr.ac.una.sistemafichas.controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author agamg
 */
public class ProceduresMaintenanceController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lbCompany;
    @FXML
    private ListView<?> listProcedures;
    @FXML
    private MFXTextField txtProcedureName;
    @FXML
    private CheckBox chkActiveProcedure;
    @FXML
    private ListView<?> listProcedures1;
    @FXML
    private MFXTextField txtBranchName;
    @FXML
    private CheckBox chkActiveBranch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onDeleteProcedure(ActionEvent event) {
    }

    @FXML
    private void onAddProcedure(ActionEvent event) {
    }

    @FXML
    private void onDeleteBranch(ActionEvent event) {
    }


    @FXML
    private void onUpdate(ActionEvent event) {
    }

    @FXML
    private void onBack(ActionEvent event) {
    }

    @FXML
    private void onAdd(ActionEvent event) {
    }
    
}
