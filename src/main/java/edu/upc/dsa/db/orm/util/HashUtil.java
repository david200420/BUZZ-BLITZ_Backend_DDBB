package edu.upc.dsa.db.orm.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashUtil {
    // Fuerza por defecto: 10; puedes ajustar según necesidades/performance.
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Genera un hash BCrypt para el texto plano dado.
     */
    public static String hash(String plain) {
        return encoder.encode(plain);
    }

    /**
     * Verifica si el texto plano coincide con el hash almacenado.
     */
    public static boolean matches(String plain, String hashed) {
        if (plain == null || hashed == null) return false;
        return encoder.matches(plain, hashed);
    }
}
