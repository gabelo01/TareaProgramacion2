package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;

import java.io.File;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TicketConfirmationController extends Controller {

    @FXML
    private Label lblNumber;
    @FXML
    private Label lblProcedure;
    @FXML
    private Label lblType;
    @FXML
    private ImageView imgLogo;

    private static final String CONFIG_PATH = "data/config.json";

    @Override
    public void initialize() {
        loadLogo();

        javafx.application.Platform.runLater(() -> {

            Ticket t = TicketService.getInstance().getLatestTicket();

            System.out.println("LATEST TICKET = " + (t != null ? t.getNumber() : "NULL"));

            if (t == null) {
                lblNumber.setText("SIN TICKET");
                lblProcedure.setText("No hay datos");
                lblType.setText("");
                return;
            }

            setTicket(t);
        });
    }

    public void setTicket(Ticket t) {

        lblNumber.setText(String.valueOf(t.getNumber()));

        lblProcedure.setText(
                (t.getProcedure() != null && t.getProcedure().getName() != null)
                ? "Trámite: " + t.getProcedure().getName()
                : "Trámite no definido"
        );

        lblType.setText(
                t.getPriority() ? "Tipo: Preferencial" : "Tipo: Normal"
        );

        playAnimation();
        autoClose();
    }

    private void loadLogo() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);

        if (config == null) {
            return;
        }

        try {
            File file = new File(config.getLogoPath());
            if (file.exists()) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }

    private void playAnimation() {

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), lblNumber);
        scale.setFromX(0.3);
        scale.setToX(1);
        scale.setFromY(0.3);
        scale.setToY(1);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), lblNumber);
        fade.setFromValue(0);
        fade.setToValue(1);

        scale.play();
        fade.play();
    }

    private void autoClose() {
        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        pause.setOnFinished(e
                -> FlowController.getInstance().goView("kiosk/LoginKioskView")
        );

        pause.play();
    }
}
