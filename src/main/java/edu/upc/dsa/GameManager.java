package edu.upc.dsa;

import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.NoSuficientesTarrosException;
import edu.upc.dsa.exceptions.UsuarioNoAutenticadoException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.models.Objeto;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface GameManager {

    public Usuario addUsuario(String id, String name, String contra, String mail) throws UsuarioYaRegistradoException;
    public Usuario login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;
    public List<Objeto> findAll();
    public Usuario Comprar (String idUsuari, Objeto objeto) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException;
    public void addObjeto(Objeto objeto);
    public Objeto findObjeto(String idObjeto);
    public void initTestUsers() throws UsuarioYaRegistradoException;

}
