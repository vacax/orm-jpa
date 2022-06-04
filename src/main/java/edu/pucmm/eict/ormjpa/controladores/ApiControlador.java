package edu.pucmm.eict.ormjpa.controladores;

import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import edu.pucmm.eict.ormjpa.servicios.ProfesorServices;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ApiControlador {

    private  Javalin app;
    private EstudianteServices estudianteServices = EstudianteServices.getInstancia();
    private ProfesorServices profesorServices = ProfesorServices.getInstancia();

    public ApiControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
        app.routes(() -> {
          path("/api", () -> {

              //para todas las llamadas convertir al tipo JSON
              after(ctx -> {
                  ctx.header("Content-Type", "application/json");
              });

              /**
               * Ejemplo de una API REST, implementando el CRUD
               * ir a
               */
              path("/estudiante", () -> {


                  get("/", ctx -> {
                        ctx.json(estudianteServices.findAll()); //TODO: implementar paginación.
                  });

                  get("/{matricula}", ctx -> {
                      Estudiante es = estudianteServices.find(ctx.pathParamAsClass("matricula", Integer.class).get());

                      if(es!=null){
                          ctx.json(es);
                      }else{
                          throw new NotFoundResponse("Estudiante no encontrado");
                      }
                  });

                  post("/", ctx -> {
                        //parseando la informacion del POJO debe venir en formato json.
                        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
                        //creando.
                        ctx.json(estudianteServices.crear(tmp));
                  });

                  put("/", ctx -> {
                      //parseando la informacion del POJO.
                      Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
                      //creando.
                      ctx.json(estudianteServices.editar(tmp));
                  });

                  delete("/{matricula}", ctx -> {
                      //creando.
                      ctx.json(estudianteServices.eliminar(ctx.pathParamAsClass("matricula", Integer.class).get()));
                  });
              });

              path("/profesor", () -> {


                  get("/", ctx -> {
                      ctx.json(profesorServices.findAll());
                  });

                  get("/{id}", ctx -> {
                      Profesor p = profesorServices.find(ctx.pathParamAsClass("id", Integer.class).get());
                      if(p!=null){
                          ctx.json(p);
                      }else{
                          throw new NotFoundResponse("profesor no encontrado");
                      }
                  });

                  post("/", ctx -> {
                      //parseando la informacion del POJO debe venir en formato json.
                      Profesor tmp = ctx.bodyAsClass(Profesor.class);
                      //creando.
                      ctx.json(profesorServices.crear(tmp));
                  });

                  put("/", ctx -> {
                      //parseando la informacion del POJO.
                      Profesor tmp = ctx.bodyAsClass(Profesor.class);
                      //creando.
                      ctx.json(profesorServices.editar(tmp));
                  });

                  delete("/{id}", ctx -> {
                      //creando.
                      ctx.json(profesorServices.eliminar(ctx.pathParamAsClass("id", Integer.class).get()));
                  });
              });
          });
        });



        app.exception(Exception.class, (exception, ctx) -> {
            ctx.status(500);
            ctx.html("<h1>Error no recuperado:"+exception.getMessage()+"</h1>");
            exception.printStackTrace();
        });
    }
}
