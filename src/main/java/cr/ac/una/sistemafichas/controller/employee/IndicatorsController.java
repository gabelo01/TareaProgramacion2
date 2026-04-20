package cr.ac.una.sistemafichas.controller.employee;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class IndicatorsController extends Controller {

    @FXML private ListView<String> listProcedures;
    @FXML private Label lblTotal;

    private static final String STATION_PATH = "data/configStation.json";

    @Override
    public void initialize() {
        loadIndicators();
    }

    private void loadIndicators() {
        listProcedures.getItems().clear();
        Station station = JsonUtil.read(STATION_PATH, Station.class);
        if (station == null) return;

        TicketService.getInstance().load();
        long total = 0;

        for (String procName : station.getProcedureNames()) {
            long count = TicketService.getInstance().getTickets().stream()
                .filter(t -> "waiting".equals(t.getStatus()))
                .filter(t -> t.getProcedure() != null
                    && t.getProcedure().getName().equals(procName))
                .count();
            total += count;
            listProcedures.getItems().add(procName + "  :  " + count);
        }

        lblTotal.setText("Total esperando: " + total);
    }

    @FXML
    private void onActionBtnBack() {
        if (getStage() != null) {
            getStage().close();
        }
    }
}
