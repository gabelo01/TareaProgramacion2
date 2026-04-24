package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXListView;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class IndicatorsController extends Controller {

    @FXML
    private TabPane tabPane;

    @FXML
    private MFXListView<String> listTopClients;
    @FXML
    private MFXListView<String> listTopBranches;
    @FXML
    private MFXListView<String> listTopStations;
    @FXML
    private MFXListView<String> listTopProcedures;
    @FXML
    private MFXListView<String> listTopPreferentials;

    @FXML
    private BarChart<String, Number> barChartClients;
    @FXML
    private BarChart<String, Number> barChartBranches;
    @FXML
    private BarChart<String, Number> barChartStations;
    @FXML
    private BarChart<String, Number> barChartProcedures;
    @FXML
    private BarChart<String, Number> barChartPreferentials;

    @FXML
    private TextField tfC_Name;
    @FXML
    private TextField tfC_Id;
    @FXML
    private TextField tfC_Branch;
    @FXML
    private TextField tfC_Procedure;
    @FXML
    private DatePicker dpC_DateFrom;
    @FXML
    private DatePicker dpC_DateTo;
    @FXML
    private ToggleButton toggleC_Preferential;

    @FXML
    private TextField tfS_Branch;
    @FXML
    private DatePicker dpS_DateFrom;
    @FXML
    private DatePicker dpS_DateTo;
    @FXML
    private ToggleButton toggleS_Stations;
    @FXML
    private ToggleButton toggleS_Procedures;
    @FXML
    private ToggleButton toggleS_Preferential;

    @FXML
    private TextField tfCJ_Station;
    @FXML
    private TextField tfCJ_Branch;
    @FXML
    private TextField tfCJ_Procedure;
    @FXML
    private ToggleButton toggleCJ_Preferential;

    @FXML
    private TextField tfT_Procedure;
    @FXML
    private TextField tfT_Branch;
    @FXML
    private DatePicker dpT_DateFrom;
    @FXML
    private DatePicker dpT_DateTo;
    @FXML
    private ToggleButton toggleT_Active;

    @FXML
    private TextField tfP_Branch;
    @FXML
    private TextField tfP_Procedure;
    @FXML
    private DatePicker dpP_DateFrom;
    @FXML
    private DatePicker dpP_DateTo;

    private List<Client> allClients = new ArrayList<>();
    private List<Branch> allBranches = new ArrayList<>();
    private List<Ticket> allTickets = new ArrayList<>();
    private List<Procedure> allProcedures = new ArrayList<>();
    private boolean filtersVisible = true;

    @FXML
    private VBox filClients;
    @FXML
    private VBox filBranches;
    @FXML
    private VBox filStation;
    @FXML
    private VBox filProcedures;
    @FXML
    private VBox filPreferenciales;

    private static class TicketFilter {

        String branch;
        String procedure;
        String station;
        LocalDate from;
        LocalDate to;
        boolean preferentialOnly;
        boolean activeOnly;
    }

    private TicketFilter buildFilter(TextField tfBranch, TextField tfProcedure,
            TextField tfStation, DatePicker dpFrom,
            DatePicker dpTo, ToggleButton togglePref,
            ToggleButton toggleActive) {
        TicketFilter f = new TicketFilter();
        f.branch = emptyToNull(getText(tfBranch));
        f.procedure = emptyToNull(getText(tfProcedure));
        f.station = emptyToNull(getText(tfStation));
        f.from = getDate(dpFrom);
        f.to = getDate(dpTo);
        f.preferentialOnly = isOn(togglePref);
        f.activeOnly = isOn(toggleActive);
        return f;
    }

    @Override
    public void initialize() {
        loadData();
        setupListeners();
        refreshAllTabs();
    }

    private void loadData() {
        Type clientType = new TypeToken<List<Client>>() {
        }.getType();
        List<Client> c = JsonUtil.read("data/clients.json", clientType);
        allClients = (c != null) ? c : new ArrayList<>();

        Type branchType = new TypeToken<List<Branch>>() {
        }.getType();
        List<Branch> b = JsonUtil.read("data/branches.json", branchType);
        allBranches = (b != null) ? b : new ArrayList<>();

        Type ticketType = new TypeToken<List<Ticket>>() {
        }.getType();
        List<Ticket> t = JsonUtil.read("data/tickets.json", ticketType);
        allTickets = (t != null) ? t : new ArrayList<>();

        Type procType = new TypeToken<List<Procedure>>() {
        }.getType();
        List<Procedure> p = JsonUtil.read("data/procedures.json", procType);
        allProcedures = (p != null) ? p : new ArrayList<>();
    }

    private void setupListeners() {
        addTextListener(tfC_Name, this::refreshTabClients);
        addTextListener(tfC_Id, this::refreshTabClients);
        addTextListener(tfC_Branch, this::refreshTabClients);
        addTextListener(tfC_Procedure, this::refreshTabClients);
        addDateListener(dpC_DateFrom, this::refreshTabClients);
        addDateListener(dpC_DateTo, this::refreshTabClients);

        addTextListener(tfS_Branch, this::refreshTabBranches);
        addDateListener(dpS_DateFrom, this::refreshTabBranches);
        addDateListener(dpS_DateTo, this::refreshTabBranches);

        addTextListener(tfCJ_Station, this::refreshTabStations);
        addTextListener(tfCJ_Branch, this::refreshTabStations);
        addTextListener(tfCJ_Procedure, this::refreshTabStations);

        addTextListener(tfT_Procedure, this::refreshTabProcedures);
        addTextListener(tfT_Branch, this::refreshTabProcedures);
        addDateListener(dpT_DateFrom, this::refreshTabProcedures);
        addDateListener(dpT_DateTo, this::refreshTabProcedures);

        addTextListener(tfP_Branch, this::refreshTabPreferentials);
        addTextListener(tfP_Procedure, this::refreshTabPreferentials);
        addDateListener(dpP_DateFrom, this::refreshTabPreferentials);
        addDateListener(dpP_DateTo, this::refreshTabPreferentials);
    }

    private void addTextListener(TextField tf, Runnable action) {
        if (tf != null) {
            tf.textProperty().addListener((obs, ov, nv) -> action.run());
        }
    }

    private void addDateListener(DatePicker dp, Runnable action) {
        if (dp != null) {
            dp.valueProperty().addListener((obs, ov, nv) -> action.run());
        }
    }

    private void refreshAllTabs() {
        refreshTabClients();
        refreshTabBranches();
        refreshTabStations();
        refreshTabProcedures();
        refreshTabPreferentials();
    }

    private void refreshActiveTab() {
        if (tabPane == null) {
            return;
        }
        switch (tabPane.getSelectionModel().getSelectedIndex()) {
            case 0 ->
                refreshTabClients();
            case 1 ->
                refreshTabBranches();
            case 2 ->
                refreshTabStations();
            case 3 ->
                refreshTabProcedures();
            case 4 ->
                refreshTabPreferentials();
            default ->
                refreshAllTabs();
        }
    }

    private void refreshTabClients() {
        TicketFilter f = buildFilter(tfC_Branch, tfC_Procedure, null,
                dpC_DateFrom, dpC_DateTo,
                toggleC_Preferential, null);

        List<Client> result = new ArrayList<>(allClients);

        String nameFilter = emptyToNull(getText(tfC_Name));
        if (nameFilter != null) {
            result = result.stream()
                    .filter(cl -> cl.getName() != null
                    && cl.getName().toLowerCase().contains(nameFilter))
                    .collect(Collectors.toList());
        }

        String idFilter = emptyToNull(getText(tfC_Id));
        if (idFilter != null) {
            result = result.stream()
                    .filter(cl -> cl.getId() != null
                    && cl.getId().toLowerCase().contains(idFilter))
                    .collect(Collectors.toList());
        }

        if (f.branch != null) {
            Set<String> ids = filterClientIdsByTickets(f.branch, null, null, null, false);
            result = result.stream().filter(cl -> ids.contains(cl.getId())).collect(Collectors.toList());
        }

        if (f.procedure != null) {
            Set<String> ids = filterClientIdsByTickets(null, f.procedure, null, null, false);
            result = result.stream().filter(cl -> ids.contains(cl.getId())).collect(Collectors.toList());
        }

        if (f.from != null || f.to != null) {
            Set<String> ids = filterClientIdsByTickets(null, null, f.from, f.to, false);
            result = result.stream().filter(cl -> ids.contains(cl.getId())).collect(Collectors.toList());
        }

        if (f.preferentialOnly) {
            result = result.stream().filter(Client::isPreferential).collect(Collectors.toList());
        }

        renderClientList(result, listTopClients, barChartClients,
                "Trámites realizados",
                "No hay clientes con los filtros seleccionados.", false);
    }

    private void refreshTabBranches() {
        TicketFilter f = buildFilter(tfS_Branch, null, null,
                dpS_DateFrom, dpS_DateTo,
                toggleS_Preferential, null);

        List<Branch> result = new ArrayList<>(allBranches);

        if (f.branch != null) {
            result = result.stream()
                    .filter(br -> br.getName() != null
                    && br.getName().toLowerCase().contains(f.branch))
                    .collect(Collectors.toList());
        }

        if (f.from != null || f.to != null) {
            Set<String> names = filterBranchNamesByTickets(f.from, f.to);
            result = result.stream()
                    .filter(br -> names.contains(br.getName()))
                    .collect(Collectors.toList());
        }

        if (isOn(toggleS_Preferential)) {
            result = result.stream()
                    .filter(br -> br.getStations() != null
                    && br.getStations().stream().anyMatch(Station::isPreferential))
                    .collect(Collectors.toList());
        }
        if (isOn(toggleS_Stations)) {
            result = result.stream()
                    .filter(br -> br.getStations() != null && !br.getStations().isEmpty())
                    .collect(Collectors.toList());
        }
        if (isOn(toggleS_Procedures)) {
            result = result.stream()
                    .filter(br -> br.getStations() != null
                    && br.getStations().stream().anyMatch(st
                            -> st.getProcedureNames() != null && !st.getProcedureNames().isEmpty()))
                    .collect(Collectors.toList());
        }

        renderBranchList(result);
    }

    private void refreshTabStations() {
        TicketFilter f = buildFilter(tfCJ_Branch, tfCJ_Procedure, tfCJ_Station,
                null, null,
                toggleCJ_Preferential, null);

        List<Station> allStations = allBranches.stream()
                .filter(br -> br.getStations() != null)
                .flatMap(br -> br.getStations().stream())
                .collect(Collectors.toList());

        List<Station> result = new ArrayList<>(allStations);

        if (f.station != null) {
            result = result.stream()
                    .filter(st -> st.getName() != null
                    && st.getName().toLowerCase().contains(f.station))
                    .collect(Collectors.toList());
        }

        if (f.branch != null) {
            result = result.stream()
                    .filter(st -> st.getBranchName() != null
                    && st.getBranchName().toLowerCase().contains(f.branch))
                    .collect(Collectors.toList());
        }

        if (f.procedure != null) {
            result = result.stream()
                    .filter(st -> st.getProcedureNames() != null
                    && st.getProcedureNames().stream()
                            .anyMatch(p -> p.toLowerCase().contains(f.procedure)))
                    .collect(Collectors.toList());
        }

        if (f.preferentialOnly) {
            result = result.stream().filter(Station::isPreferential).collect(Collectors.toList());
        }

        renderStationList(result);
    }

    private void refreshTabProcedures() {
        TicketFilter f = buildFilter(tfT_Branch, tfT_Procedure, null,
                dpT_DateFrom, dpT_DateTo,
                null, toggleT_Active);

        List<Procedure> result = new ArrayList<>(allProcedures);

        if (f.procedure != null) {
            result = result.stream()
                    .filter(pr -> pr.getName() != null
                    && pr.getName().toLowerCase().contains(f.procedure))
                    .collect(Collectors.toList());
        }

        if (f.branch != null) {
            Set<String> procNames = filterProcedureNamesByBranch(f.branch);
            result = result.stream()
                    .filter(pr -> procNames.contains(pr.getName()))
                    .collect(Collectors.toList());
        }

        if (f.from != null || f.to != null) {
            Set<String> procNames = filterProcedureNamesByDateRange(f.from, f.to);
            result = result.stream()
                    .filter(pr -> procNames.contains(pr.getName()))
                    .collect(Collectors.toList());
        }

        if (f.activeOnly) {
            result = result.stream().filter(Procedure::isActive).collect(Collectors.toList());
        }

        renderProcedureList(result);
    }

    private void refreshTabPreferentials() {
        TicketFilter f = buildFilter(tfP_Branch, tfP_Procedure, null,
                dpP_DateFrom, dpP_DateTo,
                null, null);

        List<Client> result = allClients.stream()
                .filter(Client::isPreferential)
                .collect(Collectors.toList());

        Set<String> existingIds = result.stream().map(Client::getId).collect(Collectors.toSet());
        List<Client> prefFromTickets = allTickets.stream()
                .filter(tk -> tk.getClient() != null && tk.getPriority()
                && !existingIds.contains(tk.getClient().getId()))
                .map(Ticket::getClient)
                .distinct()
                .collect(Collectors.toList());

        result = new ArrayList<>(result);
        result.addAll(prefFromTickets);

        if (f.branch != null) {
            Set<String> ids = filterClientIdsByTickets(f.branch, null, null, null, true);
            result = result.stream().filter(cl -> ids.contains(cl.getId())).collect(Collectors.toList());
        }

        if (f.procedure != null) {
            Set<String> ids = filterClientIdsByTickets(null, f.procedure, null, null, true);
            result = result.stream().filter(cl -> ids.contains(cl.getId())).collect(Collectors.toList());
        }

        if (f.from != null || f.to != null) {
            Set<String> ids = filterClientIdsByTickets(null, null, f.from, f.to, true);
            result = result.stream().filter(cl -> ids.contains(cl.getId())).collect(Collectors.toList());
        }

        List<String> seen = new ArrayList<>();
        List<Client> unique = new ArrayList<>();
        for (Client cl : result) {
            if (!seen.contains(cl.getId())) {
                seen.add(cl.getId());
                unique.add(cl);
            }
        }

        renderClientList(unique, listTopPreferentials, barChartPreferentials,
                "Trámites realizados",
                "No hay clientes preferenciales con los filtros seleccionados.", true);
    }

    private Set<String> filterClientIdsByTickets(String branch, String procedure,
            LocalDate from, LocalDate to,
            boolean preferentialOnly) {
        return allTickets.stream()
                .filter(tk -> tk.getClient() != null)
                .filter(tk -> branch == null || (tk.getBranchName() != null
                && tk.getBranchName().toLowerCase().contains(branch)))
                .filter(tk -> procedure == null || (tk.getProcedure() != null
                && tk.getProcedure().getName() != null
                && tk.getProcedure().getName().toLowerCase().contains(procedure)))
                .filter(tk -> (from == null && to == null) || isInDateRange(tk.getCreationDate(), from, to))
                .filter(tk -> !preferentialOnly || tk.getPriority())
                .map(tk -> tk.getClient().getId())
                .collect(Collectors.toSet());
    }

    private Set<String> filterBranchNamesByTickets(LocalDate from, LocalDate to) {
        return allTickets.stream()
                .filter(tk -> tk.getBranchName() != null)
                .filter(tk -> (from == null && to == null) || isInDateRange(tk.getCreationDate(), from, to))
                .map(Ticket::getBranchName)
                .collect(Collectors.toSet());
    }

    private Set<String> filterProcedureNamesByBranch(String branch) {
        return allTickets.stream()
                .filter(tk -> tk.getProcedure() != null && tk.getProcedure().getName() != null
                && tk.getBranchName() != null
                && tk.getBranchName().toLowerCase().contains(branch))
                .map(tk -> tk.getProcedure().getName())
                .collect(Collectors.toSet());
    }

    private Set<String> filterProcedureNamesByDateRange(LocalDate from, LocalDate to) {
        return allTickets.stream()
                .filter(tk -> tk.getProcedure() != null && tk.getProcedure().getName() != null
                && isInDateRange(tk.getCreationDate(), from, to))
                .map(tk -> tk.getProcedure().getName())
                .collect(Collectors.toSet());
    }

    private Map<String, Long> countTicketsByClient() {
        return allTickets.stream()
                .filter(tk -> tk.getClient() != null)
                .collect(Collectors.groupingBy(tk -> tk.getClient().getId(), Collectors.counting()));
    }

    private Map<String, Long> countTicketsByBranch() {
        return allTickets.stream()
                .filter(tk -> tk.getBranchName() != null)
                .collect(Collectors.groupingBy(Ticket::getBranchName, Collectors.counting()));
    }

    private Map<String, Long> countTicketsByProcedure() {
        return allTickets.stream()
                .filter(tk -> tk.getProcedure() != null && tk.getProcedure().getName() != null)
                .collect(Collectors.groupingBy(tk -> tk.getProcedure().getName(), Collectors.counting()));
    }

    private void renderClientList(List<Client> result, MFXListView<String> list,
            BarChart<String, Number> chart, String seriesName,
            String emptyMessage, boolean preferentialBadge) {
        Map<String, Long> count = countTicketsByClient();
        result.sort((a, b) -> Long.compare(count.getOrDefault(b.getId(), 0L),
                count.getOrDefault(a.getId(), 0L)));

        List<String> items = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Client cl = result.get(i);
            long total = count.getOrDefault(cl.getId(), 0L);
            String pref = preferentialBadge
                    ? "  ⭐ Preferencial"
                    : (cl.isPreferential() ? "  ⭐ Preferencial" : "");
            items.add(String.format("%d. %s  |  Cédula: %s  |  Trámites: %d%s",
                    i + 1, cl.getName(), cl.getId(), total, pref));
        }
        if (items.isEmpty()) {
            items.add(emptyMessage);
        }
        list.setItems(FXCollections.observableArrayList(items));

        List<String[]> chartData = result.stream().limit(3)
                .map(cl -> new String[]{cl.getName(), String.valueOf(count.getOrDefault(cl.getId(), 0L))})
                .collect(Collectors.toList());
        updateChart(chart, chartData, seriesName);
    }

    private void renderBranchList(List<Branch> result) {
        Map<String, Long> count = countTicketsByBranch();
        result.sort((a, b) -> Long.compare(count.getOrDefault(b.getName(), 0L),
                count.getOrDefault(a.getName(), 0L)));

        List<String> items = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Branch br = result.get(i);
            int cajas = br.getStations() != null ? br.getStations().size() : 0;
            long tickets = count.getOrDefault(br.getName(), 0L);
            String estado = br.isActive() ? "Activa" : "Inactiva";
            items.add(String.format("%d. %s  |  Cajas: %d  |  Tickets: %d  |  %s",
                    i + 1, br.getName(), cajas, tickets, estado));
        }
        if (items.isEmpty()) {
            items.add("No hay sucursales con los filtros seleccionados.");
        }
        listTopBranches.setItems(FXCollections.observableArrayList(items));

        List<String[]> chartData = result.stream().limit(3)
                .map(br -> new String[]{br.getName(), String.valueOf(count.getOrDefault(br.getName(), 0L))})
                .collect(Collectors.toList());
        updateChart(barChartBranches, chartData, "Tickets atendidos");
    }

    private void renderStationList(List<Station> result) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Station st = result.get(i);
            String pref = st.isPreferential() ? "  ⭐ Preferencial" : "";
            int numP = st.getProcedureNames() != null ? st.getProcedureNames().size() : 0;
            items.add(String.format("%d. %s  |  Sucursal: %s  |  Trámites: %d%s",
                    i + 1, st.getName(), st.getBranchName(), numP, pref));
        }
        if (items.isEmpty()) {
            items.add("No hay cajas con los filtros seleccionados.");
        }
        listTopStations.setItems(FXCollections.observableArrayList(items));

        List<String[]> chartData = result.stream().limit(3)
                .map(st -> new String[]{st.getName(), "0"})
                .collect(Collectors.toList());
        updateChart(barChartStations, chartData, "Trámites asignados");
    }

    private void renderProcedureList(List<Procedure> result) {
        Map<String, Long> count = countTicketsByProcedure();
        result.sort((a, b) -> Long.compare(count.getOrDefault(b.getName(), 0L),
                count.getOrDefault(a.getName(), 0L)));

        List<String> items = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Procedure pr = result.get(i);
            long total = count.getOrDefault(pr.getName(), 0L);
            String estado = pr.isActive() ? "Activo" : "Inactivo";
            items.add(String.format("%d. %s  |  Tickets: %d  |  %s",
                    i + 1, pr.getName(), total, estado));
        }
        if (items.isEmpty()) {
            items.add("No hay trámites con los filtros seleccionados.");
        }
        listTopProcedures.setItems(FXCollections.observableArrayList(items));

        List<String[]> chartData = result.stream().limit(3)
                .map(pr -> new String[]{pr.getName(), String.valueOf(count.getOrDefault(pr.getName(), 0L))})
                .collect(Collectors.toList());
        updateChart(barChartProcedures, chartData, "Tickets generados");
    }

    private void updateChart(BarChart<String, Number> chart, List<String[]> data, String seriesName) {
        if (chart == null) {
            return;
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        for (String[] entry : data) {
            try {
                series.getData().add(new XYChart.Data<>(entry[0], Long.parseLong(entry[1])));
            } catch (NumberFormatException ignored) {
            }
        }
        chart.getData().clear();
        chart.getData().add(series);
    }

    private boolean isInDateRange(String creationDate, LocalDate from, LocalDate to) {
        if (creationDate == null || creationDate.length() < 10) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(creationDate.substring(0, 10));
            if (from != null && date.isBefore(from)) {
                return false;
            }
            if (to != null && date.isAfter(to)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private String getText(TextField tf) {
        return tf != null ? tf.getText().trim().toLowerCase() : "";
    }

    private String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    private LocalDate getDate(DatePicker dp) {
        return dp != null ? dp.getValue() : null;
    }

    private boolean isOn(ToggleButton tb) {
        return tb != null && tb.isSelected();
    }

    private void OnActionFilterDates(ActionEvent event) {
        refreshActiveTab();
    }

    private void OnActionFilterBranches(ActionEvent event) {
        refreshActiveTab();
    }

    @FXML
    private void OnActionFilterStations(ActionEvent event) {
        refreshActiveTab();
    }

    @FXML
    private void OnActionFilterProcedures(ActionEvent event) {
        refreshActiveTab();
    }

    @FXML
    private void OnActionFilterPreferential(ActionEvent event) {
        refreshActiveTab();
    }

    private void OnActionFilterName(ActionEvent event) {
        refreshTabClients();
    }

    private void OnActionFilterId(ActionEvent event) {
        refreshTabClients();
    }

    @FXML
    private void OnActionBtnFilter(ActionEvent event) {

        filtersVisible = !filtersVisible;

        setFilterVisibility(filClients, filtersVisible);
        setFilterVisibility(filBranches, filtersVisible);
        setFilterVisibility(filStation, filtersVisible);
        setFilterVisibility(filProcedures, filtersVisible);
        setFilterVisibility(filPreferenciales, filtersVisible);
    }

    private void setFilterVisibility(VBox box, boolean visible) {
        if (box == null) {
            return;
        }

        box.setVisible(visible);
        box.setManaged(visible);
    }
}
