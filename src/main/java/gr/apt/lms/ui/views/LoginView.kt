package gr.apt.lms.ui.views

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinSession
import com.vaadin.flow.server.auth.AnonymousAllowed
import gr.apt.lms.config.security.TokenService
import gr.apt.lms.exception.LmsException
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.service.AuthenticationService
import io.quarkus.arc.Arc

@Route("/login")
@AnonymousAllowed
class LoginView : Div() {

    private val login = LoginForm()
    private val authenticationService = Arc.container().instance(AuthenticationService::class.java).get()

    init {
        login.className = "login-view"
        // Demo purposes only
        style.set("background-color", "var(--lumo-contrast-5pct)")
            .set("display", "flex").set("justify-content", "center")
            .set("padding", "var(--lumo-space-l)");

        login.addLoginListener { event ->
            val loggedInUser = authenticationService.authenticate(event.username, event.password)
            val token = TokenService.generateToken(
                loggedInUser.id!!,
                loggedInUser.username!!,
                loggedInUser.roles!!.mapNotNull { it.role }.toSet(),
                loggedInUser.roles!!.filter { it.id == Role.ROLE_ADMIN }.map { it.id }.first()
                    ?: throw LmsException("User has no roles assigned. Please assign role to user and try again")
                //loggedInUser.roles!!.firstNotNullOfOrNull { it.id } ?: throw LmsException("User has no roles assigned. Please assign role to user and try again")
            )
            VaadinSession.getCurrent().setAttribute("token", token)
            UI.getCurrent().navigate("/")
        }
        login.isForgotPasswordButtonVisible = false
        add(login)
        setSizeFull()
    }
}