package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.KioskSessionManager;
import com.google.gson.reflect.TypeToken;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;

import java.lang.reflect.Type;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SelectBranchKioskController extends Controller {

    @FXML
    private MFXListView<String> listBranches;
    private static final String BRANCH_PATH = "data/branches.json";

    @Override
    public void initialize() {
        loadBranches();
    }

    private void loadBranches() {

        Type type = new TypeToken<List<Branch>>(){}.getType();
        List<Branch> branches = JsonUtil.read(BRANCH_PATH, type);

        if (branches == null) return;

        List<String> activeBranches = branches.stream()
                .filter(Branch::isActive)
                .map(Branch::getName)
                .toList();

        listBranches.setItems(FXCollections.observableArrayList(activeBranches));
    }

    @FXML
    private void OnActionBtnIngresar(ActionEvent event) {

        String selected = listBranches.getSelectionModel().getSelectedValue();

        if (selected == null) {
            showAlert("Seleccione una sucursal");
            return;
        }

        KioskSessionManager.setBranch(selected);
        FlowController.getInstance().goViewInWindow("kiosk/LoginKioskView");
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}