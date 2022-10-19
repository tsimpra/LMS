package gr.apt.lms.utils

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.server.VaadinSession
import gr.apt.lms.config.security.TokenService
import gr.apt.lms.exception.LmsException
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.service.AuthenticationService
import io.quarkus.arc.Arc

fun <T : Any> Select<T>.autocomplete() {
    this.addAttachListener {
        if (this.listDataView.itemCount == 1) this.value = this.listDataView.getItem(0)
    }
}

fun getToken(): String? = VaadinSession.getCurrent().getAttribute("token")?.toString()
fun login(username: String, password: String) {
    val authenticationService = Arc.container().instance(AuthenticationService::class.java).get()
    val loggedInUser = authenticationService.authenticate(username, password)
    val token = TokenService.generateToken(
        loggedInUser.id!!,
        loggedInUser.username!!,
        loggedInUser.roles!!.mapNotNull { it.role }.toSet(),
        loggedInUser.roles!!.filter { it.id == Role.ROLE_ADMIN }.map { it.id }.first()
            ?: throw LmsException("User has no roles assigned. Please assign role to user and try again")
        //loggedInUser.roles!!.firstNotNullOfOrNull { it.id } ?: throw LmsException("User has no roles assigned. Please assign role to user and try again")
    )
    VaadinSession.getCurrent().setAttribute("token", token)
    UI.getCurrent().navigate("")
}

fun logout() = VaadinSession.getCurrent()?.session?.invalidate()

