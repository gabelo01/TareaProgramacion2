package cr.ac.una.sistemafichas.controller.employee;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CurrentClientController extends Controller {

    @FXML private ImageView imgClient;
    @FXML private Label lblName;
    @FXML private Label lblID;
    @FXML private Label lblProcedure;
    @FXML private Label lblTicketNumber;
    @FXML private Label lblType;
    @FXML
    private MFXButton btnExit;

    @Override
    public void initialize() {
        loadCurrentClient();
    }

    private void loadCurrentClient() {
        Ticket t = TicketService.getInstance().getLastCalled();

        if (t == null) {
            lblName.setText("Ningun Cliente ha sido llamado");
            lblID.setText("N/A");
            lblProcedure.setText("N/A");
            lblTicketNumber.setText("N/A");
            lblType.setText("N/A");
            imgClient.setImage(null);
            return;
        }

        lblTicketNumber.setText(String.valueOf(t.getNumber()));
        lblProcedure.setText(t.getProcedure() != null ? t.getProcedure().getName() : "Tramite es Nulo");
        lblType.setText(t.getPriority() ? " Preferencial" : " Normal");

        if (t.getClient() != null) {
            lblName.setText(t.getClient().getName());
            lblID.setText(t.getClient().getId());
            try {
                File photo = new File(t.getClient().getPhoto());
                if (photo.exists()) {
                    imgClient.setImage(new Image(photo.toURI().toString()));
                } else {
                    imgClient.setImage(null);
                }
            } catch (Exception e) {
                imgClient.setImage(null);
            }
        } else {
            lblName.setText("Anonimo");
            lblID.setText("No se ha registrado");
            imgClient.setImage(null);
        }
    }

    @FXML
    private void onActionBtnBack() {
        if(getStage()!=null){
            getStage().close();
        }
    }
}