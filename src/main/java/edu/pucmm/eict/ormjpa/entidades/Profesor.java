package edu.pucmm.eict.ormjpa.entidades;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by vacax on 03/06/16.
 */
@Entity
@NamedQueries({@NamedQuery(name = "Profesor.findAllByNombre", query = "select p from Profesor p where p.nombre like :nombre")})
public class Profesor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //crear el ID de forma automatica
    private Integer id;
    private String nombre;

    //Indicando las referencias bidireccional de la entidad Clase.
    @OneToMany(mappedBy = "profesor", fetch = FetchType.EAGER) // La clase "Clase" es la dueña de la relación.
    private Set<GrupoClase> listaClases;


    public Profesor(){

    }

    public Profesor(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
