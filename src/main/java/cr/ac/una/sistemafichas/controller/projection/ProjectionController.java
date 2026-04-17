package cr.ac.una.sistemafichas.controller.projection;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.service.TicketService;
import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ProjectionController extends Controller {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lblCompanyName;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblNotice;
    @FXML
    private Label lblPriorityBadge;
    @FXML
    private Label lblCurrentTicket;
    @FXML
    private Label lblCurrentStation;
    @FXML
    private Label lblTicket1;
    @FXML
    private Label lblStation1;
    @FXML
    private Label lblTicket2;
    @FXML
    private Label lblStation2;
    @FXML
    private Label lblTicket3;
    @FXML
    private Label lblStation3;
    @FXML
    private Label lblTicket4;
    @FXML
    private Label lblStation4;

    private static final String CONFIG_PATH = "data/config.json";
    private static final String TICKETS_PATH = "data/tickets.json";
    private static final String BRANCHES_PATH = "data/branches.json";

    private Timeline clockTimeline;
    private Timeline refreshTimeline;
    private Timeline headerTimeline;
    private TranslateTransition marquee;

    private final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM, yyyy");
    private final DateTimeFormatter timeFormatter
            = DateTimeFormatter.ofPattern("HH:mm:ss");
    @FXML
    private Label lblLastCalls;

    @Override
    public void initialize() {
        loadHeader();
        loadNoticeText();
        startClock();
        startHeaderRefresh();
        startMarquee();
        startDataRefresh();
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) {
            return;
        }
        if (lblCompanyName != null) {
            lblCompanyName.setText(config.getCompanyName());
        }
        try {
            File file = new File(config.getLogoPath());
            if (file.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }

    private void startHeaderRefresh() {
        headerTimeline = new Timeline(
                new KeyFrame(Duration.seconds(30), e -> loadHeader())
        );
        headerTimeline.setCycleCount(Timeline.INDEFINITE);
        headerTimeline.play();
    }

    private void loadNoticeText() {
        Type type = new TypeToken<List<Branch>>() {
        }.getType();
        List<Branch> branches = JsonUtil.read(BRANCHES_PATH, type);
        if (branches == null || branches.isEmpty()) {
            return;
        }
        String notice = branches.get(0).getNoticeText();
        if (notice != null && !notice.isBlank()) {
            if (lblNotice != null) {
                lblNotice.setText(notice);
            }
        }
    }

    private void startClock() {
        if (clockTimeline != null) {
            clockTimeline.stop();
        }
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now = LocalDateTime.now();
            if (lblDate != null) {
                lblDate.setText(now.format(dateFormatter));
            }
            if (lblTime != null) {
                lblTime.setText(now.format(timeFormatter));
            }
        }));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
    }

    private long lastModified = 0;

    private void startDataRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    File file = new File("data/Tickets.json");
                    long modified = file.lastModified();
                    if (modified != lastModified) {
                        lastModified = modified;
                        TicketService.getInstance().load();
                        refreshTickets();
                    }
                })
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

//    private void llamarFicha(int numeroTicket, String nombreEstacion) {
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                String texto = "Ticket " + numeroTicket + " pase a la estacion " + nombreEstacion;
//                String os = System.getProperty("os.name").toLowerCase();
//                if (os.contains("win")) {
//                    String script = "Add-Type -AssemblyName System.Speech; "
//                            + "$s = New-Object System.Speech.Synthesis.SpeechSynthesizer; "
//                            + "$s.Speak('" + texto + "')";
//                    new ProcessBuilder("powershell", "-Command", script).start().waitFor();
//                } else if (os.contains("mac")) {
//                    new ProcessBuilder("say", texto).start().waitFor();
//                } else {
//                    new ProcessBuilder("espeak", texto).start().waitFor();
//                }
//                return null;
//            }
//        };
//        new Thread(task).start();
//    }

    private void refreshTickets() {
        TicketService service = TicketService.getInstance();
        List<Ticket> tickets = service.getTickets();
        if (tickets == null) {
            tickets = new ArrayList<>();
        }

        List<Ticket> called = new ArrayList<>();

        for (int i = tickets.size() - 1; i >= 0 && called.size() < 4; i--) {
            if ("called".equals(tickets.get(i).getStatus())) {
                called.add(tickets.get(i));
            }
        }

        Ticket current = service.getLastCalled();
        if (current != null) {
            if (lblCurrentTicket != null) {
                lblCurrentTicket.setText(String.valueOf(current.getNumber()));
            }
            if (lblCurrentStation != null) {
                if (current.getStationName() != null) {
                    lblCurrentStation.setText(current.getStationName());
                } else {
                    lblCurrentStation.setText("---");
                }
            }
        }
        updateRow(called, 0, lblTicket1, lblStation1);
        updateRow(called, 1, lblTicket2, lblStation2);
        updateRow(called, 2, lblTicket3, lblStation3);
        updateRow(called, 3, lblTicket4, lblStation4);
    }

    private void updateRow(List<Ticket> list, int index,
            Label lblTicket, Label lblStation) {
        if (lblTicket == null || lblStation == null) {
            return;
        }
        if (index < list.size()) {
            Ticket t = list.get(index);
            lblTicket.setText(String.valueOf(t.getNumber()));
            if (t.getStationName() != null) {
                lblStation.setText(t.getStationName());
            } else {
                lblStation.setText("---");
            }
        } else {
            lblTicket.setText("---");
            lblStation.setText("---");
        }
    }

    private void startMarquee() {
        if (marquee != null) {
            marquee.stop();
        }
        if (lblNotice == null) {
            return;
        }
        marquee = new TranslateTransition(Duration.seconds(25), lblNotice);
        marquee.setFromX(1100);
        marquee.setToX(-1100);
        marquee.setCycleCount(TranslateTransition.INDEFINITE);
        marquee.play();
    }
}
