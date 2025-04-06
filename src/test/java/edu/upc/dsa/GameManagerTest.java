package edu.upc.dsa;

import edu.upc.dsa.exceptions.CredencialesIncorrectasException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.exceptions.UsuarioYaRegistradoException;
import edu.upc.dsa.models.Usuario;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameManagerTest {
    GameManager gm;

    @Before
    public void setUp() throws UsuarioYaRegistradoException {
        gm = GameManagerImpl.getInstance();

        gm.addUsuario("UTest", "Usuario Test Test", "test123", "test@mail.com");
    }

    @Test
    public void testLoginExitoso() throws CredencialesIncorrectasException {
        Usuario u = ((GameManagerImpl) gm).login("test@mail.com", "test123");
        assertNotNull(u);
        assertEquals("testuser", u.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginContrasenaIncorrecta() {
        ((GameManagerImpl) gm).login("test@mail.com", "wrongpass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginUsuarioNoExiste() {
        ((GameManagerImpl) gm).login("noexiste@mail.com", "whatever");
    }
}
