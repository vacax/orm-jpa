package edu.pucmm.eict.ormjpa.entidades;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by vacax on 03/06/16.
 */
@Entity
@NamedQueries({@NamedQuery(name = "Clase.findAllProfesorId", query = "select p from GrupoClase p where p.profesor.id = :id")})
public class GrupoClase implements Serializable {

    @Id
    private Long id;
    private String clave;
    private String nombre;

    @ManyToOne()
    private Profesor profesor; //Relación mucho a uno con el profesor, un profesor puede tener muchas clases, pero una clase un profesor.

    @ManyToMany() //
    private Set<Estudiante> listaEstudiante; //Relación mucho a mucho entre estudiante y clase, de forma implicita.

    public GrupoClase() {
    }

    public GrupoClase(Long id, String clave, String nombre, Profesor profesor, Set<Estudiante> listaEstudiante) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.profesor = profesor;
        this.listaEstudiante = listaEstudiante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Set<Estudiante> getListaEstudiante() {
        return listaEstudiante;
    }

    public void setListaEstudiante(Set<Estudiante> listaEstudiante) {
        this.listaEstudiante = listaEstudiante;
    }
}
