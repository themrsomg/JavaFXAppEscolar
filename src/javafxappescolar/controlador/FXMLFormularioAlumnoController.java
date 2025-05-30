/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.dao.CatalogoDAO;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.Carrera;
import javafxappescolar.modelo.pojo.Facultad;
import javafxappescolar.modelo.pojo.ResultadoOperacion;
import javafxappescolar.modelo.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author themr
 */
public class FXMLFormularioAlumnoController implements Initializable {

    @FXML
    private ImageView ivFoto;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApePaterno;
    @FXML
    private TextField tfApeMaterno;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfEmail;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private ComboBox<Facultad> cbFacultad;
    @FXML
    private ComboBox<Carrera> cbCarrera;
    @FXML
    private Button btnSeleccionarFoto;
    
    ObservableList<Facultad> facultades;
    ObservableList<Carrera> carreras;
    File archivoFoto;
    INotificacion observador;
    Alumno alumnoEdicion;
    boolean esEdicion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarFacultades();
        seleccionarFacultad();
    }    

    public void inicializarInformacion(boolean esEdicion, Alumno alumnoEdicion, INotificacion observador){
        this.esEdicion = esEdicion;
        this.alumnoEdicion = alumnoEdicion;
        this.observador = observador;
        if(esEdicion)
            cargarInformacionEdicion();
    }
    
    private void cargarInformacionEdicion(){
        try {
            tfNombre.setText(alumnoEdicion.getNombre());
            tfApePaterno.setText(alumnoEdicion.getApellidoPaterno());
            tfApeMaterno.setText(alumnoEdicion.getApellidoMaterno());
            tfMatricula.setText(alumnoEdicion.getMatricula());
            tfEmail.setText(alumnoEdicion.getEmail());
            dpFechaNacimiento.setValue(LocalDate.parse(alumnoEdicion.getFechaNacimiento().toString()));
            tfMatricula.setEditable(false);
            
            
            int indice = obtenerPosicionFacultad(alumnoEdicion.getIdFacultad());
            cbFacultad.getSelectionModel().select(indice);
            int indiceCarrera = obtenerPosicionCarrera(alumnoEdicion.getIdCarrera());
            cbCarrera.getSelectionModel().select(indiceCarrera);
            
            byte[] foto = AlumnoDAO.obtenerFotoAlumno(alumnoEdicion.getIdAlumno());
            alumnoEdicion.setFoto(foto);
            ByteArrayInputStream input = new ByteArrayInputStream(foto);
            Image imagen = new Image(input);
            ivFoto.setImage(imagen);
            
        for (Facultad f : facultades) {
            if (f.getIdFacultad() == alumnoEdicion.getIdFacultad()) {
                cbFacultad.getSelectionModel().select(f);
                cargarCarreras(f.getIdFacultad()); 
                break;
            }
        }

        for (Carrera c : cbCarrera.getItems()) {
            if (c.getIdCarrera() == alumnoEdicion.getIdCarrera()) {
                cbCarrera.getSelectionModel().select(c);
                break;
            }
        }

        if (alumnoEdicion.getFoto() != null) {
            Image image = new Image(new java.io.ByteArrayInputStream(alumnoEdicion.getFoto()));
            ivFoto.setImage(image);
        }
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", e.getMessage());
        } 
    }
    
    private void cargarFacultades(){
        try {
            facultades = FXCollections.observableArrayList();
            List<Facultad> listaFacultades = CatalogoDAO.obtenerFacultades();
            facultades.addAll(listaFacultades);
            cbFacultad.setItems(facultades);
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No hay conexión con la base de datos", "Inténtalo más tarde");
        }        
    }
    
    private void seleccionarFacultad(){
        cbFacultad.valueProperty().addListener(new ChangeListener<Facultad>() {
            @Override
            public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                if(newValue != null){
                    cargarCarreras(newValue.getIdFacultad());
                }
            }
        });
    }
    
    private void cargarCarreras(int idFacultad) {
        try {
            carreras = FXCollections.observableArrayList();
            ArrayList<Carrera> listaCarreras = CatalogoDAO.obtenerCarrerasPorFacultad(idFacultad);
            carreras.addAll(listaCarreras);
            cbCarrera.setItems(carreras);
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error", "Ha ocurrido un error, inténtalo más tarde");
        }
    }

    private void cerrarVentana(){
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    @FXML
    private void btnClicSeleccionarFoto(ActionEvent event) {
       mostrarDialogoSeleccionFoto();
    }

    @FXML
    private void btnClicGuardar(ActionEvent event) {
        if(validarCampos()){
            try {
                Alumno alumno = obtenerAlumnoNuevo();

                if(esEdicion){
                    alumno.setIdAlumno(alumnoEdicion.getIdAlumno());
                    ResultadoOperacion resultadoActualizar = AlumnoDAO.editarAlumno(alumno);
                    if(!resultadoActualizar.isError()){
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno actualizado", "Alumno " + alumno.getNombre() + " actualizado con éxito");
                        cerrarVentana();
                        observador.operacionExitosa("actualizar", alumno.getNombre());
                    }else{
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al actualizar " + resultadoActualizar.getMensaje());
                    }
                } else {
                    guardarAlumno(alumno);
                }

            } catch (IOException | SQLException ex) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar el alumno: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    private void mostrarDialogoSeleccionFoto(){
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImg = new FileChooser.ExtensionFilter("Archivos JPG (.jpg, .png, .gif)", "*.png", "*.jpg", "*.gif");
        dialogoSeleccion.getExtensionFilters().add(filtroImg);
        archivoFoto = dialogoSeleccion.showOpenDialog(Utilidad.getObtenerScenarioComponente(btnSeleccionarFoto));
        
        if(archivoFoto != null){
            mostrarFotoPerfil(archivoFoto);
        }
    }
    
    private void mostrarFotoPerfil(File archivoFoto){
        try {
            BufferedImage bufferImg = ImageIO.read(archivoFoto);
            Image imagen = SwingFXUtils.toFXImage(bufferImg, null);
            ivFoto.setImage(imagen);
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error", "No se ha podido cargar la imagen, inténtalo más tarde");
        }
    }
    
    private boolean validarCampos(){
        String nombre = tfNombre.getText().trim();
        String apellidoPaterno = tfApePaterno.getText().trim();
        String apellidoMaterno = tfApeMaterno.getText().trim();
        String email = tfEmail.getText().trim();
        String matricula = tfMatricula.getText().trim();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        Facultad facultad = cbFacultad.getSelectionModel().getSelectedItem();
        Carrera carrera = cbCarrera.getSelectionModel().getSelectedItem();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty()
                || email.isEmpty() || matricula.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos vacíos",
                    "Por favor, completa todos los campos de texto.");
            return false;
        }

        if (fechaNacimiento == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Fecha no seleccionada",
                    "Por favor, selecciona una fecha de nacimiento.");
            return false;
        }

        if (facultad == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Facultad no seleccionada",
                    "Por favor, selecciona una facultad.");
            return false;
        }

        if (carrera == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Carrera no seleccionada",
                    "Por favor, selecciona una carrera.");
            return false;
        }

        if (archivoFoto == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Foto no seleccionada",
                    "Por favor, selecciona una foto para el alumno.");
            return false;
        }

        return true;
    }
    
    private Alumno obtenerAlumnoNuevo()throws IOException{
        Alumno alumno = new Alumno();
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApePaterno.getText());
        alumno.setApellidoMaterno(tfApeMaterno.getText());
        alumno.setMatricula(tfMatricula.getText());
        alumno.setEmail(tfEmail.getText());
        alumno.setFechaNacimiento(dpFechaNacimiento.getValue().toString());
        alumno.setIdCarrera(cbCarrera.getSelectionModel().getSelectedItem().getIdCarrera());
        alumno.setFoto(Files.readAllBytes(archivoFoto.toPath()));
        
        if(archivoFoto !=null){
            byte[] foto = Files.readAllBytes(archivoFoto.toPath());
            alumno.setFoto(foto);
        }else{
            alumno.setFoto(alumnoEdicion.getFoto());
        }
        
        return alumno;
    }
    
    private void guardarAlumno(Alumno alumno){
        try {
            ResultadoOperacion resultadoInsertar = AlumnoDAO.registrarAlumno(alumno);
            if(!resultadoInsertar.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno(a) registrado", "Alumno(a) " + alumno.getNombre() + " registrado con éxito");
                Utilidad.getObtenerScenarioComponente(tfNombre).close();
                observador.operacionExitosa("insertar", alumno.getNombre());
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al registrar " + resultadoInsertar.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No hay conexión con la base de datos" + ex.getMessage());
        }
    }
    
    private int obtenerPosicionFacultad(int idFacultad){
        for (int i = 0; i < facultades.size(); i++) {
            if(facultades.get(i).getIdFacultad() == idFacultad)
                return i;
        }
        return 0;
    }
    
    private int obtenerPosicionCarrera(int idCarrera){
        for (int i = 0; i < carreras.size(); i++) {
            if(carreras.get(i).getIdCarrera() == idCarrera)
                return i;
        }
        return 0;
    }
}
