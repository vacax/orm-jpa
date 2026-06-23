package edu.pucmm.eict.ormjpa.servicios;

import edu.pucmm.eict.ormjpa.entidades.Comentario;

public class ComentarioServices extends GestionDb<Comentario> {

    private static ComentarioServices instancia;

    private ComentarioServices() {
        super(Comentario.class);
    }

    public static ComentarioServices getInstancia() {
        if (instancia == null) {
            instancia = new ComentarioServices();
        }
        return instancia;
    }
}
