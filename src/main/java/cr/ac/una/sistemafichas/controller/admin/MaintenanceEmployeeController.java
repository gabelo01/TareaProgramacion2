package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Employee;
import cr.ac.una.sistemafichas.util.JsonUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MaintenanceEmployeeController extends Controller {

    @FXML private ListView<Employee> tblEmployees;
    @FXML private TextField txtName, txtId, txtPin, txtBranch, txtStation;

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

        if (employees == null) employees = new ArrayList<>();

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
    }

    @FXML
    private void onDelete() {
        if (selected != null) {
            employees.remove(selected);
            JsonUtil.write(PATH, employees);
            load();
            clear();
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
}