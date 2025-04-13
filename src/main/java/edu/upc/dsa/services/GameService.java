package edu.upc.dsa.services;


import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.exceptions.UsuarioNoAutenticadoException;
import edu.upc.dsa.exceptions.NoSuficientesTarrosException;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Api(value = "/usuarios", description = "Endpoint to Usuario Service")
@Path("/usuarios")
public class GameService {

    private GameManager gm;

    public GameService() throws UsuarioYaRegistradoException {
        this.gm = GameManagerImpl.getInstance();
        this.gm.initTestUsers();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUsuario(UsuReg usuReg) {
        try {
            Usuario u = gm.addUsuario(usuReg.getId(), usuReg.getName(), usuReg.getPswd(), usuReg.getMail(), usuReg.getPregunta(), usuReg.getRespuesta());
            return Response.status(201).entity(u).build(); // Registrado con éxito
        } catch (UsuarioYaRegistradoException e) {

            return Response.status(409).entity(e.getMessage()).build(); // Conflicto
        } catch (Exception e) {

            return Response.status(500).entity("Error interno del servidor").build(); // Error general
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Usulogin loginData) {
        try {
            Usuario u = gm.login(loginData.getIdoname(), loginData.getPswd());
            return Response.status(200).entity(u).build(); // Login OK
        } catch (CredencialesIncorrectasException e) {

            return Response.status(401).entity(e.getMessage()).build(); // No autorizado
        } catch (Exception e) {

            return Response.status(500).entity("Error interno del servidor").build(); // Error general
        }
    }

    @PUT
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprarObjeto(Compra compra) {
        try {
            Usuario usuarioActualizado = gm.Comprar(compra);
            return Response.status(200).entity(usuarioActualizado).build();
        } catch (UsuarioNoAutenticadoException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoSuficientesTarrosException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/tienda/armas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArmas() {
        List<Object> t = gm.findArmas();
        return Response.status(200).entity(t).build();
    }

    @GET
    @Path("/tienda/skins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkin() {
        List<Object> t = gm.findSkins();
        return Response.status(200).entity(t).build();
    }

    @GET
    @Path("/login/recordarContraseña")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getlogin(String u) {
        try {
            String pregunta = gm.obtenerContra(u);
            return Response.status(200).entity(pregunta).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        }

    }
    @POST
    @Path("/login/recuperarCuenta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(OlvContra usu) {
        try {
            Usuario u = gm.relogin(usu.getId(), usu.getRespuesta());
            return Response.status(200).entity(u).build(); // Login OK
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build(); // No autorizado
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build(); // Error general
        }
    }
    @PUT
    @Path("/login/cambiarContraseña")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiarContra(Usulogin u) {
        try {
            gm.CambiarContra(u.getIdoname(), u.getPswd());
            return Response.status(200).entity("Contraseña cambiada con éxito").build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        }
    }


}



