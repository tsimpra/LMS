package gr.apt.lms.ui

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import gr.apt.lms.ui.persons.PersonList

@Route("")
class MainLayout : AppLayout() {
    init {
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        val listLink = RouterLink("Persons List", PersonList::class.java)
        listLink.highlightCondition = HighlightConditions.sameLocation()
        addToDrawer(VerticalLayout(listLink))
    }

    private fun createHeader() {
        val logo = H3("my Header")
        logo.addClassName("logo")
        val header = HorizontalLayout(DrawerToggle(), logo)
        header.addClassName("header")
        header.setWidthFull()
        //header.expand(logo);

        val logoIcon = Image("/icons/icon.png", "Alt Image")
        logoIcon.setHeight(10.0f, Unit.MM)
        logoIcon.setWidth(30.0f, Unit.MM)

        header.defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
        addToNavbar(logoIcon, header)
    }
}