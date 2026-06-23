package edu.pucmm.eict.ormjpa.controladores;

import edu.pucmm.eict.ormjpa.entidades.Comentario;
import edu.pucmm.eict.ormjpa.entidades.Producto;
import edu.pucmm.eict.ormjpa.entidades.Usuario;
import edu.pucmm.eict.ormjpa.servicios.ComentarioServices;
import edu.pucmm.eict.ormjpa.servicios.ProductoServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class ComentarioControlador {

    public static void agregar(@NotNull Context ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        Long productoId = ctx.pathParamAsClass("id", Long.class).get();

        if (usuario == null) {
            ctx.redirect("/login");
            return;
        }

        Producto producto = ProductoServices.getInstancia().find(productoId);
        if (producto != null) {
            String texto = ctx.formParam("texto");
            ComentarioServices.getInstancia().crear(new Comentario(texto, usuario, producto));
        }

        ctx.redirect("/productos/" + productoId);
    }
}
