package cr.ac.una.sistemafichas.util;

import cr.ac.una.sistemafichas.App;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import cr.ac.una.sistemafichas.controller.Controller;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.scene.layout.HBox;

public class FlowController { //singlenton una sola instancia

    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma; //para manejar diferentes idiomas en la interfaz
    private static HashMap<String, FXMLLoader> loaders = new HashMap<>(); //almacenar todas las vistas FXML que vamos cargando

    private FlowController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FlowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlowController();
                }
            }
        }
    }

    public static FlowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle idioma) {
        getInstance();
        this.mainStage = stage;
        this.idioma = idioma;
    }

    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                if (loader == null) {
                    try {
                        loader = new FXMLLoader(App.class.getResource("view/" + name + ".fxml"), this.idioma); //tener en cuenta el nombre de la vista
                        loader.load(); // lo carga, ejecuta el codigo en el metodo initialize de los controles
                        loaders.put(name, loader); // se guarda en hashMap la vista
                    } catch (Exception ex) {
                        loader = null;
                        java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Creando loader [" + name + "].", ex);
                    }
                }
            }
        }
        return loader;
    }

    public void goMain() { //para mandar a llamar la pantalla principal del sistema
        try {
            this.mainStage.setScene(new Scene(FXMLLoader.load(App.class.getResource("view/PrincipalView.fxml"), this.idioma)));
            MFXThemeManager.addOn(this.mainStage.getScene(), Themes.DEFAULT, Themes.LEGACY);
            this.mainStage.show();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Error inicializando la vista base.", ex);
        }
    }

    public void goView(String viewName) {
        goView(viewName, "Center", null);
    }

    public void goView(String viewName, String accion) {
        goView(viewName, "Center", accion);
    }

    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        //controller.setAccion(accion);
        controller.initialize();
        Stage stage = controller.getStage();
        if (stage == null) {
            stage = this.mainStage;
            controller.setStage(stage);
        }
        switch (location) {
            case "Center":
                
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();
                VBox vBox = (VBox)borderPane.getCenter();
                vBox.getChildren().clear();
                vBox.getChildren().add(loader.getRoot());
                        
                /*VBox vBox = ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter());
                vBox.getChildren().clear();
                vBox.getChildren().add(loader.getRoot());*/
                break;
            case "Top":
                BorderPane borderPane2 = (BorderPane) stage.getScene().getRoot();
                HBox hBox = (HBox)borderPane2.getTop();
                hBox.getChildren().clear();
                hBox.getChildren().add(loader.getRoot());
                break;
            case "Bottom":
                break;
            case "Right":
                break;
            case "Left":
                break;
            default:
                break;
        }
    }

    public void goViewInStage(String viewName, Stage stage) { //mostrar en una ventana en especifico o stage especifico
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.getScene().setRoot(loader.getRoot());
        MFXThemeManager.addOn(stage.getScene(), Themes.DEFAULT, Themes.LEGACY);
        
    }

    public void goViewInWindow(String viewName) { //abre en nueva ventana
        FXMLLoader loader = getLoader(viewName); //get loader busca en hashMap si existe y abre
        Controller controller = loader.getController();// obtiene el controller de ese loader 
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("cr/ac/una/unaplanilla/resource/LogoUNArojo.png"));
        stage.setTitle(controller.getNombreVista());
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("cr/ac/una/unaplanilla/resource/LogoUNArojo.png")); // DUDAS
        stage.setTitle(controller.getNombreVista());
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();

    }
    
    /***************
     * 
     * 
     * Nuevo metodo
     * 
     **************/

    public void goViewReplace(String viewName) {
    try {
        Stage stage = getMainStage();
        Scene scene = new Scene(FXMLLoader.load(
            App.class.getResource("view/" + viewName + ".fxml"), idioma));
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        java.util.logging.Logger.getLogger(FlowController.class.getName())
            .log(Level.SEVERE, "Error cargando vista [" + viewName + "].", ex);
     }
    }
     private Stage getMainStage() {
        if (mainStage != null) return mainStage;
        mainStage = Stage.getWindows()
                    .stream()
                    .filter(w -> w instanceof Stage && w.isShowing())
                    .map(w -> (Stage) w)
                    .findFirst()
                    .orElse(null);
        return mainStage;
    }
      
    
    
    /////////////////////////////////////
    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
    }
    
    public void limpiarLoader(String view){
        this.loaders.remove(view);
    }

    public static void setIdioma(ResourceBundle idioma) {
        FlowController.idioma = idioma;
    }
    
    public void initialize() {
        this.loaders.clear();
    }

    public void salir() {
        this.mainStage.close();
    }
    
}

