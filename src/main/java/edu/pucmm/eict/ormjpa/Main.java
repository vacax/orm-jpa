package edu.pucmm.eict.ormjpa;

import edu.pucmm.eict.ormjpa.controladores.ComentarioControlador;
import edu.pucmm.eict.ormjpa.controladores.EstudianteControlador;
import edu.pucmm.eict.ormjpa.controladores.FotoControlador;
import edu.pucmm.eict.ormjpa.controladores.ProductoControlador;
import edu.pucmm.eict.ormjpa.controladores.ProfesorControlador;
import edu.pucmm.eict.ormjpa.controladores.SeguridadControlador;
import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.servicios.BootStrapServices;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import edu.pucmm.eict.ormjpa.servicios.ProfesorServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    //indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    public static void main(String[] args) {
        String mensaje = "Software ORM - JPA";
        System.out.println(mensaje);
        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            }); //desde la carpeta de resources

            //En la versión 6, cambio la forma de registrar los sistemas de plantilla.
            // ver en https://javalin.io/migration-guide-javalin-5-to-6
             /*
                    Este es el camino largo, por si tengo más de un sistema de plantilla, es decir,
                    si quiero utilizar Freemaker o Velocity en conjunto.
                    */
                   /*
                   var render = new JavalinThymeleaf();
                   String salida = render.render("/templates/mi_plantilla.html", modelo, ctx);
                    ctx.contentType(ContentType.HTML);
                    ctx.result(salida);*/
            config.fileRenderer(new JavalinThymeleaf());

            /**
             * Definicion de los endpoint.
             * En la versión 7, las rutas se definen dentro del bloque de configuración.
             * ver en https://javalin.io/migration-guide-javalin-6-to-7
             */
            config.router.apiBuilder(() -> {
                path("/api",() -> {

                    path("/estudiante", () -> {
                        get(EstudianteControlador::listarEstudiantes);
                        post(EstudianteControlador::crearEstudiante);
                        put(EstudianteControlador::actualizarEstudiante);
                        path("/{matricula}", () -> {
                            get(EstudianteControlador::estudiantePorMatricula);
                            delete(EstudianteControlador::eliminarEstudiante);
                        });
                    });

                    path("/profesor", () -> {
                        get(ProfesorControlador::listarProfesores);
                        post(ProfesorControlador::crearProfesor);
                        put(ProfesorControlador::actualizarProfesor);
                        path("/{id}", () -> {
                            get(ProfesorControlador::profesorPorId);
                            delete(ProfesorControlador::eliminarProfesor);
                        });
                    });
                });

                path("/fotos",() -> {
                    get(ctx -> {
                        ctx.redirect("/fotos/listar");
                    });
                    get("/listar", FotoControlador::listarFotos);
                    post("/procesarFoto", FotoControlador::procesarFotos);
                    get("/visualizar/{id}", FotoControlador::visualizarFotos);
                    get("/eliminar/{id}", FotoControlador::eliminarFotos);
                });

                path("/productos", () -> {
                    get(ProductoControlador::listar);
                    post(ProductoControlador::crear);
                    get("/crear", ProductoControlador::mostrarFormularioCrear);
                    get("/editar/{id}", ProductoControlador::mostrarFormularioEditar);
                    post("/editar/{id}", ProductoControlador::editar);
                    get("/eliminar/{id}", ProductoControlador::eliminar);
                    post("/{id}/comentario", ComentarioControlador::agregar);
                    get("/{id}", ProductoControlador::ver);
                });

                get("/login", SeguridadControlador::mostrarLogin);
                post("/login", SeguridadControlador::procesarLogin);
                get("/logout", SeguridadControlador::logout);
                get("/admin", SeguridadControlador::panelAdmin);

                //Endpoint de inicio.
                get("/", ctx -> ctx.result(mensaje));

                get("/prueba", ctx -> {
                    EstudianteServices.getInstancia().pruebaActualizacion();
                    ctx.result("Bien!...");
                });
            });

        });

        app.before(SeguridadControlador::autoLogin);
        app.before("/admin", SeguridadControlador::protegerAdmin);
        app.before("/admin/*", SeguridadControlador::protegerAdmin);

        //Manejo global de excepciones. En Javalin 6 se registra sobre la instancia de la app.
        app.exception(Exception.class, (exception, ctx) -> {
            ctx.status(500);
            ctx.html("<h1>Error no recuperado:"+exception.getMessage()+"</h1>");
            exception.printStackTrace();
        });

        app.start(getHerokuAssignedPort());
    }

    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

    /**
     * Nos
     * @return
     */
    public static String getModoConexion(){
        return modoConexion;
    }
}
