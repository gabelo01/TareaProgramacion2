package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.util.Mensaje;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.controlsfx.control.Notifications;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class ProceduresMaintenanceController extends Controller {

    // ──────────────── FXML ────────────────
    @FXML
    private MFXTextField txtProcedureName;
    @FXML
    private CheckBox chkActiveProcedure;
    @FXML
    private ListView<Procedure> listProcedures;

    // ──────────────── PATHS ────────────────
    private static final String PROCEDURES_PATH = "data/procedures.json";
    private static final String CONFIG_PATH = "data/config.json";

    // ──────────────── DATA ────────────────
    private List<Procedure> procedures;
    private Procedure selectedProcedure;

    // ──────────────── INIT ────────────────
    private List<Node> requeridos = new ArrayList();

    @Override
    public void initialize() {
        loadData();
        setupSelection();
        chkActiveProcedure.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (selectedProcedure != null) {
                selectedProcedure.setActive(newVal);
                JsonUtil.write(PROCEDURES_PATH, procedures);
            }
        });
    }

    private void loadData() {
        Type procedureListType = new TypeToken<List<Procedure>>() {
        }.getType();
        procedures = JsonUtil.read(PROCEDURES_PATH, procedureListType);
        if (procedures == null) {
            procedures = new ArrayList<>();
        }
        refreshProcedures();
    }

    private void refreshProcedures() {
        listProcedures.getItems().setAll(procedures);
    }

    // ──────────────── SELECTION ────────────────
    private void setupSelection() {
        listProcedures.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedProcedure = newVal;
            if (newVal != null) {
                txtProcedureName.setText(newVal.getName());
                chkActiveProcedure.setSelected(newVal.isActive());
            }
        });
    }

    // ──────────────── ACTIONS ────────────────
    @FXML
    private void btnAddProcedure() {
        try {
            String name = txtProcedureName.getText().trim();

            if (name.isEmpty()) {
                Notifications.create()
                        .title("Nombre Vacio")
                        .text("No hay un trámite para agregar")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }

            for (Procedure p : procedures) {
                if (p.getName().equalsIgnoreCase(name)) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Tramite existente", "El trámite ya existe.");
                    return;
                }
            }

            Procedure p = new Procedure(name, chkActiveProcedure.isSelected());
            procedures.add(p);
            JsonUtil.write(PROCEDURES_PATH, procedures);
            refreshProcedures();
            clearProcedure();
            Notifications.create()
                    .title("Agregado correctamente")
                    .text("El trámite ha sido agregado correctamente")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showInformation();
        } catch (Exception e) {
            Notifications.create()
                    .title("ERROR")
                    .text("Error al editar el trámite")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }

    @FXML
    private void btnDeleteProcedure() {
        try {
            if (selectedProcedure == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No se selecciono un tramite", "Seleccione un trámite.");
                return;
            }
            if (new Mensaje().showConfirmation("Eliminar Tramite", getStage(), "¿Esta seguro que desea eliminar el cliente?")) {

                procedures.remove(selectedProcedure);
                JsonUtil.write(PROCEDURES_PATH, procedures);
                refreshProcedures();
                clearProcedure();
                Notifications.create()
                        .title("Eliminado correctamente")
                        .text("El trámite ha sido eliminado correctamente")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showInformation();
            }
        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al eliminar el trámite")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }

    private void clearProcedure() {
        txtProcedureName.clear();
        chkActiveProcedure.setSelected(false);
        selectedProcedure = null;
    }

    @FXML
    private void btnBack() {
        FlowController.getInstance().goViewReplace("admin/SelectMaintenance");
    }

    @FXML
    private void OnActionBtnEditProcedure(ActionEvent event) {
        try {
            if (selectedProcedure == null) {
            Notifications.create()
                    .title("Trámite no seleccionado")
                    .text("No hay un trámite seleccionado que se pueda editar")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
                return;
            }

            selectedProcedure.setName(txtProcedureName.getText());
            JsonUtil.write(PROCEDURES_PATH, procedures);
            refreshProcedures();
            Notifications.create()
                    .title("Editado correctamente")
                    .text("El trámite ha sido editado correctamente")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showInformation();
        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al editar el trámite")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }

}
