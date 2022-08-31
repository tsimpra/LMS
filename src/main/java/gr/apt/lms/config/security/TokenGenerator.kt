package gr.apt.lms.config.security

import io.smallrye.jwt.build.Jwt
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
object TokenGenerator {

    internal const val USERNAME_CLAIM = "username"
    fun generateToken(username: String): String {
        //TODO: make generate token work for real time service
        return Jwt.issuer("https://example.com/issuer") //.upn("jdoe@quarkus.io")
            .groups(HashSet(Arrays.asList("User", "Admin")))
            .claim(USERNAME_CLAIM, username)
            .sign()
    }
}