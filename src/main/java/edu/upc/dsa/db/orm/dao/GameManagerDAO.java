package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;

import java.util.List;

public interface GameManagerDAO {


    public void addUsuario(String id, String name, String ape,String contra, String mail, String q, String a) throws UsuarioYaRegistradoException;
    public UsuarioEnviar login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;
    public DevolverCompra Comprar (usuario_objeto usuarioobjeto) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException;
    public List<Objeto> findSkins();
    public List<Objeto> findArmas();
    public List<Objeto> skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public List<Objeto> armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException;
    public Intercambio intercambio(String usuario) throws CredencialesIncorrectasException, NoHayFlores;
    public List<Info> informcion (String UserId);
}
