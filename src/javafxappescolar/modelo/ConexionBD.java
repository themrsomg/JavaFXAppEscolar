/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.animation.PauseTransition;

/**
 *
 * @author themr
 */
public class ConexionBD {
    
    /*1. IP / Hostname = localhost 
    * 2. Puerto = 3306
    * 3. Nombre de la base de datos
    * 4. Credenciales de acceso (user y password)
    * 5. 
    */
    
    private static final String IP = "localhost"; 
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "escolar";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "ElFerxxoEnRotoplas117";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection abrirConexion() {
        Connection conexionBD = null;
        String urlConexion = String.format(
            "jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
            IP, PUERTO, NOMBRE_BD
        );

        try {
            Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(urlConexion, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Clase del Driver no encontrada.");
            e.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Error en la conexión: " + ex.getMessage());
            ex.printStackTrace();
        }

        return conexionBD;
    }
    
}
