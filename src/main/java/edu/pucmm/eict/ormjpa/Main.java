package edu.pucmm.eict.ormjpa;

import edu.pucmm.eict.ormjpa.controladores.ApiControlador;
import edu.pucmm.eict.ormjpa.controladores.FotoControlador;
import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.servicios.BootStrapServices;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import edu.pucmm.eict.ormjpa.servicios.ProfesorServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.RouteOverviewPlugin;

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

            config.plugins.register(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.plugins.enableCors(corsContainer -> {
                corsContainer.add(corsPluginConfig -> {
                    corsPluginConfig.anyHost();
                });
            });
        }).start(getHerokuAssignedPort());

        //Endpoint de inicio.
        app.get("/", ctx -> {
            ctx.result(mensaje);
        });

        app.get("/prueba", ctx -> {
            EstudianteServices.getInstancia().pruebaActualizacion();
            ctx.result("Bien!...");
        });

        //creando los endpoint de las rutas.
        new ApiControlador(app).aplicarRutas();
        new FotoControlador(app).aplicarRutas();
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
