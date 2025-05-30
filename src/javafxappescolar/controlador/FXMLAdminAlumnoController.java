/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.ResultadoOperacion;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author themr
 */
public class FXMLAdminAlumnoController implements Initializable, INotificacion {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Alumno> tvAlumnos;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colApePaterno;
    @FXML
    private TableColumn colApeMaterno;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colFacultad;
    @FXML
    private TableColumn colCarrera;

    private ObservableList<Alumno> alumnos = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    } 
    
    private void configurarTabla(){
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApePaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApeMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colFacultad.setCellValueFactory(new PropertyValueFactory<>("facultad"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        
    }
    
    private void cargarInformacionTabla(){
        try {
            alumnos = FXCollections.observableArrayList();
            ArrayList<Alumno> alumnosDAO = AlumnoDAO.obtenerAlumnos();
            alumnos.addAll(alumnosDAO);
            tvAlumnos.setItems(alumnos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al cargar alumnos: " + ex.getMessage());
            cerrarVentana();
        }
    }

    private void cerrarVentana(){
        ((Stage) tfBuscar.getScene().getWindow()).close();
    }
    
    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioAlumno(false, null);
    }

    @FXML
    private void btnClicModificar(ActionEvent event) {
        Alumno alumnoSeleccionado = tvAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            irFormularioAlumno(true, alumnoSeleccionado);
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selecciona un alumno", "Por favor selecciona un alumno de la tabla para modificarlo.");
        }
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        Alumno alumnoSeleccionado = tvAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            boolean confirmacion = Utilidad.mostrarDialogoConfirmacion("Confirmar eliminación", "¿Estás seguro de eliminar al alumno(a) " + alumnoSeleccionado.getNombre() + "?");
            if (confirmacion) {
                try {
                    ResultadoOperacion resultado = AlumnoDAO.eliminarAlumno(alumnoSeleccionado.getIdAlumno());
                    Alert.AlertType tipoAlerta = resultado.isError() ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
                    Utilidad.mostrarAlertaSimple(tipoAlerta,
                                                resultado.isError() ? "Error" : "Éxito",
                                                resultado.getMensaje());

                    if (!resultado.isError()) {
                        cargarInformacionTabla(); 
                    }
                } catch (SQLException ex) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al eliminar",
                                                "No se pudo eliminar el alumno: " + ex.getMessage());
                }
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selecciona un alumno",
                                        "Por favor selecciona un alumno de la tabla para eliminarlo.");
        }
    }

    private void irFormularioAlumno(boolean esEdicion, Alumno alumnoEdicion){
        try {
            Stage escenarioFormulario = new Stage();
            FXMLLoader loader = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLFormularioAlumno.fxml"));
            Parent vista = loader.load();
            FXMLFormularioAlumnoController controlador = loader.getController();
            controlador.inicializarInformacion(esEdicion, alumnoEdicion, this);
            
            Scene escena = new Scene(vista);
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Formulario de Alumno");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void operacionExitosa(String tipo, String nombreAlumno) {
        System.out.println("Operación: " + tipo+" con el alumno(a): " +nombreAlumno);
        cargarInformacionTabla();
    }
}
