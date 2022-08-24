package gr.apt.config.security

import io.smallrye.jwt.build.Jwt
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TokenGenerator {
    fun generateToken(username: String?): String {
        //TODO: make generate token work for real time service
        return Jwt.issuer("https://example.com/issuer") //.upn("jdoe@quarkus.io")
            .groups(HashSet(Arrays.asList("User", "Admin")))
            .claim("username", username)
            .sign()
    }
}