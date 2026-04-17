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
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.util.EmployeeSessionManager;
import cr.ac.una.sistemafichas.util.Formato;
import cr.ac.una.sistemafichas.util.Mensaje;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class BranchMaintenanceController extends Controller {

    @FXML
    private MFXTextField txtBranchName;
    @FXML
    private CheckBox chkActiveBranch;
    @FXML
    private ListView<Branch> tblListBranches;

    private static final String BRANCHES_PATH = "data/branches.json";

    private List<Branch> branches;
    private Branch selectedBranch;

    // ──────────────── INIT ────────────────
    @Override
    public void initialize() {
        loadData();
        setupSelection();
        txtBranchName.delegateSetTextFormatter(Formato.getInstance().letrasFormat(30));
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
    private void onActionBtnAddBranch() {
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
                Notifications.create()
                        .title("Sucursal no seleccionada")
                        .text("No hay ninguna sucursal seleccionada que se pueda eliminar")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }

            if (new Mensaje().showConfirmation("Eliminar Tramite", getStage(), "¿Esta seguro que desea eliminar el cliente?")) {
                branches.remove(selectedBranch);
                JsonUtil.write(BRANCHES_PATH, branches);
                refreshBranches();
                clearBranch();
                Notifications.create()
                        .title("Eliminado correctamente")
                        .text("La sucursal ha sido eliminada correctamente")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showInformation();
                return;
            }
        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al eliminar la sucursal")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }

    }

    private void clearBranch() {
        txtBranchName.clear();
        chkActiveBranch.setSelected(false);
        selectedBranch = null;
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

            String name = txtBranchName.getText().trim();
            if (name.isEmpty()) {
                Notifications.create()
                        .title("Nombre inválido")
                        .text("El nombre no puede estar vacío")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }
            selectedBranch.setName(name);
            selectedBranch.setActive(chkActiveBranch.isSelected());
            for (Station s : selectedBranch.getStations()) {
                s.setBranchName(name);
            }
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
        EmployeeSessionManager.setBranchName(selectedBranch.getName());
        FlowController.getInstance().goView("admin/MaintenanceStationView");

    }

    

}
