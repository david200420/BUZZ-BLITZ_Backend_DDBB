package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;

import java.util.List;

public interface GameManager {

    public void addUsuario(String id, String name, String contra, String mail, String a, String q) throws UsuarioYaRegistradoException;
    public UsuarioEnviar login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;
    public ConsultaTienda findSkins();
    public ConsultaTienda findArmas();
    public Usuario Comprar (Compra compra) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException;
    public void addObjeto(Objeto objeto);
    public Objeto findObjeto(String idObjeto);
    public void initTestUsers() throws UsuarioYaRegistradoException;
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException;
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException;
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException;
    public ConsultaTienda skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public ConsultaTienda armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    void deleteUsuario(String id) throws UsuarioNoEncontradoException;

}
