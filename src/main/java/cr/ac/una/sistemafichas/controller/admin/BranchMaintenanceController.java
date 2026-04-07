package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.CompanyConfig;
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
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BranchMaintenanceController extends Controller {

    // ──────────────── FXML ────────────────

    @FXML private MFXTextField txtBranchName;
    @FXML private CheckBox chkActiveBranch;
    @FXML private ListView<Branch> listBranches;
    @FXML private Label lbCompany;
    @FXML private ImageView imgLogo;

    // ──────────────── PATHS ────────────────

    private static final String BRANCHES_PATH = "data/branches.json";
    private static final String CONFIG_PATH = "data/config.json";

    // ──────────────── DATA ────────────────

    private List<Branch> branches;
    private Branch selectedBranch;

    // ──────────────── INIT ────────────────

    @Override
    public void initialize() {
        loadHeader();
        loadData();
        setupSelection();
    }

    private void loadData() {
        Type branchListType = new TypeToken<List<Branch>>(){}.getType();
        branches = JsonUtil.read(BRANCHES_PATH, branchListType);
        if (branches == null) branches = new ArrayList<>();
        refreshBranches();
    }

    private void refreshBranches() {
        listBranches.getItems().setAll(branches);
    }

    // ──────────────── SELECTION ────────────────

    private void setupSelection() {
        listBranches.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedBranch = newVal;
            if (newVal != null) {
                txtBranchName.setText(newVal.getName());
            }
        });
    }

    // ──────────────── ACTIONS ────────────────

    @FXML
    private void btnAddBranch() {
        String name = txtBranchName.getText().trim();

        if (name.isEmpty()) {
            showAlert("Ingrese un nombre de sucursal.");
            return;
        }

        for (Branch b : branches) {
            if (b.getName().equalsIgnoreCase(name)) {
                showAlert("La sucursal ya existe.");
                return;
            }
        }

        Branch b = new Branch(name, "", "", new ArrayList<>());
        branches.add(b);
        JsonUtil.write(BRANCHES_PATH, branches);
        refreshBranches();
        clearBranch();
    }

    @FXML
    private void btnDeleteBranch() {
        if (selectedBranch == null) {
            showAlert("Seleccione una sucursal.");
            return;
        }

        branches.remove(selectedBranch);
        JsonUtil.write(BRANCHES_PATH, branches);
        refreshBranches();
        clearBranch();
    }

    private void clearBranch() {
        txtBranchName.clear();
        chkActiveBranch.setSelected(false);
        selectedBranch = null;
    }

    @FXML
    private void btnBack() {
        FlowController.getInstance().goViewReplace("admin/SelectMaintenance");
    }

    // ──────────────── HEADER ────────────────

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

        if (lbCompany != null) lbCompany.setText(config.getCompanyName());

        try {
            File file = new File(config.getLogoPath());
            if (file.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error loading logo");
        }
    }
}
