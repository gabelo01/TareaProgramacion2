package cr.ac.una.sistemafichas.controller.employee;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Employee;
import cr.ac.una.sistemafichas.util.EmployeeSessionManager;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.Formato;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;

import java.lang.reflect.Type;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class StationLoginController extends Controller {

    @FXML
    private MFXPasswordField pswPin;
    @FXML
    private MFXButton btnIngresar;
    @FXML
    private MFXButton btnSalir;

    private static final String EMPLOYEE_PATH = "data/employees.json";

    @Override
    public void initialize() {
        pswPin.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        if (pswPin != null) {
            pswPin.clear();
        }
    }

    @FXML
    private void onKeyPressedIngresar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnIngresar(null);
        }
    }

    @FXML
    private void btnIngresar(ActionEvent event) {

        String pin = pswPin.getText().trim();

        if (pin.isEmpty()) {
            showAlert("Ingrese el PIN");
            return;
        }

        Type type = new TypeToken<List<Employee>>() {
        }.getType();
        List<Employee> employees = JsonUtil.read(EMPLOYEE_PATH, type);

        if (employees == null || employees.isEmpty()) {
            showAlert("No hay empleados registrados");
            return;
        }

        Employee found = employees.stream()
                .filter(e -> e.getPin().equals(pin))
                .findFirst()
                .orElse(null);

        if (found == null) {
            showAlert("PIN incorrecto");
            return;
        }
        EmployeeSessionManager.setBranchName(found.getBranchName());
        EmployeeSessionManager.setStationName(found.getStationName());

        FlowController.getInstance().goView("employee/StationView");
    }

    @FXML
    private void btnSalir(ActionEvent event) {
        getStage().close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
