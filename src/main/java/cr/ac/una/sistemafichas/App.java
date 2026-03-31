package cr.ac.una.sistemafichas;

import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        FlowController.getInstance().goViewReplace("admin/LoginAdminView");

    }

    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/"+/* "admin/" + */fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
       
    }

}