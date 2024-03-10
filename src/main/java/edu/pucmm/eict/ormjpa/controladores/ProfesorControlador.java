package edu.pucmm.eict.ormjpa.controladores;


import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.servicios.ProfesorServices;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jetbrains.annotations.NotNull;

public class ProfesorControlador {

    public static void listarProfesores(@NotNull Context ctx) throws Exception {
        ctx.json(ProfesorServices.getInstancia().findAll());
    }

    public static void profesorPorId(@NotNull Context ctx) throws Exception {
        Profesor es = ProfesorServices.getInstancia().find(ctx.pathParamAsClass("id", Integer.class).get());

        if(es!=null){
            ctx.json(es);
        }else{
            throw new NotFoundResponse("Profesor no encontrado");
        }
    }

    public static void crearProfesor(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Profesor tmp = ctx.bodyAsClass(Profesor.class);
        //creando.
        ctx.json(ProfesorServices.getInstancia().crear(tmp));
    }

    public static void actualizarProfesor(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Profesor tmp = ctx.bodyAsClass(Profesor.class);
        //creando.
        ctx.json(ProfesorServices.getInstancia().editar(tmp));
    }

    public static void eliminarProfesor(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        ctx.json(ProfesorServices.getInstancia().eliminar(ctx.pathParamAsClass("id", Integer.class).get()));
    }
}
