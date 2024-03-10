package edu.pucmm.eict.ormjpa.controladores;

import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

public class EstudianteControlador {

    @OpenApi(
            path = "/api/estudiante",
            methods = HttpMethod.GET,
            responses = {@OpenApiResponse(status = "200", description = "Listado de Estudiantes", content = {@OpenApiContent(from = Estudiante.class)})}
    )
    public static void listarEstudiantes(@NotNull Context ctx) throws Exception {
        ctx.json(EstudianteServices.getInstancia().findAll());
    }

    @OpenApi(
            path = "/api/estudiante/{matricula}",
            methods = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "matricula", required = true, type = Integer.class)},
            responses = {@OpenApiResponse(status = "200", description = "Estudiante Por Matricula o ID", content = {@OpenApiContent(from = Estudiante.class)})}
    )
    public static void estudiantePorMatricula(@NotNull Context ctx) throws Exception {
        Estudiante es = EstudianteServices.getInstancia().find(ctx.pathParamAsClass("matricula", Integer.class).get());

        if(es!=null){
            ctx.json(es);
        }else{
            throw new NotFoundResponse("Estudiante no encontrado");
        }
    }

    @OpenApi(
            path = "/api/estudiante",
            methods = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = Estudiante.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Crear del estudiante", content = {@OpenApiContent(from = Estudiante.class)})}
    )
    public static void crearEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
        //creando.
        ctx.json(EstudianteServices.getInstancia().crear(tmp));
    }

    @OpenApi(
            path = "/api/estudiante",
            methods = HttpMethod.PUT,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = Estudiante.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Actualización del estudiante", content = {@OpenApiContent(from = Estudiante.class)})}
    )
    public static void actualizarEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
        //creando.
        ctx.json(EstudianteServices.getInstancia().editar(tmp));
    }

    @OpenApi(
            path = "/api/estudiante/{matricula}",
            methods = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "matricula", required = true, type = Integer.class)},
            responses = {@OpenApiResponse(status = "200", description = "Eliminar Por Matricula o ID", content = {@OpenApiContent(from = Boolean.class)})}
    )
    public static void eliminarEstudiante(@NotNull Context ctx) throws Exception {
        //parseando la informacion del POJO debe venir en formato json.
        ctx.json(EstudianteServices.getInstancia().eliminar(ctx.pathParamAsClass("matricula", Integer.class).get()));
    }
}
