package edu.upc.dsa;

import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.models.Usuario;

import java.util.List;

public interface GameManager {

    public void initTestUsers() throws UsuarioYaRegistradoException;
    public Usuario addUsuario(String id, String name, String contra, String mail) throws UsuarioYaRegistradoException;
    public Usuario login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;


}
