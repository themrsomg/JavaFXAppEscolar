/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.pojo;

/**
 *
 * @author themr
 */
public class Carrera {
    private int idCarrera;
    private String nombre;
    private String codigo;
    private int idFacultad;
    private String nombreFacultad;

    public Carrera(){}
    
    public Carrera(int idCarrera, String nombre, String codigo, int idFacultad, String nombreFacultad) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.codigo = codigo;
        this.idFacultad = idFacultad;
        this.nombreFacultad = nombreFacultad;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(int idFacultad) {
        this.idFacultad = idFacultad;
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
    }
    
    @Override
    public String toString() {
        return "("+codigo+")" + nombre;
    }
    
    
}
