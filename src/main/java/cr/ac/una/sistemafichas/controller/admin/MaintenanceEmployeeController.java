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

    @FXML
    private ListView<Employee> tblEmployees;
    @FXML
    private MFXTextField txtName;
    @FXML
    private MFXTextField txtId;
    @FXML
    private MFXTextField txtPin;
    @FXML
    private MFXTextField txtBranch;
    @FXML
    private MFXTextField txtStation;
    

    private static final String PATH = "data/employees.json";

    private List<Employee> employees = new ArrayList<>();
    private Employee selected;

    @Override
    public void initialize() {
        load();
        setupSelection();
    }

    private void load() {
        Type type = new TypeToken<List<Employee>>() {
        }.getType();
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
        txtBranch.clear();
        txtId.clear();
        txtName.clear();
        txtPin.clear();
        txtStation.clear();
    }

    @FXML
    private void onSave() {
        try {

            if (txtName.getText().trim().isEmpty()
                    || txtId.getText().trim().isEmpty()
                    || txtPin.getText().trim().isEmpty()
                    || txtBranch.getText().trim().isEmpty()
                    || txtStation.getText().trim().isEmpty()) {

                new Mensaje().showConfirmation("Campos imcompletos", getStage(), "Todos los campos deben estar llenos");

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
                Notifications.create()
                        .title("Guardado correctamente")
                        .text("El empleado ha sido guardado correctamente")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showInformation();
                return;
            }

            JsonUtil.write(PATH, employees);
            load();
            clear();

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
                        .text("No hay ningún empleado seleccionado que se pueda eliminar")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }

            if (new Mensaje().showConfirmation("Eliminar Empleado", getStage(), "¿Esta seguro que desea eliminar el empleado?")) {
                employees.remove(selected);
                JsonUtil.write(PATH, employees);
                load();
                clear();

                Notifications.create()
                        .title("Eliminado correctamente")
                        .text("El empleado ha sido eliminado correctamente")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showInformation();
                return;
            }

        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al eliminar el empleado")
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
        try {
            if (selected == null) {
                Notifications.create()
                        .title("Empleado no seleccionado")
                        .text("No hay un empleado seleccionado que se pueda editar")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(2))
                        .showError();
                return;
            }

            selected.setName(txtName.getText());
            selected.setId(txtId.getText());
            selected.setPin(txtPin.getText());
            selected.setBranchName(txtBranch.getText());
            selected.setStationName(txtStation.getText());
            JsonUtil.write(PATH, employees);
            load();
            clear();

            Notifications.create()
                    .title("Editado correctamente")
                    .text("El empleado ha sido editado correctamente")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showInformation();
            return;
        } catch (Exception ex) {
            Notifications.create()
                    .title("ERROR")
                    .text("Hubo un error al editar el empleado")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(2))
                    .showError();
        }
    }
}
