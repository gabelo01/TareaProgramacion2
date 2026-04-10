/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.sistemafichas.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author diazv
 */
public class CameraController implements Initializable {

    @FXML
    private ImageView imgImageView;
    @FXML
    private MFXButton btnTakePhoto;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void onActionBtnTakePhoto(ActionEvent event) {
    }

    @FXML
    private void onActionBtnsalir(ActionEvent event) {
    }
    
}
