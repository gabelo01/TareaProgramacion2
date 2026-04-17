package cr.ac.una.sistemafichas;

import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cr.ac.una.sistemafichas.service.CamaraService;
import cr.ac.una.sistemafichas.util.KioskSessionManager;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override

    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader();

        String mode = getParameters().getRaw().isEmpty()
                ? ""
                : getParameters().getRaw().get(0);

        switch (mode) {
            case "kiosk":
                FlowController.getInstance().goViewInWindow("kiosk/LoginKioskView");
                break;

            case "employee":
                FlowController.getInstance().goViewInWindow("employee/StationLogin");
                break;

            case "admin":
                FlowController.getInstance().goViewInWindow("admin/LoginAdminView");
                break;

            case "projection":
                FlowController.getInstance().goViewInWindow("projection/Projection");
                break;

            default:
                FlowController.getInstance().goViewInWindow("kiosk/LoginKioskView");
        }
        stage.show();
        //FlowController.getInstance().InitializeFlow(stage, null);
        //FXMLLoader loader = new FXMLLoader();

        //Kiosko
        //KioskSessionManager.setBranch("Buenos Aires");
        //FlowController.getInstance().goMain("kiosk/LoginKioskView");
        //Admin
        //FlowController.getInstance().goViewInWindow("admin/LoginAdminView");
        //employee
        //lowController.getInstance().goViewInWindow("employee/StationLogin");
        //FlowController.getInstance().goViewInWindow("projection/Projection");
//FlowController.getInstance().goViewInWindow("admin/SelectMaintenance");
        //FlowController.getInstance().goViewInWindow("kiosk/LoginKioskView");
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" +/* "admin/" + */ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
