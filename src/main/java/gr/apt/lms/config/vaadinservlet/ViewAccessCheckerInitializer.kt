package gr.apt.lms.config.vaadinservlet

import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.UIInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.auth.ViewAccessChecker
import gr.apt.lms.ui.views.LoginView

/*
    Listener that checks before entering on Page if user is authenticated via authenticationNavigation
    and if user has the authority to view the requested page via ViewAccessChecker
 */
class ViewAccessCheckerInitializer : VaadinServiceInitListener {

    override fun serviceInit(serviceInitEvent: ServiceInitEvent) {
        serviceInitEvent.source.addUIInitListener { uiInitEvent: UIInitEvent ->
            uiInitEvent.ui.addBeforeEnterListener(this::authenticateNavigation)
            uiInitEvent.ui.addBeforeEnterListener(ViewAccessChecker())
        }
    }

    private fun authenticateNavigation(event: BeforeEnterEvent) {
        if (
            LoginView::class.java != event.navigationTarget
            && !isAuthenticated()
        ) {
            event.rerouteTo(LoginView::class.java)
        }
    }

    private fun isAuthenticated(): Boolean {
        val request = VaadinServletRequest.getCurrent()
        return request != null && request.userPrincipal != null
    }
}