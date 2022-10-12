package gr.apt.lms.ui.views

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteConfiguration
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinSession
import com.vaadin.quarkus.annotation.VaadinSessionScoped
import gr.apt.lms.config.security.LmsPrincipal
import gr.apt.lms.exception.LmsException
import gr.apt.lms.persistence.entity.Menu
import gr.apt.lms.repository.MenuRepository
import gr.apt.lms.ui.components.MenuTab
import io.quarkus.arc.Arc
import javax.annotation.security.PermitAll


@Route("")
@VaadinSessionScoped
@PermitAll
class MainLayout : AppLayout() {
    private var treeGrid = TreeGrid<MenuTab>()
    private var selectedNode: MenuTab? = null

    init {
        //checkAuthentication()
        createHeader()
        createDrawer()
    }

    internal fun createDrawer() {
        //remove tree grid if its already attached and instantiate new tree grid
        if (treeGrid.isAttached) {
            this.remove(treeGrid)
            treeGrid = TreeGrid<MenuTab>()
        }
        //fetch user's menu
        val principal = VaadinServletRequest.getCurrent().userPrincipal as? LmsPrincipal
            ?: throw LmsException("user is not authenticated")
        val selectedRole = principal.selectedRole
        val menuRepository = Arc.container().instance(MenuRepository::class.java).get()
        val list: List<Menu> = menuRepository.getUserMenuByRoleId(selectedRole)
        val routerList = list.map {
            val route = RouteConfiguration.forSessionScope().getRoute(it.path)
            MenuTab(it, RouterLink("", route.get()))
        }
        //create menu tree
        val parents = routerList.filter { it.ref == null }
        val children = { parent: MenuTab -> routerList.filter { it.ref == parent.id } }
        treeGrid.setItems(parents, children)

        //expand tree if selected node exists
        if (selectedNode != null) treeGrid.expand(selectedNode)

        //for grid selection
        treeGrid.asSingleSelect().addValueChangeListener { changed ->
            if (changed.value != null) {
                selectedNode = changed.value
                val href = changed.value.router.href
                if (href.isNotEmpty()) {
                    treeGrid.select(changed.value)
                    UI.getCurrent().navigate(href)
                } else
                    if (treeGrid.isExpanded(changed.value))
                        treeGrid.collapse(changed.value)
                    else
                        treeGrid.expand(changed.value)
            } else {
                selectedNode = null
                treeGrid.collapse(treeGrid.treeData.rootItems)
            }
        }
        //for when selecting the label instead of the grid
        treeGrid.addItemClickListener {
            treeGrid.select(it.item)
        }
        treeGrid.addComponentHierarchyColumn {
            it
        }

        treeGrid.setHeightFull()
        addToDrawer(treeGrid)
    }

    private fun createHeader() {
        var header = HorizontalLayout(DrawerToggle())
        header.addClassName("header")
        header.setWidthFull()
        //header.expand(logo);

        //check if user is authenticated (has token) and if he is create the logout button
        if (VaadinSession.getCurrent().getAttribute("token") != null) {
            val logout = Button("Logout") {
                VaadinSession.getCurrent().session.invalidate()
                UI.getCurrent().page.setLocation("/login")
            }
            header.add(logout)
            header.setAlignSelf(FlexComponent.Alignment.END, logout)
        }

        val logoIcon = Image("/icons/icon.png", "Alt Image")
        logoIcon.setHeight(10.0f, Unit.MM)
        logoIcon.setWidth(30.0f, Unit.MM)

        header.defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
        addToNavbar(logoIcon, header)
    }

//    fun checkAuthentication() {
//        val token = VaadinSession.getCurrent().getAttribute("token") as? String
//        if (token == null)
//            ui.get().navigate(LoginView::class.java)
//        else {
//            val personId = TokenService.getPersonFromToken(token)
//            if (personId == null || TokenService.isTokenExpired(token)) {
//                ui.get().navigate(LoginView::class.java)
//            }
//        }
//    }
}