package edu.pucmm.eict.ormjpa.servicios;

import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Profesor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;


/**
 *
 * Created by vacax on 03/06/16.
 */
public class EstudianteServices extends GestionDb<Estudiante> {

    private static EstudianteServices instancia;

    private EstudianteServices() {
        super(Estudiante.class);
    }

    public static EstudianteServices getInstancia(){
        if(instancia==null){
            instancia = new EstudianteServices();
        }
        return instancia;
    }

    /**
     *
     * @param nombre
     * @return
     */
    public List<Estudiante> findAllByNombre(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Estudiante e where e.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Estudiante> lista = query.getResultList();
        return lista;
    }

    public List<Estudiante> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from estudiante ", Estudiante.class);
        //query.setParameter("nombre", apellido+"%");
        List<Estudiante> lista = query.getResultList();
        return lista;
    }

    public void pruebaActualizacion(){
        EntityManager em = getEntityManager();
        Estudiante est = new Estudiante(2222, "Nombre");
        em.getTransaction().begin();
        em.persist(est); //creando el registro.
        em.flush();
        em.getTransaction().commit(); //persistiendo el registro.
        em.getTransaction().begin();
        est.setNombre("Otro Nombre");
        em.flush();
        em.getTransaction().commit();
        em.getTransaction().begin();
        est.setNombre("Nuevamente otro nombre...");
        em.flush();
        em.getTransaction().commit();
        em.close(); //todos los objetos abiertos y manejados fueron cerrados.
        //
        est.setNombre("Cambiando el objeto..."); //en memoria, no en la base datos.
        em = getEntityManager();
        em.getTransaction().begin();
        em.merge(est);
        em.getTransaction().commit();
        Profesor p = em.find(Profesor.class, 1);
        System.out.println("El nombre del profesor: "+p.getNombre());
        em.close();
    }


}
