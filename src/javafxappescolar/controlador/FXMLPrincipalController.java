/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author themr
 */
public class FXMLPrincipalController implements Initializable {

    
    private Usuario usuarioSesion;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUsuario;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnCarreraXFacultad;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    private void cargarInformacionUsuario(){
        if(usuarioSesion != null){
            lbNombre.setText(usuarioSesion.toString());
            lbUsuario.setText(usuarioSesion.getUsername());
        }
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
        //TODO hacer de tarea
        try {
        Stage escenarioBase = (Stage) lbNombre.getScene().getWindow();
        escenarioBase.setScene(new Scene(FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLInicioSesion.fxml"))));
        escenarioBase.setTitle("Inicio de sesión");
        escenarioBase.showAndWait();
        usuarioSesion = null;
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAdminAlumnos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLAdminAlumno.fxml"));
            Parent vista = loader.load();

            Stage escenarioAdmin = new Stage();
            Scene escena = new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.setTitle("Administrador de alumnos");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar vista", "No se pudo cargar la ventana de administración de alumnos." + ex.getMessage());
        }
    }

    @FXML
    private void btnClicCarreraXFacultad(ActionEvent event) {
        try {
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLAdminFacultad.fxml"));
            Scene escena = new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.setTitle("Carreras por facultad");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        } catch (IOException e) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
