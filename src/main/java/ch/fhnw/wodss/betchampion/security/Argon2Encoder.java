package ch.fhnw.wodss.betchampion.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Hasan Kara
 */
public class Argon2Encoder implements PasswordEncoder {

    Argon2 argon2 = Argon2Factory.create();

    @Override
    public String encode(CharSequence rawPassword) {
        return argon2.hash(40, 128000, 4, String.valueOf(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return argon2.verify(encodedPassword, String.valueOf(rawPassword));
    }
}
