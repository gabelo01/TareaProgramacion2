package cr.ac.una.sistemafichas.controller.projection;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.JsonUtil;
import java.io.File;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 * @author agamg
 */
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
    private Label lblNewBadge;
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

    
           // ──────────────── PATHS ────────────────
    private static final String CONFIG_PATH = "data/config.json";  
    
          // ──────────────── DATA ────────────────
    private Timeline headerTimeline;
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize() {
        loadHeader();
        startHeaderRefresh();
    }    
    
        private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

        lblCompanyName.setText(config.getCompanyName());

        try {
            File file = new File(config.getLogoPath());
            if (file.exists()) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error loading logo");
        }
       }
        
        private void startHeaderRefresh() {
        headerTimeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> loadHeader()));
        headerTimeline.setCycleCount(Timeline.INDEFINITE);
        headerTimeline.play();
       }
    
}
