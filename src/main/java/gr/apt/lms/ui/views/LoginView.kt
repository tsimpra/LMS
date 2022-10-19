package gr.apt.lms.ui.views

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import gr.apt.lms.service.login

@Route("/login")
@PreserveOnRefresh
@AnonymousAllowed
class LoginView : Div() {

    private val login = LoginForm()

    init {
        login.className = "login-view"
        // Demo purposes only
        style.set("background-color", "var(--lumo-contrast-5pct)")
            .set("display", "flex")
            .set("justify-content", "center")
            .set("padding", "var(--lumo-space-l)");

        login.addLoginListener { event -> login(event.username, event.password) }
        login.isForgotPasswordButtonVisible = false
        add(login)
        setSizeFull()
    }
}