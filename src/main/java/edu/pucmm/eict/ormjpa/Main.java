package edu.pucmm.eict.ormjpa;

import com.fasterxml.jackson.databind.node.TextNode;
import edu.pucmm.eict.ormjpa.controladores.EstudianteControlador;
import edu.pucmm.eict.ormjpa.controladores.FotoControlador;
import edu.pucmm.eict.ormjpa.controladores.ProfesorControlador;
import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.servicios.BootStrapServices;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import edu.pucmm.eict.ormjpa.servicios.ProfesorServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.JsonSchemaLoader;
import io.javalin.openapi.JsonSchemaResource;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.security.RouteRole;
import io.javalin.validation.Rule;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    //indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    enum Rules implements RouteRole {
        ANONYMOUS,
        USER,
    }

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

        //creando los objetos por defecto.
        for(int i=0;i<50;i++){
            EstudianteServices.getInstancia().crear(new Estudiante(i, "nombre "+i));
            ProfesorServices.getInstancia().crear(new Profesor("Profesor "+i));
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
            });

            //Ver la documentación en https://github.com/javalin/javalin-openapi
            config.registerPlugin(new OpenApiPlugin(openApiConf -> {
                openApiConf
                        .withRoles(Rules.ANONYMOUS)
                        .withDefinitionConfiguration((version, openApiDefinition) -> {
                            openApiDefinition
                                    .withInfo(openApiInfo ->
                                            openApiInfo
                                                    .description("Api de demostración para OpenAPI")
                                                    .termsOfService("https://icc352.pucmm.edu.do/tos")
                                                    .contact("API Soporte", "https://icc352.pucmm.edu.do/soporte", "support@example.com")
                                                    .license("Apache 2.0", "https://www.apache.org/licenses/", "Apache-2.0")
                                    )
                                    .withServer(openApiServer ->
                                            openApiServer
                                                    .description("Información sobre servidor")
                                                    .url("http://localhost:{port}/")
                                                    //.url("http://localhost:{port}{basePath}/" + version + "/")
                                                    .variable("port", "Puertos servidor", "7000", "8080", "7000")
                                                    //.variable("basePath", "Base path of the server", "", "", "v1")
                                    )
                                    .withSecurity(openApiSecurity ->
                                            openApiSecurity
                                                    .withBasicAuth() //Indicando la autorización basica.
                                                    .withBearerAuth() //Indicando la autorización vía Bearer.
                                                    .withApiKeyAuth("ApiKeyAuth", "X-Api-Key")
                                                    .withCookieAuth("CookieAuth", "JSESSIONID")
                                                    .withOpenID("OpenID", "https://example.com/.well-known/openid-configuration")
                                                    .withOAuth2("OAuth2", "API usando OAuth 2", oauth2 ->
                                                            oauth2
                                                                    .withClientCredentials("https://icc352.pucmm.edu.do/credentials/authorize")
                                                                    .withImplicitFlow("https://icc352.pucmm.edu.do/oauth2/authorize", flow ->
                                                                            flow
                                                                                    .withScope("read_pets", "read your pets")
                                                                                    .withScope("write_pets", "modify pets in your account")
                                                                    )
                                                    )
                                                    .withGlobalSecurity("OAuth2", globalSecurity ->
                                                            globalSecurity
                                                                    .withScope("write_pets")
                                                                    .withScope("read_pets")
                                                    )
                                    )
                                    .withDefinitionProcessor(content -> { // you can add whatever you want to this document using your favourite json api
                                        content.set("test", new TextNode("Value"));
                                        return content.toPrettyString();
                                    });
                        });
            }));

            config.registerPlugin(new SwaggerPlugin(swaggerConfiguration -> {
                //swaggerConfiguration.setDocumentationPath(deprecatedDocsPath);
            }));

            config.registerPlugin(new ReDocPlugin(reDocConfiguration -> {
                //reDocConfiguration.setDocumentationPath(deprecatedDocsPath);
            }));

            for (JsonSchemaResource generatedJsonSchema : new JsonSchemaLoader().loadGeneratedSchemes()) {
                System.out.println(generatedJsonSchema.getName());
                System.out.println(generatedJsonSchema.getContentAsString());
            }

        }).start(getHerokuAssignedPort());

        //Endpoint de inicio.
        app.get("/", ctx -> {
            ctx.result(mensaje);
        });

        app.get("/prueba", ctx -> {
            EstudianteServices.getInstancia().pruebaActualizacion();
            ctx.result("Bien!...");
        });

        app.exception(Exception.class, (exception, ctx) -> {
            ctx.status(500);
            ctx.html("<h1>Error no recuperado:"+exception.getMessage()+"</h1>");
            exception.printStackTrace();
        });


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
