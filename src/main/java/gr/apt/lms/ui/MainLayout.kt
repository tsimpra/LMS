package gr.apt.lms.ui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.router.Route

@Route("")
class MainLayout : AppLayout() {
    init {
        val logo = H3("my Header")
        addToNavbar(logo)
    }
}