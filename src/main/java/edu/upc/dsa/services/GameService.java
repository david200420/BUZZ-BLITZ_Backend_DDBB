package edu.upc.dsa.services;


import edu.upc.dsa.db.orm.dao.GameManagerDAO;
import edu.upc.dsa.db.orm.dao.GameManagerDAOImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/usuarios", description = "Endpoint to Usuario Service")
@Path("/usuarios")
public class GameService {

    private GameManagerDAO dao;

    public GameService(){
        this.dao = new GameManagerDAOImpl();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "registra un usuario", notes = "asdasd")
    public Response registerUsuario(UsuReg usuReg) {
        try {
            dao.addUsuario(usuReg.getId(), usuReg.getName(), usuReg.getApellidos(),usuReg.getPswd(), usuReg.getMail(), usuReg.getPregunta(), usuReg.getRespuesta());
            return Response.status(201).build(); // Registrado con éxito
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
    @ApiOperation(value = "login d'un usuari")
    public Response loginUsuario(Usulogin loginData) {
        try {
            UsuarioEnviar u = dao.login(loginData.getIdoname(), loginData.getPswd());
            return Response.status(200).entity(u).build(); // Login OK
        } catch (CredencialesIncorrectasException e) {

            return Response.status(401).entity(e.getMessage()).build(); // No autorizado
        } catch (Exception e) {

            return Response.status(500).entity("Error interno del servidor").build(); // Error general
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "borra un usuari")
    public Response deleteUsuario(@PathParam("id") String id) {
        try {
            dao.deleteUsuario(id);
            return Response.status(200).build();
        } catch (UsuarioNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno").build();
        }
    }

    @PUT
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Compra un objcte de la tenda")
    public Response comprarObjeto(Usuario_objeto usuarioobjeto) {
        try {
            DevolverCompra usuarioActualizado = dao.Comprar(usuarioobjeto);
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
    @ApiOperation(value = "proporciona TOTES les armes de la tenda")
    public Response getArmas() {
            List<Objeto> armas = dao.findArmas();
            GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(armas) {};
            return Response.status(200).entity(entity).build();
    }

    @GET
    @Path("/tienda/skins")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "proporciona TOTES les skins de la tenda")
    public Response getSkin() {
        List<Objeto> skins = dao.findSkins();
        GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(skins) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @Path("/login/recordarContraseña")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "envia la pregunta de seguridad de un usuario")
    public Response getlogin(@QueryParam("id")String u) {
        try {
            String pregunta = dao.obtenerContra(u);
            return Response.ok(pregunta).build();
        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        }
    }
    @POST
    @Path("/login/recuperarCuenta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "inicia el proces de recuperació del compte")
    public Response loginUsuario(OlvContra usu) {
        try {
            Usuario u = dao.relogin(usu.getId(), usu.getRespuesta());
            return Response.status(200).entity(u).build(); // Login OK
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build(); // No autorizado
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build(); // Error general
        }
    }
    @POST
    @Path("/login/cambiarContraseña")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Cambia la contrasenya d'un usuari")
    public Response cambiarContra(Usulogin u) {
        try {
            dao.CambiarContra(u.getIdoname(), u.getPswd());
            return Response.status(200).entity("Contraseña cambiada con éxito").build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/tienda/{id}/armas")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "proporciona TOTES les armes d'un usuari")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Objeto.class, responseContainer="List"),
    })
    public Response getArmasUsuario(@PathParam("id")String u) {
        try {
            List<Objeto> armas = dao.armasUsuario(u);
            GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(armas) {};
            return Response.status(201).entity(entity).build();
        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayObjetos e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/tienda/{id}/skins")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "proporciona TOTES les skins d'un usuari")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Objeto.class, responseContainer="List"),
    })
    public Response getSkinUsuario(@PathParam("id")String u) {
        try {
            System.out.println("va el getSkinUsuario():");
            List<Objeto> skins = dao.skinsUsuario(u);
            GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(skins) {};
            // aixo s'utilitza per fer el json d'un contenidor d'objectes
            return Response.status(201).entity(entity).build();        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayObjetos e) {
            System.out.println("Error no hay objetos");
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/tienda/{id}/intercambio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Intercambia les flors per tarros de mel")
    public Response Conversion(@PathParam("id") String id) {
        try {
            Intercambio i = dao.intercambio(id);
            return Response.status(200).entity(i).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayFlores e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/informacion/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Dona un ranking de la informació d'un usuari")
    public Response getInfo(@PathParam("id") String id ) {
        List<Info> informcion = dao.informcion(id);
        GenericEntity<List<Info>> entity = new GenericEntity<List<Info>>(informcion) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @Path("/media")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideos() {
        List<VideoDTO> videos = new ArrayList<>();
        videos.add(new VideoDTO("youtube.com/watch?v=tAGnKpE4NCI"));
        videos.add(new VideoDTO("https://www.youtube.com/watch?v=_Yhyp-_hX2s"));
        videos.add(new VideoDTO("https://www.youtube.com/watch?v=5qm8PH4xAss"));
        videos.add(new VideoDTO("https://www.youtube.com/watch?v=bm51ihfi1p4"));

        System.out.println("[API] GET /media - Videos de soporte solicitados");

        VideoListDTO videoList = new VideoListDTO(videos);
        return Response.status(200).entity(videoList).build();
    }

    @GET
    @Path("/badges/{userId}/badges")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBadges(@PathParam("userId") String userId) {
        List<Badge> badges = new ArrayList<>();

        if ("carlos2003".equals(userId)) {
            badges.add(new Badge("master del universo",
                    "https://cdn.pixabay.com/photo/2017/07/11/15/51/kermit-2493979_1280.png"));
        }
        else if ("MSC78".equals(userId)) {
            badges.add(new Badge("becario enfurismado",
                    "https://cdn.pixabay.com/photo/2017/07/11/15/51/kermit-2493979_1280.png"));
        }
        else if ("Test".equals(userId)) {
            badges.add(new Badge("insignia especial para Test",
                    "https://cdn.pixabay.com/photo/2017/07/11/15/51/kermit-2493979_1280.png"));
        }
        else {
            badges.add(new Badge("insignia predeterminada",
                    "https://ejemplo.com/default.png"));
        }

        System.out.println("Retornando insignias para: " + userId);
        return Response.status(200).entity(new edu.upc.dsa.models.BadgeListDTO(badges)).build();
    }
}



