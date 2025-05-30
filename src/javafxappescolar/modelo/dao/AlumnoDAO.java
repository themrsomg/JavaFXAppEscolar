/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author themr
 */

public class AlumnoDAO {
    
    public static ArrayList<Alumno> obtenerAlumnos() throws SQLException{
        
        ArrayList<Alumno> alumnos = new ArrayList();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT idAlumno, a.nombre, apellidoPaterno, apellidoMaterno, fechaNacimiento, matricula, email, a.idCarrera, c.nombre AS nombreCarrera, c.idFacultad, f.nombre AS Facultad FROM alumno a INNER JOIN carrera c ON a.idCarrera = c.idCarrera INNER JOIN facultad f ON f.idFacultad = c.idFacultad;";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                alumnos.add(convertirRegistroAlumno(resultado));
                
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión a la base de datos");
        }
        return alumnos;
    }
    
    public static ResultadoOperacion registrarAlumno(Alumno alumno)throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String sentencia = "INSERT INTO alumno (nombre, apellidoPaterno, apellidoMaterno, matricula, email, idCarrera, fechaNacimiento, foto) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conexionBD.prepareStatement(sentencia);
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellidoPaterno());
            ps.setString(3, alumno.getApellidoMaterno());
            ps.setString(4, alumno.getMatricula());
            ps.setString(5, alumno.getEmail());
            ps.setInt(6, alumno.getIdCarrera());
            ps.setString(7, alumno.getFechaNacimiento());
            ps.setBytes(8, alumno.getFoto());
            int filasAfectadas = ps.executeUpdate();
            if(filasAfectadas == 1){
                resultado.setError(false);
                resultado.setMensaje("Alumno(a) registrado correctamente");
            }else{
                resultado.setError(true);
                resultado.setMensaje("Ha ocurrido un error, inténtalo más tarde");
            }
            ps.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión a la base de datos");
        }
        return resultado;
    }    
    
    public static byte[] obtenerFotoAlumno(int idAlumno)throws SQLException{
        byte[] foto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT foto FROM alumno WHERE idAlumno = ?";
            PreparedStatement ps = conexionBD.prepareStatement(consulta);
            ps.setInt(1, idAlumno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] bytes = rs.getBytes("foto");
                if (bytes != null) {
                    foto = new byte[bytes.length];
                    for (int i = 0; i < bytes.length; i++) {
                        foto[i] = bytes[i];
                    }
                }
            }
            rs.close();
            ps.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la base de datos");
        }
        return foto;
    }
    
    public static boolean verificarExistenciaMatricula(String matricula)throws SQLException{
        boolean existe = false;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT COUNT(*) AS total FROM alumno WHERE matricula = ?";
            PreparedStatement ps = conexionBD.prepareStatement(consulta);
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = rs.getInt("total") > 0;
            }
            rs.close();
            ps.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la base de datos");
        }
        return existe;
    }
    
    public static ResultadoOperacion editarAlumno(Alumno alumno)throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        String sql = "UPDATE alumno SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, email=?, matricula=?, idcarrera=?, fechaNacimiento=?, foto=? WHERE idAlumno=?";
        try (Connection conn = ConexionBD.abrirConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellidoPaterno());
            ps.setString(3, alumno.getApellidoMaterno());
            ps.setString(4, alumno.getEmail());
            ps.setString(5, alumno.getMatricula());
            ps.setInt(6, alumno.getIdCarrera());
            ps.setString(7, alumno.getFechaNacimiento());
            ps.setBytes(8, alumno.getFoto());
            ps.setInt(9, alumno.getIdAlumno());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                resultado.setError(false);
                resultado.setMensaje("Alumno modificado correctamente.");
            } else {
                resultado.setError(true);
                resultado.setMensaje("No se encontró el alumno para modificar.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultado.setError(true);
            resultado.setMensaje("Error al modificar alumno: " + ex.getMessage());
        }
        return resultado;
    }
    
    public static ResultadoOperacion eliminarAlumno(int idAlumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();

        String sqlEliminarCalificaciones = "DELETE FROM calificacion WHERE idAlumno = ?";
        String sqlEliminarAlumno = "DELETE FROM alumno WHERE idAlumno = ?";

        try (Connection conn = ConexionBD.abrirConexion()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement psCalif = conn.prepareStatement(sqlEliminarCalificaciones);
                PreparedStatement psAlumno = conn.prepareStatement(sqlEliminarAlumno)) {
                psCalif.setInt(1, idAlumno);
                psCalif.executeUpdate();
                psAlumno.setInt(1, idAlumno);
                int filasEliminadas = psAlumno.executeUpdate();

                if (filasEliminadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Alumno eliminado correctamente.");
                } else {
                    resultado.setError(true);
                    resultado.setMensaje("No se encontró el alumno a eliminar.");
                }

                conn.commit(); 
            } catch (SQLException ex) {
                conn.rollback(); 
                resultado.setError(true);
                resultado.setMensaje("Error al eliminar alumno: " + ex.getMessage());
            } finally {
                conn.setAutoCommit(true); 
            }
        }

        return resultado;
    }
    
    private static Alumno convertirRegistroAlumno(ResultSet resultado) throws SQLException {
            Alumno alumno = new Alumno();
            alumno.setIdAlumno(resultado.getInt("idAlumno"));
            alumno.setNombre(resultado.getString("nombre"));
            alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            alumno.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            alumno.setMatricula(resultado.getString("matricula"));
            alumno.setEmail(resultado.getString("email"));
            alumno.setIdCarrera(resultado.getInt("idCarrera"));
            alumno.setCarrera(resultado.getString("nombreCarrera"));
            alumno.setIdFacultad(resultado.getInt("idFacultad"));
            alumno.setFacultad(resultado.getString("facultad"));
            alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
            
            return alumno;
        }
}
