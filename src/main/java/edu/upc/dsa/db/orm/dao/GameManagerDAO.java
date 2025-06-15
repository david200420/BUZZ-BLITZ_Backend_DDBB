package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;

import java.util.List;

public interface GameManagerDAO {


    public void addUsuario(String id, String name, String ape,String contra, String mail, String q, String a) throws UsuarioYaRegistradoException;
    public UsuarioEnviar login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;
    public DevolverCompra Comprar (Usuario_objeto usuarioobjeto) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException;
    public List<Objeto> findSkins();
    public List<Objeto> findArmas();
    public List<Objeto> skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public List<Objeto> armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException;
    public Intercambio intercambio(String usuario) throws CredencialesIncorrectasException, NoHayFlores;
    public List<Info> informcion (String UserId);
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException; // Cambia la contraseña del usuario
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException; //Obtiene la pregunta de seguridad del usuario para el relogin
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException; //comprueba la respuesta
    List<Badge> getUserBadges(String userId) throws Exception;
    ListFreqQuest getPreguntasFrecuentes();
    void addQuestion(Question question) throws Exception;
    void addIssue(Issue issue) throws Exception;
    List<Issue> getAllIssues() throws Exception;

    String recuperarCuenta(String id, String respuesta) throws CredencialesIncorrectasException;

    String obtenerPreguntaSeguridad(String id) throws UsuarioNoEncontradoException;
}
