package edu.pucmm.eict.ormjpa;

import edu.pucmm.eict.ormjpa.controladores.ApiControlador;
import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.servicios.BootStrapServices;
import edu.pucmm.eict.ormjpa.servicios.EstudianteServices;
import edu.pucmm.eict.ormjpa.servicios.ProfesorServices;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

public class Main {

    public static void main(String[] args) {
        String mensaje = "Software ORM - JPA";
        System.out.println(mensaje);

        //Iniciando la base de datos.
        BootStrapServices.getInstancia().init();

        //creando los objetos por defecto.
        for(int i=0;i<50;i++){
            EstudianteServices.getInstancia().crear(new Estudiante(i, "nombre "+i));
            ProfesorServices.getInstancia().crear(new Profesor("Profesor "+i));
        }

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles("/publico"); //desde la carpeta de resources
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
        }).start(getHerokuAssignedPort());

        //Endpoint de inicio.
        app.get("/", ctx -> {
            ctx.result(mensaje);
        });

        //creando los endpoint de las rutas.
        new ApiControlador(app).aplicarRutas();
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
}
