package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.Employee;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.util.Formato;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXTextField;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class MaintenanceEmployeeController extends Controller {

    @FXML private ListView<Employee> tblEmployees;
    @FXML private MFXTextField       txtName;
    @FXML private MFXTextField       txtId;
    @FXML private MFXTextField       txtPin;
    @FXML private ComboBox<String>   cmbBranch;
    @FXML private ComboBox<String>   cmbStation;
    @FXML private TextField          tfC_Name;

    private static final String PATH        = "data/employees.json";
    private static final String BRANCH_PATH = "data/branches.json";

    private List<Employee> employees = new ArrayList<>();
    private List<Employee> allEmployees = new ArrayList<>();
    private List<Branch>   branches  = new ArrayList<>();
    private Employee       selected;

    @Override
    public void initialize() {
        load();
        loadBranches();
        setupSelection();
        setupFilterListeners();
        txtName.delegateSetTextFormatter(Formato.getInstance().letrasFormat(30));
        txtPin.delegateSetTextFormatter(Formato.getInstance().integerFormat());
    }

    private void setupFilterListeners() {
        if (tfC_Name != null) tfC_Name.textProperty().addListener((obs, ov, nv) -> applyFilter());
    }

    private void applyFilter() {
        String filter = tfC_Name != null ? tfC_Name.getText().trim().toLowerCase() : "";
        List<Employee> result = filter.isEmpty()
            ? new ArrayList<>(allEmployees)
            : allEmployees.stream()
                .filter(e -> (e.getName() != null && e.getName().toLowerCase().contains(filter))
                    || (e.getId() != null && e.getId().toLowerCase().contains(filter)))
                .collect(Collectors.toList());
        tblEmployees.getItems().setAll(result);
    }

    private void load() {
        Type type = new TypeToken<List<Employee>>() {}.getType();
        allEmployees = JsonUtil.read(PATH, type);
        if (allEmployees == null) allEmployees = new ArrayList<>();
        employees = allEmployees;
        tblEmployees.getItems().setAll(allEmployees);
    }

    private void loadBranches() {
        Type type = new TypeToken<List<Branch>>() {}.getType();
        branches = JsonUtil.read(BRANCH_PATH, type);
        if (branches == null) branches = new ArrayList<>();

        cmbBranch.getItems().setAll(branches.stream().map(Branch::getName).toList());

        cmbBranch.setOnAction(e -> {
            String branchName = cmbBranch.getValue();
            Branch branch = branches.stream()
                .filter(b -> b.getName().equals(branchName))
                .findFirst().orElse(null);
            if (branch != null && branch.getStations() != null) {
                cmbStation.getItems().setAll(
                    branch.getStations().stream().map(Station::getName).toList()
                );
            }
        });
    }

    private void setupSelection() {
        tblEmployees.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            selected = n;
            if (n != null) {
                txtName.setText(n.getName());
                txtId.setText(n.getId());
                txtPin.setText(n.getPin());
                cmbBranch.setValue(n.getBranchName());

                Branch branch = branches.stream()
                    .filter(b -> b.getName().equals(n.getBranchName()))
                    .findFirst().orElse(null);
                if (branch != null) {
                    cmbStation.getItems().setAll(
                        branch.getStations().stream().map(Station::getName).toList()
                    );
                }
                cmbStation.setValue(n.getStationName());
            }
        });
    }

    @FXML
    private void onActionBtnNew() {
        clear();
    }

    @FXML
    private void onActionBtnSave() {
        try {
            if (txtName.getText().trim().isEmpty() || txtId.getText().trim().isEmpty()
                    || txtPin.getText().trim().isEmpty()
                    || cmbBranch.getValue() == null || cmbStation.getValue() == null) {
                new Mensaje().showConfirmation("Campos incompletos", getStage(), "Todos los campos deben estar llenos");
                return;
            }

            if (selected == null) {
                Employee e = new Employee(
                    txtName.getText(), txtId.getText(), txtPin.getText(),
                    cmbBranch.getValue(), cmbStation.getValue()
                );
                employees.add(e);
            } else {
                selected.setName(txtName.getText());
                selected.setId(txtId.getText());
                selected.setPin(txtPin.getText());
                selected.setBranchName(cmbBranch.getValue());
                selected.setStationName(cmbStation.getValue());
            }

            JsonUtil.write(PATH, employees);
            load();
            applyFilter();
            clear();

            Notifications.create()
                .title("Guardado correctamente").text("Empleado guardado")
                .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(2)).showInformation();
        } catch (Exception ex) {
            Notifications.create()
                .title("ERROR").text("Error al guardar")
                .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(2)).showError();
        }
    }

    @FXML
    private void onActionBtnDelete() {
        if (selected == null) return;
        employees.remove(selected);
        JsonUtil.write(PATH, employees);
        load();
        applyFilter();
        clear();
    }

    private void clear() {
        txtName.clear();
        txtId.clear();
        txtPin.clear();
        cmbBranch.setValue(null);
        cmbStation.setValue(null);
        cmbStation.getItems().clear();
        selected = null;
    }
}
