package edu.upc.dsa.services;


import edu.upc.dsa.db.orm.dao.GameManagerDAO;
import edu.upc.dsa.db.orm.dao.GameManagerDAOImpl;
import edu.upc.dsa.db.orm.util.HashUtil;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            String HashedAns = HashUtil.hash(usuReg.getRespuesta()); // Aquí deberías aplicar el hash a la contraseña
            String HashedPswd = HashUtil.hash(usuReg.getPswd());
            dao.addUsuario(usuReg.getId(), usuReg.getName(), usuReg.getApellidos(),HashedPswd, usuReg.getMail(), usuReg.getPregunta(), HashedAns);
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
        }catch (Throwable t) {
            t.printStackTrace();
            return null;
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

    @GET
    @Path("/login/obtenerPregunta")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Obtiene la pregunta de seguridad de un usuario")
    public Response obtenerPreguntaSeguridad(@QueryParam("id") String id) {
        try {
            String pregunta = dao.obtenerPreguntaSeguridad(id);
            return Response.ok(pregunta).build();
        } catch (UsuarioNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }

    @POST
    @Path("/login/recuperarCuenta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response recuperarCuenta(OlvContra usu) {
        try {
            String tempPassword = dao.recuperarCuenta(usu.getId(), usu.getRespuesta());
            return Response.ok("Tu nueva contraseña temporal es: " + tempPassword).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }

    @POST
    @Path("/login/cambiarContraseña")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiarContra(Usulogin u) {
        try {
            dao.CambiarContra(u.getIdoname(), u.getPswd());
            return Response.status(200).entity("Contraseña cambiada con éxito").build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
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
	    List<Objeto> skins = null;
        try {
            System.out.println("va el getSkinUsuario():");
            skins = dao.skinsUsuario(u);
            GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(skins) {};
            // aixo s'utilitza per fer el json d'un contenidor d'objectes
            return Response.status(201).entity(entity).build();        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayObjetos e) {
            System.out.println("Error no hay objetos");
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Throwable t) {
		t.printStackTrace();
	}
	return null;
    }

    @PUT
    @Path("/tienda/{id}/intercambio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Intercambia les flors per tarros de mel")
    public Response Conversion(@PathParam("id") String id) {
	    Intercambio i = null;
        try {
            i = dao.intercambio(id);
            return Response.status(200).entity(i).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayFlores e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Throwable t) {
		t.printStackTrace();
	}
	return null;
	
    }

    @GET
    @Path("/informacion/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfo(@PathParam("id") String id) {
        try {
            List<Info> informcion = dao.informcion(id);

            // Calcular la posición del usuario en el ranking
            int posicionUsuario = 1;
            boolean encontrado = false;
            for (Info info : informcion) {
                if (info.getUsuario().equals(id)) {
                    encontrado = true;
                    break;
                }
                posicionUsuario++;
            }

            if (!encontrado) {
                posicionUsuario = -1; // Usuario no encontrado en el ranking
            }

            // Crear la respuesta con la estructura correcta
            RankingResponse response = new RankingResponse(informcion, posicionUsuario);

            return Response.status(200).entity(response).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/media")
    public Response getVideos() {
        List<VideoDTO> videos = new ArrayList<>();
        videos.add(new VideoDTO("youtube.com/watch?v=tAGnKpE4NCI"));
        videos.add(new VideoDTO("https://www.youtube.com/watch?v=_Yhyp-_hX2s"));
        videos.add(new VideoDTO("https://www.youtube.com/watch?v=5qm8PH4xAss"));
        videos.add(new VideoDTO("https://www.youtube.com/watch?v=bm51ihfi1p4"));
        //Prueba de video
        VideoListDTO videoList = new VideoListDTO(videos);
        return Response.status(200).entity(videoList).build();
    }

    @GET
    @Path("/badges/{userId}/badges")
    public Response getUserBadges(@PathParam("userId") String userId) {
        try {
            List<Badge> badges = dao.getUserBadges(userId);
            return Response.status(200).entity(new BadgeListDTO(badges)).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/faqs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFaqs() {
        try {
            ListFreqQuest faqs = dao.getPreguntasFrecuentes();
            return Response.status(200).entity(faqs).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/question")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Envía una nueva consulta")
    public Response submitQuestion(Question question) {
        try {
            question.setId(UUID.randomUUID().toString());
            dao.addQuestion(question);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/issue")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportIssue(Issue issue) {
        try {
            dao.addIssue(issue);
            return Response.status(201).build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/issue/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllIssues() {
        try {
            List<Issue> issues = dao.getAllIssues();
            System.out.println("Issues encontrados: " + issues.size()); // Log de diagnóstico

            GenericEntity<List<Issue>> entity = new GenericEntity<List<Issue>>(issues) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log completo del error
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/resetData/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Resetea los datos del usuario a valores iniciales")
    public Response resetUserData(@PathParam("id") String id) {
        try {
            dao.resetUserData(id);
            return Response.status(200).entity("Datos reseteados con éxito").build();
        } catch (UsuarioNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/forum/messages")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Obtener todos los mensajes del foro")
    public Response getForumMessages() {
        try {
            List<Forum> messages = dao.getForumMessages();
            GenericEntity<List<Forum>> entity = new GenericEntity<List<Forum>>(messages) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/forum/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Publicar un mensaje en el foro")
    public Response postForumMessage(Forum forum) {
        try {
            forum.setId(UUID.randomUUID().toString());
            forum.setDate(new Date().toString());
            dao.addForumMessage(forum);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/chat/users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Obtener todos los usuarios")
    public Response getAllUsers() {
        try {
            List<Usuario> users = dao.getAllUsers();
            GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(users) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/chat/{user1}/{user2}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivateMessages(
            @PathParam("user1") String user1,
            @PathParam("user2") String user2) {

        try {
            List<ChatIndividual> messages = dao.getPrivateMessages(user1, user2);
            GenericEntity<List<ChatIndividual>> entity = new GenericEntity<List<ChatIndividual>>(messages) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/chat/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Enviar un mensaje privado")
    public Response sendPrivateMessage(ChatIndividual chat) {
        try {
            chat.setId(UUID.randomUUID().toString());
            chat.setDate(new Date().toString());
            dao.addPrivateMessage(chat);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }
    @POST
    @Path("/partida/guardar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Guardar una partida")
    public Response guardarPartida(PartidaGuardada partida) {
        try {
            dao.guardarPartida(partida);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor: " + e.getMessage()).build();
        }
    }
}



