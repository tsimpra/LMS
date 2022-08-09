package gr.apt.security;

import io.smallrye.jwt.build.Jwt;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class TokenGenerator {

    public String generateToken(String username) {
        return Jwt.issuer("https://example.com/issuer")
                //.upn("jdoe@quarkus.io")
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim("username", username)
                .sign();
    }
}
