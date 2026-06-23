package edu.pucmm.eict.ormjpa.servicios;

import edu.pucmm.eict.ormjpa.entidades.Comentario;
import edu.pucmm.eict.ormjpa.entidades.Estudiante;
import edu.pucmm.eict.ormjpa.entidades.Imagen;
import edu.pucmm.eict.ormjpa.entidades.Producto;
import edu.pucmm.eict.ormjpa.entidades.Profesor;
import edu.pucmm.eict.ormjpa.entidades.Usuario;
import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by vacax on 07/06/17.
 */
public class BootStrapServices {

    private static BootStrapServices instancia;

    private BootStrapServices(){

    }

    public static BootStrapServices getInstancia(){
        if(instancia == null){
            instancia=new BootStrapServices();
        }
        return instancia;
    }

    public void startDb() {
        try {
            //Modo servidor H2.
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon",
                    "-ifNotExists").start();
            //Abriendo la consola web en un puerto fijo conocido.
            String status = Server.createWebServer("-webPort", "8082").start().getStatus();
            //
            System.out.println("Status Web: "+status);
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }

    public void init(){
         startDb();
         cargarDatosBase();
    }

    /**
     * Crea la informacion base de manera automatica.
     */
    private void cargarDatosBase(){
        if(EstudianteServices.getInstancia().findAll().isEmpty()){
            EstudianteServices.getInstancia().crear(new Estudiante(1, "Estudiante Demo"));
            ProfesorServices.getInstancia().crear(new Profesor("Profesor Demo"));
            System.out.println("Datos base creados.");
        }

        if(UsuarioServices.getInstancia().findAll().isEmpty()){
            UsuarioServices.getInstancia().crear(new Usuario("admin", "admin", "ADMIN"));
            UsuarioServices.getInstancia().crear(new Usuario("usuario", "usuario", "USUARIO"));
        }

        if(ProductoServices.getInstancia().findAll().isEmpty()){
            Producto p = new Producto("Producto Demo", "Descripcion del producto demo", 100.0);
            p.agregarImagen(new Imagen("image/png", "iVBORw0KGgo="));
            Producto guardado = ProductoServices.getInstancia().crear(p);
            Usuario admin = UsuarioServices.getInstancia().buscarPorUsername("admin");
            ComentarioServices.getInstancia().crear(new Comentario("Primer comentario", admin, guardado));
            System.out.println("Producto demo creado con imagen y comentario.");
        }
    }
}
