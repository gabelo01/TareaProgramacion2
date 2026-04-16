package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Employee;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXTextField;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class MaintenanceEmployeeController extends Controller {

    @FXML private ListView<Employee> tblEmployees;
    @FXML private MFXTextField txtName;
    @FXML private MFXTextField txtId;
    @FXML private MFXTextField txtPin;
    @FXML private MFXTextField txtBranch;
    @FXML private MFXTextField txtStation;

    private static final String PATH = "data/employees.json";

    private List<Employee> employees = new ArrayList<>();
    private Employee selected;

    @Override
    public void initialize() {
        load();
        setupSelection();
    }

    private void load() {
        Type type = new TypeToken<List<Employee>>(){}.getType();
        employees = JsonUtil.read(PATH, type);

        if (employees == null) {
            employees = new ArrayList<>();
        }

        tblEmployees.getItems().setAll(employees);
    }

    private void setupSelection() {
        tblEmployees.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            selected = n;
            if (n != null) {
                txtName.setText(n.getName());
                txtId.setText(n.getId());
                txtPin.setText(n.getPin());
                txtBranch.setText(n.getBranchName());
                txtStation.setText(n.getStationName());
            }
        });
    }

    @FXML
    private void onNew() {
        clear();
    }

    @FXML
    private void onSave() {
        try {

            if (txtName.getText().trim().isEmpty()
                    || txtId.getText().trim().isEmpty()
                    || txtPin.getText().trim().isEmpty()
                    || txtBranch.getText().trim().isEmpty()
                    || txtStation.getText().trim().isEmpty()) {

                new Mensaje().showConfirmation("Campos incompletos", getStage(), "Todos los campos deben estar llenos");
                return;
            }

            if (selected == null) {
                Employee e = new Employee(
                        txtName.getText(),
                        txtId.getText(),
                        txtPin.getText(),
                        txtBranch.getText(),
                        txtStation.getText()
                );
                employees.add(e);
            } else {
                selected.setName(txtName.getText());
                selected.setId(txtId.getText());
                selected.setPin(txtPin.getText());
                selected.setBranchName(txtBranch.getText());
                selected.setStationName(txtStation.getText());
            }

            JsonUtil.write(PATH, employees);
            load();
            clear();

            Notifications.create().title("Guardado correctamente").text("El empleado ha sido guardado correctamente")
                    .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(2)).showInformation();

        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al guardar el empleado")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }

    @FXML
    private void onDelete() {
        try {
            if (selected == null) {
                Notifications.create()
                        .title("Empleado no seleccionado")
                        .text("Seleccione un empleado")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }

            if (new Mensaje().showConfirmation("Eliminar Empleado", getStage(), "¿Está seguro?")) {
                employees.remove(selected);
                JsonUtil.write(PATH, employees);
                load();
                clear();

                Notifications.create()
                        .title("Eliminado")
                        .text("Empleado eliminado correctamente")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showInformation();
            }

        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Error al eliminar")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }

    private void clear() {
        txtName.clear();
        txtId.clear();
        txtPin.clear();
        txtBranch.clear();
        txtStation.clear();
        selected = null;
    }

    @FXML
    private void OnActionBtnEditEmployee(ActionEvent event) {
        onSave(); // reutiliza lógica
    }
}