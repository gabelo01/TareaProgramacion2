package cr.ac.una.sistemafichas.controller;

import cr.ac.una.sistemafichas.model.*;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BranchesController extends Controller {

    @FXML private MFXTextField txtBranchName;
    @FXML private MFXTextField txtBranchDir;
    @FXML private MFXTextField txtNotice;
    @FXML private MFXTextField txtStationName;
    @FXML private CheckBox chkPreferential;
    @FXML private MFXButton btnAddBranch;
    @FXML private MFXButton btnUpdateBranch;
    @FXML private MFXButton btnDeleteBranch;
    @FXML private MFXButton btnAddStation;
    @FXML private MFXButton btnRemoveProcedure;
    @FXML private MFXButton btnVolver;
    @FXML private ListView<String> listBranches;
    @FXML private ListView<String> listStations;
    @FXML private ListView<String> listAvailableProcedures;
    @FXML private ListView<String> listStationProcedures;
    @FXML private Label lbCompany;
    @FXML private ImageView imgLogo;

    private static final String BRANCHES_PATH   = "data/branches.json";
    private static final String PROCEDURES_PATH = "data/procedures.json";
    private static final String CONFIG_PATH     = "data/config.json";

    private BranchesWrapper branchesData;
    private ProceduresWrapper proceduresData;
    private Branch selectedBranch;
    private Station selectedStation;

    @Override
    public void initialize() {
        cargarEncabezado();
        cargarDatos();
        configurarSeleccionSucursal();
        configurarSeleccionEstacion();
        configurarDragAndDrop();
    }

    // ── Carga inicial ──────────────────────────────────────────

    private void cargarDatos() {
        branchesData = JsonUtil.read(BRANCHES_PATH, BranchesWrapper.class);
        if (branchesData == null) {
            branchesData = new BranchesWrapper();
            branchesData.branches = new ArrayList<>();
        }

        proceduresData = JsonUtil.read(PROCEDURES_PATH, ProceduresWrapper.class);
        if (proceduresData == null) {
            proceduresData = new ProceduresWrapper();
            proceduresData.procedures = new ArrayList<>();
        }

        refrescarListaSucursales();
    }

    private void refrescarListaSucursales() {
        listBranches.getItems().clear();
        for (Branch b : branchesData.branches) {
            listBranches.getItems().add(b.getName());
        }
        limpiarPanelEstaciones();
    }

    private void refrescarListaEstaciones() {
        listStations.getItems().clear();
        if (selectedBranch == null || selectedBranch.getStations() == null) return;
        for (Station s : selectedBranch.getStations()) {
            String label = s.getName() + (s.isPreferential() ? " ⭐" : "");
            listStations.getItems().add(label);
        }
        limpiarPanelTramites();
    }

    private void refrescarTramites() {
        listAvailableProcedures.getItems().clear();
        listStationProcedures.getItems().clear();

        if (selectedStation == null) return;

        List<String> asignados = selectedStation.getProcedureNames();
        if (asignados == null) asignados = new ArrayList<>();

        // trámites activos no asignados aún
        for (Procedure p : proceduresData.procedures) {
            if (p.isActive() && !asignados.contains(p.getName())) {
                listAvailableProcedures.getItems().add(p.getName());
            }
        }

        listStationProcedures.getItems().addAll(asignados);
    }

    private void limpiarPanelEstaciones() {
        listStations.getItems().clear();
        limpiarPanelTramites();
        selectedBranch = null;
        selectedStation = null;
    }

    private void limpiarPanelTramites() {
        listAvailableProcedures.getItems().clear();
        listStationProcedures.getItems().clear();
        selectedStation = null;
    }

    // ── Selección ──────────────────────────────────────────────

    private void configurarSeleccionSucursal() {
        listBranches.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (idx < 0 || idx >= branchesData.branches.size()) return;
            selectedBranch = branchesData.branches.get(idx);
            txtBranchName.setText(selectedBranch.getName());
            txtBranchDir.setText(selectedBranch.getDirection());
            txtNotice.setText(selectedBranch.getNoticeText() != null ? selectedBranch.getNoticeText() : "");
            refrescarListaEstaciones();
        });
    }

    private void configurarSeleccionEstacion() {
        listStations.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (selectedBranch == null) return;
            List<Station> stations = selectedBranch.getStations();
            if (stations == null || idx < 0 || idx >= stations.size()) return;
            selectedStation = stations.get(idx);
            txtStationName.setText(selectedStation.getName());
            chkPreferential.setSelected(selectedStation.isPreferential());
            refrescarTramites();
        });
    }

    // ── Drag and Drop ─────────────────────────────────────────

    private void configurarDragAndDrop() {
        // Arrastrar desde disponibles
        listAvailableProcedures.setOnDragDetected(event -> {
            String selected = listAvailableProcedures.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            Dragboard db = listAvailableProcedures.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(selected);
            db.setContent(content);
            event.consume();
        });

        // Soltar en asignados
        listStationProcedures.setOnDragOver(event -> {
            if (event.getGestureSource() == listAvailableProcedures
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        listStationProcedures.setOnDragDropped(event -> {
            String procedureName = event.getDragboard().getString();
            if (selectedStation == null) return;
            if (selectedStation.getProcedureNames() == null) {
                selectedStation.setProcedureNames(new ArrayList<>());
            }
            selectedStation.getProcedureNames().add(procedureName);
            JsonUtil.write(BRANCHES_PATH, branchesData);
            refrescarTramites();
            event.setDropCompleted(true);
            event.consume();
        });

        // Arrastrar desde asignados de vuelta a disponibles
        listStationProcedures.setOnDragDetected(event -> {
            String selected = listStationProcedures.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            Dragboard db = listStationProcedures.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(selected);
            db.setContent(content);
            event.consume();
        });

        listAvailableProcedures.setOnDragOver(event -> {
            if (event.getGestureSource() == listStationProcedures
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        listAvailableProcedures.setOnDragDropped(event -> {
            String procedureName = event.getDragboard().getString();
            if (selectedStation == null) return;
            selectedStation.getProcedureNames().remove(procedureName);
            JsonUtil.write(BRANCHES_PATH, branchesData);
            refrescarTramites();
            event.setDropCompleted(true);
            event.consume();
        });
    }

    // ── Acciones botones ──────────────────────────────────────

    @FXML
    private void btnAddBranch() {
        String nombre = txtBranchName.getText().trim();
        String dir    = txtBranchDir.getText().trim();
        String notice = txtNotice.getText().trim();

        if (nombre.isEmpty() || dir.isEmpty()) return;

        Branch nueva = new Branch(nombre, dir, notice, new ArrayList<>());
        branchesData.branches.add(nueva);
        JsonUtil.write(BRANCHES_PATH, branchesData);
        refrescarListaSucursales();
        limpiarCamposSucursal();
    }

    @FXML
    private void btnUpdateBranch() {
        if (selectedBranch == null) return;
        selectedBranch.setName(txtBranchName.getText().trim());
        selectedBranch.setDirection(txtBranchDir.getText().trim());
        selectedBranch.setNoticeText(txtNotice.getText().trim());
        JsonUtil.write(BRANCHES_PATH, branchesData);
        refrescarListaSucursales();
    }

    @FXML
    private void btnDeleteBranch() {
        if (selectedBranch == null) return;
        branchesData.branches.remove(selectedBranch);
        JsonUtil.write(BRANCHES_PATH, branchesData);
        refrescarListaSucursales();
        limpiarCamposSucursal();
    }

    @FXML
    private void btnAddStation() {
        if (selectedBranch == null) return;
        String nombre = txtStationName.getText().trim();
        if (nombre.isEmpty()) return;

        if (selectedBranch.getStations() == null) {
            selectedBranch.setStations(new ArrayList<>());
        }

        Station nueva = new Station(
            selectedBranch.getStations().size() + 1,
            nombre,
            chkPreferential.isSelected(),
            new ArrayList<>()
        );
        selectedBranch.getStations().add(nueva);
        JsonUtil.write(BRANCHES_PATH, branchesData);
        refrescarListaEstaciones();
        txtStationName.clear();
        chkPreferential.setSelected(false);
    }

    @FXML
    private void btnRemoveProcedure() {
        if (selectedStation == null) return;
        String selected = listStationProcedures.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        selectedStation.getProcedureNames().remove(selected);
        JsonUtil.write(BRANCHES_PATH, branchesData);
        refrescarTramites();
    }

    @FXML
    private void btnVolver() {
        FlowController.getInstance().goView("SelectMaintenance");
    }

    // ── Utilidades ────────────────────────────────────────────

    private void limpiarCamposSucursal() {
        txtBranchName.clear();
        txtBranchDir.clear();
        txtNotice.clear();
        selectedBranch = null;
        selectedStation = null;
    }

    private void cargarEncabezado() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;
        if (lbCompany != null) lbCompany.setText(config.getCompanyName());
        try {
            File logoFile = new File(config.getLogoPath());
            if (logoFile.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(logoFile.toURI().toString()));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo.");
        }
    }
}