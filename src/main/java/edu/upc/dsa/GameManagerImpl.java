package edu.upc.dsa;
import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.NoSuficientesTarrosException;
import edu.upc.dsa.exceptions.UsuarioNoAutenticadoException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Tienda;
import edu.upc.dsa.models.Usuario;
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
    private Tienda tienda;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    public GameManagerImpl() {
        this.usuarios = new HashMap<>();
        this.usuariosm = new HashMap<>();
        this.objetos = new HashMap<>();
        this.tienda = new Tienda();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManagerImpl();
        return instance;
    }

    public Usuario addUsuario(String id, String name, String contra, String mail) throws UsuarioYaRegistradoException {
        logger.info("Registrando nuevo usuario: " + id + " / " + mail);

        if (usuarios.containsKey(id)) {
            throw new UsuarioYaRegistradoException("El USER ya está registrado");
        }

        if (usuariosm.containsKey(mail)) {
            throw new UsuarioYaRegistradoException("El MAIL ya está registrado");
        }

        Usuario nuevo = new Usuario(id, name, contra, mail);
        this.usuarios.put(id, nuevo);
        this.usuariosm.put(mail, nuevo);
        logger.info("Usuario registrado exitosamente");
        return nuevo;
    }
    public Usuario login(String mailOId, String pswd) throws CredencialesIncorrectasException {
        Usuario u = null;

        if (usuarios.containsKey(mailOId)) {
            u = usuarios.get(mailOId);
        } else if (usuariosm.containsKey(mailOId)) {
            u = usuariosm.get(mailOId);
        }

        if (u == null) {
            throw new CredencialesIncorrectasException("Usuario no encontrado");
        }

        if (!u.getPswd().equals(pswd)) {
            throw new CredencialesIncorrectasException("Contraseña incorrecta");
        }
        return u;
    }
    @Override
    public void addObjeto(Objeto objeto) {
        this.objetos.put(objeto.getId(), objeto);
    }

    @Override
    public Objeto findObjeto(String id) {
        return this.objetos.get(id);
    }

    @Override
    public Usuario Comprar (String idUsuari, Objeto objeto) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException { // va a ser un @PUT
        //Algo de que si idUsuari, mande una excepcion de que falta iniciar session, de quiero saber si
        // se ha registrado, mi idea esq en la web arriba a la derecha tengas un parametro con tu
        //id para almacenar la variable y poderla mandar en cada JSON
        Usuario u = this.usuarios.get(idUsuari);
        if(u == null) {
            throw new UsuarioNoAutenticadoException("Usuario no autenticado o no encontrado. Debe iniciar sesión.");
        }
        if(objeto.getPrecio() > u.getTarrosMiel()){
            throw new NoSuficientesTarrosException("No tienes suficiente Miel");
        }
            u.setTarrosMiel(u.getTarrosMiel() - objeto.getPrecio());
            if(objeto.getTipo() == 1){ //arma
                u.UpdateArmas(objeto);
            }
            else if(objeto.getTipo() == 2){ //skin
                u.UpdateSkin(objeto);
            }
            return u;
    }

    public void initTestUsers() throws UsuarioYaRegistradoException {
        try {
            this.addUsuario("Carlos2004", "Carlos", "123", "carlos@gmail.com");
            this.addUsuario("MSC78", "Marc", "321", "marc@gmail.com");
            this.addUsuario("Inad", "Dani", "147", "dani@gmail.com");
            this.addObjeto(new Objeto("1", "MotoSierra",20500, 1));
            this.addObjeto(new Objeto("2", "Camionero", 10000, 2));
            this.addObjeto(new Objeto("3", "Espada",11500 ,1));
        } catch (UsuarioYaRegistradoException e) {
            logger.warn("Usuario de prueba ya estaba registrado");
        }
    }

    @Override
    public List<Objeto> findAll() {
        List<Objeto> valores = new ArrayList<>(objetos.values());
        return valores;
    }

}
