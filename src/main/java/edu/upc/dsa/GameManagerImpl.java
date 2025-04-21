package edu.upc.dsa;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    private Map<String, Objeto> objetos;
    private Map<String, Usuario> usuarios;
    private Map<String, Usuario> usuariosm;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    public GameManagerImpl() {
        this.usuarios = new HashMap<>();
        this.usuariosm = new HashMap<>();
        this.objetos = new HashMap<>();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManagerImpl();
        return instance;
    }

    public Usuario obtenerUsuario(String id) throws CredencialesIncorrectasException {
        Usuario u = null;
        if (usuarios.containsKey(id)) {
            u = usuarios.get(id);
        } else if (usuariosm.containsKey(id)) {
            u = usuariosm.get(id);
        }
        return u;
    }

    @Override
    public void addUsuario(String id, String name, String contra, String mail, String q, String a) throws UsuarioYaRegistradoException {
        logger.info("Registrando nuevo usuario: " + id + " / " + mail);

        if (usuarios.containsKey(id)) {
            throw new UsuarioYaRegistradoException("El USER ya está registrado");
        }

        if (usuariosm.containsKey(mail)) {
            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
        }

        Usuario nuevo = new Usuario(id, name, contra, mail,q,a);
        this.usuarios.put(id, nuevo);
        this.usuariosm.put(mail, nuevo);
        logger.info("Usuario registrado exitosamente");
    }

    @Override
    public UsuarioEnviar login(String mailOId, String pswd) throws CredencialesIncorrectasException {
       logger.info("Iniciando login");
        Usuario u = obtenerUsuario(mailOId);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getPswd().equals(pswd)) {
            throw new CredencialesIncorrectasException("Contraseña incorrecta");
        }
        UsuarioEnviar usu = new UsuarioEnviar(u.getId(), u.getName(), u.getPswd(), u.getMail(), u.getPregunta(), u.getRespuesta());
        return usu;
    }
    @Override
    public void addObjeto(Objeto objeto) {
        logger.info("Iniciando objeto");
        this.objetos.put(objeto.getId(), objeto);
    }

    @Override
    public Objeto findObjeto(String id) {
        return this.objetos.get(id);
    }

    @Override
    public Usuario Comprar (Compra compra) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException { // va a ser un @PUT
        //Algo de que si idUsuari, mande una excepcion de que falta iniciar session, de quiero saber si
        // se ha registrado, mi idea esq en la web arriba a la derecha tengas un parametro con tu
        //id para almacenar la variable y poderla mandar en cada JSON
        Usuario u = this.usuarios.get(compra.getUsuarioId());
        if(u == null) {
            throw new UsuarioNoAutenticadoException("Usuario no autenticado o no encontrado. Debe iniciar sesión.");
        }
        if(compra.getObjeto().getPrecio() > u.getTarrosMiel()){
            throw new NoSuficientesTarrosException("No tienes suficiente Miel");
        }
            u.setTarrosMiel(u.getTarrosMiel() - compra.getObjeto().getPrecio());
            if(compra.getObjeto().getTipo() == 1){ //arma
                u.UpdateArmas(compra.getObjeto());
            }
            else if(compra.getObjeto().getTipo() == 2){ //skin
                u.UpdateSkin(compra.getObjeto());
            }
            return u;
    }
    @Override
    public void initTestUsers() throws UsuarioYaRegistradoException {
        try {
            this.addUsuario("carlos2004", "Carlos", "123", "carlos@gmail.com","Tu comida favorita?","Arroz" );
            this.addUsuario("MSC78", "Marc", "321", "marc@gmail.com","Como se llamaba tu escuela de Primaria?" ,"Dali" );
            this.addUsuario("Test", "Dani", "147", "dani@gmail.com","El nombre de tu familiar mas mayor?" ,"Teresa" );
            this.addObjeto(new Objeto("1", "Palo",200 ,1, "Un paaaaaaaaaaaaaalo","palo1"));
            this.addObjeto(new Objeto("2", "Hacha",700, 1,"Un hacha asequible para todos pero mortal como ninguna, su especialidad: las telarañas" ,"hacha1"));
            this.addObjeto(new Objeto("3", "Gorro Pirata", 1000, 2, "Para surcar los mares","gorropirata"));
            this.addObjeto(new Objeto("4", "Gorro Patito", 1000, 2, "Para nadar mucho","gorropatito"));
            this.addObjeto(new Objeto("5", "Mister Potato", 1000, 2, "Para ser feliz","misterpotato"));
            this.addObjeto(new Objeto("7", "Espada",1150 ,1, "Un corte profundo que hiere a las arañas más poderosas","espada1"));
            this.addObjeto(new Objeto("8", "Espada Real",1350 ,1, "De su corte se entera hasta la mismisima Anansi","espada2"));
        } catch (UsuarioYaRegistradoException e) {
            logger.warn("Usuario de prueba ya estaba registrado");
        }
    }

    @Override
    public ConsultaTienda findArmas() {

        HashMap<String, Objeto> armas = new HashMap<>();
        for (Objeto obj : this.objetos.values()) {
            if (obj.getTipo() == 1) { // tipo 1 = arma
                armas.put(obj.getId(), obj);
            }
        }
        List<Objeto> listaItems = new ArrayList<Objeto>(armas.values());
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }
    @Override
    public ConsultaTienda findSkins() {

        HashMap<String, Objeto> skins = new HashMap<>();
        for (Objeto obj : this.objetos.values()) {
            if (obj.getTipo() == 2) { // tipo 2 = arma
                skins.put(obj.getId(), obj);
            }
        }
        List<Objeto> listaItems = new ArrayList<Objeto>(skins.values());
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException {

        logger.info("Obteniendo pregunta");
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        logger.info(u.getPregunta());
        return u.getPregunta();
    }

    @Override
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException {
        Usuario u = obtenerUsuario(id);

        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getRespuesta().equals(respuesta)) {
            throw new CredencialesIncorrectasException("Respuesta incorrecta");
        }
        return u;


    }

    @Override
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        u.setRespuesta(contra);

    }

    @Override
    public ConsultaTienda skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        List<Objeto> listaItems = new ArrayList<>(u.getSkins().values());

        if (listaItems.isEmpty()) {
            throw new NoHayObjetos("No tienes skins");
        }

        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public ConsultaTienda armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Usuario u = obtenerUsuario(usuario);
        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }
        List<Objeto> listaItems = new ArrayList<>(u.getArmas().values());

        if (listaItems.isEmpty()) {
            throw new NoHayObjetos("No tienes armas");
        }
        ConsultaTienda items = new ConsultaTienda(listaItems);
        return items;
    }

    @Override
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        String email = usuario.getMail();
        usuarios.remove(id);
        usuariosm.remove(email);
        logger.info("Usuario eliminado: " + id);
    }
}
