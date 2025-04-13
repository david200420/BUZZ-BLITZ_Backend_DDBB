package edu.upc.dsa;

import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.NoSuficientesTarrosException;
import edu.upc.dsa.exceptions.UsuarioNoAutenticadoException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.models.Compra;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.OlvContra;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface GameManager {

    public Usuario addUsuario(String id, String name, String contra, String mail, String a, String q) throws UsuarioYaRegistradoException;
    public Usuario login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;
    public List<Object> findSkins();
    public List<Object> findArmas();
    public Usuario Comprar (Compra compra) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException;
    public void addObjeto(Item objeto);
    public Item findObjeto(String idObjeto);
    public void initTestUsers() throws UsuarioYaRegistradoException;
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException;
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException;
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException;

}
