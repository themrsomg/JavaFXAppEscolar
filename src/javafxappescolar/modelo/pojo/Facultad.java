/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.pojo;

/**
 *
 * @author themr
 */
public class Facultad {
    private int idFacultad;
    private String nombreFacultad;

    public Facultad(){}
    
    public Facultad(int idFacultad, String nombre) {
        this.idFacultad = idFacultad;
        this.nombreFacultad = nombreFacultad;
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
        return "Facultad de " + nombreFacultad;
    }
    
    
}
