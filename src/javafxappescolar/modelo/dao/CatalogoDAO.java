/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.Carrera;
import javafxappescolar.modelo.pojo.Facultad;

/**
 *
 * @author themr
 */
public class CatalogoDAO {
    
    public static ArrayList<Facultad> obtenerFacultades() throws SQLException{
        
        ArrayList<Facultad> facultades = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        String query = "SELECT idFacultad, nombre FROM facultad";

        try (PreparedStatement ps = conexionBD.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                Facultad facultad = new Facultad();
                facultad.setIdFacultad(rs.getInt("idFacultad"));
                facultad.setNombreFacultad(rs.getString("nombre"));
                facultades.add(facultad);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener facultades", ex);
        }
        return facultades;
    }
    
    public static ArrayList<Carrera> obtenerCarrerasPorFacultad(int idFacultad) throws SQLException {
        ArrayList<Carrera> carreras = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        String query = "SELECT idCarrera, codigo, nombre FROM carrera WHERE idFacultad = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idFacultad);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Carrera carrera = new Carrera();
                carrera.setIdCarrera(rs.getInt("idCarrera"));
                carrera.setCodigo(rs.getString("codigo"));
                carrera.setNombre(rs.getString("nombre"));
                carrera.setIdFacultad(idFacultad);
                carreras.add(carrera);
            }

            rs.close();
        } catch (SQLException e) {
            throw new SQLException("Error al obtener carreras por facultad", e);
        }

        return carreras;
    }
    
    private static Carrera convertirRegistroCarrera(ResultSet resultado) throws SQLException {
            Carrera carrera = new Carrera();
            carrera.setIdCarrera(resultado.getInt("idCarrera"));
            carrera.setNombre(resultado.getString("nombreCarrera"));
            carrera.setCodigo(resultado.getString("codigo"));
            carrera.setIdFacultad(resultado.getInt("idFacultad"));
            carrera.setNombreFacultad(resultado.getString("nombreFacultad"));
            
            return carrera;
        }
}
