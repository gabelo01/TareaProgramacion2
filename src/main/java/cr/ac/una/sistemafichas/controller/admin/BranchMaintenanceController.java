package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.util.Mensaje;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class BranchMaintenanceController extends Controller {

    // ──────────────── FXML ────────────────
    @FXML
    private MFXTextField txtBranchName;
    @FXML
    private CheckBox chkActiveBranch;
    @FXML
    private ListView<Branch> tblListBranches;

    // ──────────────── PATHS ────────────────
    private static final String BRANCHES_PATH = "data/branches.json";

    // ──────────────── DATA ────────────────
    private List<Branch> branches;
    private Branch selectedBranch;

    public class BranchName {

        public static String branchName;
    }

    // ──────────────── INIT ────────────────
    @Override
    public void initialize() {
        loadData();
        setupSelection();
        chkActiveBranch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (selectedBranch != null) {
                selectedBranch.setActive(newVal);
                JsonUtil.write(BRANCHES_PATH, branches);
            }
        });
    }

    // ──────────────── LoadData ────────────────
    private void loadData() {
        Type branchListType = new TypeToken<List<Branch>>() {
        }.getType();
        branches = JsonUtil.read(BRANCHES_PATH, branchListType);
        if (branches == null) {
            branches = new ArrayList<>();
        }
        refreshBranches();
    }

    private void refreshBranches() {
        tblListBranches.getItems().setAll(branches);
    }

    // ──────────────── SELECTION ────────────────
    private void setupSelection() {
        tblListBranches.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedBranch = newVal;
            if (newVal != null) {
                txtBranchName.setText(newVal.getName());
                chkActiveBranch.setSelected(newVal.isActive());
            }
        });
    }

    // ──────────────── ACTIONS ────────────────
    @FXML
    private void btnAddBranch() {
        try {
            String name = txtBranchName.getText().trim();

            if (name.isEmpty()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Nombre vacio", "Ingrese un nombre de sucursal.");
                return;
            }

            for (Branch b : branches) {
                if (b.getName().equalsIgnoreCase(name)) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Sucursal existente", "La sucursal ya existe.");
                    return;
                }
            }

            Branch b = new Branch(name, "", "", new ArrayList<>(), true);
            branches.add(b);
            JsonUtil.write(BRANCHES_PATH, branches);
            refreshBranches();
            clearBranch();
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Sucursal guardada", getStage(), "La sucursal se guardo correctamente");

        } catch (Exception e) {
            Logger.getLogger(ProceduresMaintenanceController.class.getName()).log(Level.SEVERE, "Error agregando la sucursal", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error agregando la sucursal", getStage(), "Hubo un error al agregar la sucursal.");
        }
    }

    @FXML
    private void btnDeleteBranch() {
        try {
            if (selectedBranch == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No se selecciono una sucursal", "Seleccione una sucursal.");
                return;
            }
            if (new Mensaje().showConfirmation("Eliminar Tramite", getStage(), "¿Esta seguro que desea eliminar el cliente?")) {
                branches.remove(selectedBranch);
                JsonUtil.write(BRANCHES_PATH, branches);
                refreshBranches();
                clearBranch();
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Se elimino la sucursal", getStage(), "Se elimino la sucursal correctamente.");
            }
        } catch (Exception ex) {
            Logger.getLogger(ProceduresMaintenanceController.class.getName()).log(Level.SEVERE, "Error eliminando la sucursal", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error eliminando la sucursal", getStage(), "Hubo un error al eliminar la sucursal.");
        }

    }

    private void clearBranch() {
        txtBranchName.clear();
        chkActiveBranch.setSelected(false);
        selectedBranch = null;
    }

    @FXML
    private void onActionBtnBack() {
        FlowController.getInstance().goViewReplace("admin/SelectMaintenance");
    }

    @FXML
    private void OnActionBtnEditBranch(ActionEvent event) {
        try {
            if (selectedBranch == null) {
                Notifications.create()
                        .title("Sucursal no seleccionada")
                        .text("No hay una sucursal seleccionada que se pueda editar")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }

            selectedBranch.setName(txtBranchName.getText());
            JsonUtil.write(BRANCHES_PATH, branches);
            refreshBranches();
            Notifications.create()
                    .title("Editado correctamente")
                    .text("La sucursal ha sido editada correctamente")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showInformation();
        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al editar la sucursal")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }

    @FXML
    private void onActionBtnAddStations(ActionEvent event) {
        if (selectedBranch == null) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Sucursal no Seleccionada", "Seleccione una sucursal");
            return;
        }
        BranchName.branchName = selectedBranch.getName();
        FlowController.getInstance().goView("admin/MaintenanceStationView");

    }

}
