/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Parent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.dao.InicioSesionDAO;
import javafxappescolar.modelo.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author themr
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private Button btnIngresar;
    @FXML
    private PasswordField tfContrasenia;
    @FXML
    private TextField tfUsuario;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorContrasenia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    @FXML
    private void btnClicVerificar(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfContrasenia.getText();
        
        if(validarCampos(username, password))
            validarCredenciales(username, password);    
    }
    
    private boolean validarCampos(String username, String password){
        
        lbErrorUsuario.setText("");
        lbErrorContrasenia.setText("");
        
        boolean camposValidos = true;
        
        if(username.isEmpty()){
            lbErrorUsuario.setText("Usuario obligatorio");
            camposValidos = false;
        }
        if(password.isEmpty()){
            lbErrorContrasenia.setText("Contraseña obligatoria");
            camposValidos = false;
        }
        return camposValidos;
    }
    private void validarCredenciales(String username, String password){
        //TODO Flujo normal y alterno 1
        try {
            Usuario usuarioSesion = InicioSesionDAO.verificarCredenciales(username, password);
            if(usuarioSesion != null){
                //TODO flujo normal
                
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenido " + usuarioSesion.toString());
                irPantallaPrincipal(usuarioSesion);
                
            }else{
                //TODO flujo alterno 1
                
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales incorrectas", "Usuario y/o contraseña incorrectos");
            }
        } catch (SQLException ex) {
            //TODO flujo excepcion
        
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexión", ex.getMessage());
            
        }
    }
    private void irPantallaPrincipal(Usuario usuarioSesion){
        try {
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            // Parent vista = FXMLLoader.load(getClass().getResource("/javafxappescolar/vista/FXMLPrincipal.fxml"));
            
            FXMLLoader cargador = new FXMLLoader(JavaFXAppEscolar.class.getResource("/javafxappescolar/vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalController controlador = cargador.getController();
            controlador.inicializarInformacion(usuarioSesion);
            Scene escenaPrincipal = new Scene(vista);
            Stage nuevaVentana = new Stage();
            nuevaVentana.setScene(escenaPrincipal);
            nuevaVentana.setTitle("Home");

            escenarioBase.hide(); 
            nuevaVentana.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar la vista principal", e.getMessage());
    }
    }
}
