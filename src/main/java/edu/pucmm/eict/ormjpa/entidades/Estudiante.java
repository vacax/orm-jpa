package edu.pucmm.eict.ormjpa.entidades;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by vacax on 02/06/16.
 */
@Entity
public class Estudiante implements Serializable {

    @Id
    private int matricula;
    private String nombre;
    @Column(name = "mi_fecha_nacimiento") //indica que el nombre en la tabla
    private Date fechaNacimiento;
    @Transient
    private int edad;

    @ManyToMany(mappedBy = "listaEstudiante", fetch = FetchType.EAGER) //indicando que la carga será en linea.
    private Set<GrupoClase> listaClases; //La duena de la relación es la clase Clase


    public Estudiante(){  //Debo tener el constructor vacio...

    }

    public Estudiante(int matricula, String nombre) {
        this.setMatricula(matricula);
        this.setNombre(nombre);
    }



    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PreUpdate
    @PrePersist
    private void cancelarMatricula() {
        //setNombre(getNombre().toUpperCase()); //cambiando a mayuscula
        if(getMatricula() == 20011137){
              throw new RuntimeException("No puede ser esa matricula..");
        }
    }

    public Set<GrupoClase> getListaClases() {
        return listaClases;
    }

    public void setListaClases(Set<GrupoClase> listaClases) {
        this.listaClases = listaClases;
    }

    public int getEdad() {
        //La edad viene del campo evaluado del fechaNacimiento
        edad = 12; //calcular la fecha ......
        return edad;
    }



    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
