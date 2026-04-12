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
import java.io.IOException;

public class App extends Application {

    private static Scene scene;

//
//    @Override
//    public void start(Stage stage) throws IOException {
//        scene = new Scene(loadFXML("LoginAdminView"), 640, 480);
//        stage.setScene(scene);
//        stage.show();
//    }
    @Override
    public void start(Stage stage) throws Exception {

        FlowController.getInstance().InitializeFlow(stage, null);

        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(getClass().getResource("/cr/ac/una/sistemafichas/view/CameraView.fxml"));
        //Parent root = loader.load();

        //stage.setScene(new Scene(root));
        //stage.setTitle("Cámara");
        //stage.show();
        //FlowController.getInstance().goViewFirst("admin/LoginAdminView");
        //FlowController.getInstance().goViewFirst("employee/StationView");
        //FlowController.getInstance().goViewFirst("kiosk/SelectProcedures");
        FlowController.getInstance().goViewFirst("projection/Projection");

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" +/* "admin/" + */ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();

    }

}
