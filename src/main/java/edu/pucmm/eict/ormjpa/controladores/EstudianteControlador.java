package edu.pucmm.eict.ormjpa.controladores;

import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jetbrains.annotations.NotNull;

public class EstudianteControlador {

    public static void listarEstudiantes(@NotNull Context ctx) throws Exception {
        ctx.json(EstudianteServices.getInstancia().findAll());
    }

    public static void estudiantePorMatricula(@NotNull Context ctx) throws Exception {
        Estudiante es = EstudianteServices.getInstancia().find(ctx.pathParamAsClass("matricula", Integer.class).get());

        if(es!=null){
            ctx.json(es);
        }else{
            throw new NotFoundResponse("Estudiante no encontrado");
        }
    }

    public static void crearEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
        //creando.
        ctx.json(EstudianteServices.getInstancia().crear(tmp));
    }

    public static void actualizarEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
        //creando.
        ctx.json(EstudianteServices.getInstancia().editar(tmp));
    }

    public static void eliminarEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        ctx.json(EstudianteServices.getInstancia().eliminar(ctx.pathParamAsClass("matricula", Integer.class).get()));
    }
}
