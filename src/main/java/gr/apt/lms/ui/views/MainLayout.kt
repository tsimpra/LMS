package gr.apt.lms.ui.views

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinSession
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.config.security.LmsPrincipal
import gr.apt.lms.config.security.TokenService
import gr.apt.lms.exception.LmsException
import gr.apt.lms.metamodel.entity.Role_
import gr.apt.lms.persistence.entity.Role
import gr.apt.lms.repository.RoleRepository
import gr.apt.lms.service.getToken
import gr.apt.lms.service.logout
import gr.apt.lms.ui.components.AutoCompletableSelect
import gr.apt.lms.ui.components.MenuTabTree
import io.quarkus.arc.Arc
import java.math.BigInteger
import javax.annotation.security.PermitAll


@Route("")
@UIScoped
@PermitAll
class MainLayout : AppLayout() {
    private var treeGrid = MenuTabTree()
    private var selectedRole = AutoCompletableSelect<Role>(Role_.ROLE_HEADER)
    private var layout = VerticalLayout()
    private val principal: LmsPrincipal
        get() = VaadinServletRequest.getCurrent().userPrincipal as? LmsPrincipal
            ?: throw LmsException("user is not authenticated")

    init {
        createHeader()
        createDrawer()
        configureSelectedRole()
    }

    internal fun createDrawer() {
//        //remove tree grid if its already attached and instantiate new tree grid
        if (layout.isAttached) {
            this.layout.removeAll()
            treeGrid.treeData.clear()
            treeGrid.removeAllColumns()
            this.remove(layout)
            layout = VerticalLayout()
            treeGrid = MenuTabTree()
        }

        layout.add(treeGrid, selectedRole)
        layout.setHeightFull()

        addToDrawer(layout)
    }

    private fun configureSelectedRole() {
        val roleRepository = Arc.container().instance(RoleRepository::class.java).get()
        selectedRole.setItems(roleRepository.getPersonRoles(BigInteger(principal.name)))
        selectedRole.value =
            selectedRole.listDataView.items.filter { it.id == principal.selectedRole }.findFirst().get()
        selectedRole.setItemLabelGenerator {
            it.role
        }
        selectedRole.addValueChangeListener { event ->
            if (event.value != null) {
                val newToken = TokenService.generateTokenWithNewSelectedRole(
                    getToken() ?: throw LmsException("user is not authenticated"),
                    event.value.id!!
                )
                VaadinSession.getCurrent().setAttribute("token", newToken)
                createDrawer()
                UI.getCurrent().navigate("/")
            }
        }
    }

    private fun createHeader() {
        val header = HorizontalLayout(DrawerToggle())
        header.addClassName("header")
        header.setWidthFull()
        header.isMargin = true

        //check if user is authenticated (has token) and if he is, then create the logout button
        if (getToken() != null) {
            val logout = Button("Logout") { logout() }
            logout.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY)
            //style to align component on right side
            logout.style.set("margin-left", "auto")
            header.add(logout)
        }

        //logo
        val logoIcon = Image("/icons/icon.png", "Alt Image")
        logoIcon.setHeight(10.0f, Unit.MM)
        logoIcon.setWidth(30.0f, Unit.MM)
        logoIcon.addClickListener { UI.getCurrent().navigate("/") }

        header.defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
        addToNavbar(logoIcon, header)
    }
}