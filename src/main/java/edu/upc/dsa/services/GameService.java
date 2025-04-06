package edu.upc.dsa.services;


import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.models.UsuReg;
import edu.upc.dsa.models.Usuario;
import edu.upc.dsa.models.Usulogin;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
            Usuario u = gm.addUsuario(usuReg.getId(), usuReg.getName(), usuReg.getPswd(), usuReg.getMail());
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
}



