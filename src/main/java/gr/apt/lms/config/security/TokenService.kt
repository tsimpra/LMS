package gr.apt.lms.config.security

import com.vaadin.flow.component.notification.Notification
import gr.apt.lms.service.logout
import io.quarkus.arc.Arc
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.auth.principal.ParseException
import io.smallrye.jwt.build.Jwt
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
object TokenService {

    internal const val USERNAME_CLAIM = "username"
    private const val PERSON_CLAIM = "person"
    private const val SELECTED_ROLE_CLAIM = "role"

    private const val secret: String =
        "obc123tr1b64rt238rtb2878tfyb83224gdfbgvbvfdusi1gsvh3bavbsvbkasjvmnkv1nsdfbvsdn27gbds5dbd9bsg"

    private val parser = Arc.container().instance(JWTParser::class.java).get()

    fun generateToken(username: String): String = Jwt.issuer("https://lms.gr/issuer")
        .subject("LMS")
        .groups(HashSet(listOf("User", "Admin")))
        .claim(USERNAME_CLAIM, username)
        .claim(PERSON_CLAIM, 1)
        .claim(SELECTED_ROLE_CLAIM, -44)
        .issuedAt(currentTimeSecs())
        .expiresAt(currentTimeSecs() + 3600)
        .jws()
        .signWithSecret(secret)


    fun generateToken(person: BigInteger, username: String, roles: Set<String>, selectedRole: BigInteger): String =
        Jwt.issuer("https://lms.gr/issuer")
            .subject("LMS")
            .groups(roles)
            .claim(USERNAME_CLAIM, username)
            .claim(PERSON_CLAIM, person)
            .claim(SELECTED_ROLE_CLAIM, selectedRole)
            .issuedAt(currentTimeSecs())
            .expiresAt(currentTimeSecs() + 3600)
            .jws()
            .signWithSecret(secret)


    fun generateTokenWithNewSelectedRole(token: String, newSelectedRole: BigInteger): String =
        Jwt.issuer("https://lms.gr/issuer")
            .subject("LMS")
            .groups(getRolesFromToken(token))
            .claim(USERNAME_CLAIM, getUsernameFromToken(token))
            .claim(PERSON_CLAIM, getPersonFromToken(token))
            .claim(SELECTED_ROLE_CLAIM, newSelectedRole)
            .issuedAt(currentTimeSecs())
            .expiresAt(currentTimeSecs() + 3600)
            .jws()
            .signWithSecret(secret)

    fun getPersonFromToken(token: String): Any? {
        return try {
            parser.verify(token, secret).getClaim(PERSON_CLAIM) as? Any
        } catch (ex: ParseException) {
            println("token has expired")
            Notification.show("token has expired")
            logout()
            null
        }
    }

    fun getUsernameFromToken(token: String): String? {
        return try {
            parser.verify(token, secret).getClaim(USERNAME_CLAIM)
        } catch (ex: ParseException) {
            println("token has expired")
            Notification.show("token has expired")
            logout()
            null
        }
    }

    fun getSelectedRoleFromToken(token: String): Any? {
        return try {
            parser.verify(token, secret).getClaim(SELECTED_ROLE_CLAIM) as? Any
        } catch (ex: ParseException) {
            println("token has expired")
            Notification.show("token has expired")
            logout()
            null
        }
    }


    fun getRolesFromToken(token: String): Set<String>? {
        return try {
            parser.verify(token, secret).groups
        } catch (ex: ParseException) {
            println("token has expired")
            Notification.show("token has expired")
            logout()
            null
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            parser.verify(token, secret).expirationTime < currentTimeSecs()
        } catch (ex: ParseException) {
            println("token has expired")
            Notification.show("token has expired")
            logout()
            true
        }
    }

    private fun currentTimeSecs() = System.currentTimeMillis() / 1000

}