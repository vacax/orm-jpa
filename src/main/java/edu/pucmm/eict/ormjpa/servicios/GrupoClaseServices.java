package edu.pucmm.eict.ormjpa.servicios;


import edu.pucmm.eict.ormjpa.entidades.GrupoClase;
import edu.pucmm.eict.ormjpa.entidades.Profesor;

import java.util.List;

/**
 * Created by vacax on 04/06/16.
 */
public class GrupoClaseServices extends GestionDb<Profesor> {

    private static GrupoClaseServices instancia;

    private GrupoClaseServices(){
        super(Profesor.class);
    }

    public static GrupoClaseServices getInstancia(){
        if(instancia==null){
            instancia = new GrupoClaseServices();
        }
        return instancia;
    }



    /**
     *
     * @param id
     * @return
     */
    public List<GrupoClase> findAllByProfesorClase(long id){
       /* EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Clase.findAllProfesorId");
        query.setParameter("id", id);
        List<Clase> lista = query.getResultList();
        em.create*/
        return null;
    }
}
