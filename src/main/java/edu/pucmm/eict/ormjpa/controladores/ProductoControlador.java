package edu.pucmm.eict.ormjpa.controladores;

import edu.pucmm.eict.ormjpa.entidades.Imagen;
import edu.pucmm.eict.ormjpa.entidades.Producto;
import edu.pucmm.eict.ormjpa.servicios.ProductoServices;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoControlador {

    public static void listar(@NotNull Context ctx) {
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("productos", ProductoServices.getInstancia().findAll());
        ctx.render("/templates/producto-listar.html", modelo);
    }

    public static void mostrarFormularioCrear(@NotNull Context ctx) {
        ctx.render("/templates/producto-form.html", new HashMap<>());
    }

    public static void crear(@NotNull Context ctx) {
        List<UploadedFile> archivos = ctx.uploadedFiles("imagenes");

        if (archivos.isEmpty()) {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("error", "Debe agregar al menos una imagen");
            modelo.put("nombre", ctx.formParam("nombre"));
            modelo.put("descripcion", ctx.formParam("descripcion"));
            modelo.put("precio", ctx.formParam("precio"));
            ctx.render("/templates/producto-form.html", modelo);
            return;
        }

        Producto producto = new Producto(
                ctx.formParam("nombre"),
                ctx.formParam("descripcion"),
                Double.parseDouble(ctx.formParam("precio")));

        for (UploadedFile archivo : archivos) {
            producto.agregarImagen(leerImagen(archivo));
        }

        ProductoServices.getInstancia().crear(producto);
        ctx.redirect("/productos");
    }

    public static void ver(@NotNull Context ctx) {
        Producto producto = ProductoServices.getInstancia().find(ctx.pathParamAsClass("id", Long.class).get());
        if (producto == null) {
            ctx.redirect("/productos");
            return;
        }
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("producto", producto);
        ctx.render("/templates/producto-ver.html", modelo);
    }

    public static void mostrarFormularioEditar(@NotNull Context ctx) {
        Producto producto = ProductoServices.getInstancia().find(ctx.pathParamAsClass("id", Long.class).get());
        if (producto == null) {
            ctx.redirect("/productos");
            return;
        }
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("producto", producto);
        ctx.render("/templates/producto-form.html", modelo);
    }

    public static void editar(@NotNull Context ctx) {
        Producto producto = ProductoServices.getInstancia().find(ctx.pathParamAsClass("id", Long.class).get());
        if (producto == null) {
            ctx.redirect("/productos");
            return;
        }
        producto.setNombre(ctx.formParam("nombre"));
        producto.setDescripcion(ctx.formParam("descripcion"));
        producto.setPrecio(Double.parseDouble(ctx.formParam("precio")));

        for (UploadedFile archivo : ctx.uploadedFiles("imagenes")) {
            producto.agregarImagen(leerImagen(archivo));
        }

        ProductoServices.getInstancia().editar(producto);
        ctx.redirect("/productos");
    }

    public static void eliminar(@NotNull Context ctx) {
        Producto producto = ProductoServices.getInstancia().find(ctx.pathParamAsClass("id", Long.class).get());
        if (producto != null) {
            ProductoServices.getInstancia().eliminar(producto.getId());
        }
        ctx.redirect("/productos");
    }

    private static Imagen leerImagen(UploadedFile archivo) {
        try {
            byte[] bytes = archivo.content().readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return new Imagen(archivo.contentType(), base64);
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo la imagen", e);
        }
    }
}
