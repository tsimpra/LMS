package gr.apt.lms.config.security

import io.quarkus.arc.Arc
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.build.Jwt
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
object TokenService {

    internal const val USERNAME_CLAIM = "username"
    internal const val PERSON_CLAIM = "person"
    internal const val SELECTED_ROLE_CLAIM = "role"

    private const val secret: String =
        "obc123tr1b64rt238rtb2878tfyb83224gdfbgvbvfdusifgsvhsbavbsvbkasjvmnkvbnsdfbvsdnbdgbdssdbdfbsg"

    private val parser = Arc.container().instance(JWTParser::class.java).get()

    fun generateToken(username: String): String {
        //TODO: make generate token work for real time service
        return Jwt.issuer("https://example.com/issuer")
            .subject("LMS")
            .groups(HashSet(listOf("User", "Admin")))
            .claim(USERNAME_CLAIM, username)
            .claim(PERSON_CLAIM, 1)
            .claim(SELECTED_ROLE_CLAIM, -44)
            .issuedAt(currentTimeSecs())
            .expiresAt(currentTimeSecs() + 3600)
            .jws()
            .signWithSecret(secret)
    }

    fun generateToken(person: BigInteger, username: String, roles: Set<String>, selectedRole: BigInteger): String {
        return Jwt.issuer("https://example.com/issuer")
            .subject("LMS")
            .groups(roles)
            .claim(USERNAME_CLAIM, username)
            .claim(PERSON_CLAIM, person)
            .claim(SELECTED_ROLE_CLAIM, selectedRole)
            .issuedAt(currentTimeSecs())
            .expiresAt(currentTimeSecs() + 3600)
            .jws()
            .signWithSecret(secret)
    }

    fun getPersonFromToken(token: String): Any? = parser.verify(token, secret).getClaim(PERSON_CLAIM)

    fun getUsernameFromToken(token: String): String? = parser.verify(token, secret).getClaim(USERNAME_CLAIM)

    fun getSelectedRoleFromToken(token: String): Any? = parser.verify(token, secret).getClaim(SELECTED_ROLE_CLAIM)

    fun getRolesFromToken(token: String): Set<String>? = parser.verify(token, secret).groups

    fun isTokenExpired(token: String): Boolean =
        parser.verify(token, secret).expirationTime < currentTimeSecs()

    fun currentTimeSecs() = System.currentTimeMillis() / 1000


}