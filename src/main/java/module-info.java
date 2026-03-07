module cr.ac.una.sistemafichas {
    requires javafx.controls;
    requires javafx.fxml;

    opens cr.ac.una.sistemafichas to javafx.fxml;
    exports cr.ac.una.sistemafichas;
}
