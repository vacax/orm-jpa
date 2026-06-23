package edu.pucmm.eict.ormjpa.controladores;

import edu.pucmm.eict.ormjpa.entidades.Usuario;
import edu.pucmm.eict.ormjpa.servicios.UsuarioServices;
import edu.pucmm.eict.ormjpa.util.Encriptacion;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SeguridadControlador {

    public static final String COOKIE_RECORDAR = "recordar";
    private static final int UNA_SEMANA = 60 * 60 * 24 * 7;

    public static void mostrarLogin(@NotNull Context ctx) {
        ctx.render("/templates/login.html", new HashMap<>());
    }

    public static void procesarLogin(@NotNull Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        Usuario usuario = UsuarioServices.getInstancia().buscarPorUsername(username);

        if (usuario == null || !usuario.getPassword().equals(password)) {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("error", "Usuario o contraseña incorrectos");
            ctx.render("/templates/login.html", modelo);
            return;
        }

        ctx.sessionAttribute("usuario", usuario);

        if ("on".equals(ctx.formParam("recordar"))) {
            ctx.cookie(COOKIE_RECORDAR, Encriptacion.encriptar(usuario.getUsername()), UNA_SEMANA);
        }

        ctx.redirect("/admin");
    }

    public static void logout(@NotNull Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.removeCookie(COOKIE_RECORDAR);
        ctx.redirect("/login");
    }

    public static void autoLogin(@NotNull Context ctx) {
        if (ctx.sessionAttribute("usuario") != null) {
            return;
        }
        String cookie = ctx.cookie(COOKIE_RECORDAR);
        if (cookie == null) {
            return;
        }
        try {
            String username = Encriptacion.desencriptar(cookie);
            Usuario usuario = UsuarioServices.getInstancia().buscarPorUsername(username);
            if (usuario != null) {
                ctx.sessionAttribute("usuario", usuario);
            }
        } catch (Exception e) {
            ctx.removeCookie(COOKIE_RECORDAR);
        }
    }

    public static void protegerAdmin(@NotNull Context ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            ctx.redirect("/login");
            ctx.skipRemainingHandlers();
        }
    }

    public static void panelAdmin(@NotNull Context ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        ctx.html("<h1>Panel Admin</h1><p>Bienvenido " + usuario.getUsername()
                + " (" + usuario.getRol() + ")</p><a href='/logout'>Salir</a>");
    }
}
