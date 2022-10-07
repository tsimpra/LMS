package gr.apt.lms.ui

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteConfiguration
import com.vaadin.flow.router.RouterLink
import gr.apt.lms.persistence.entity.Menu
import gr.apt.lms.repository.MenuRepository
import io.quarkus.arc.Arc
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@Route("")
@ApplicationScoped
class MainLayout : AppLayout() {
    private var treeGrid = TreeGrid<MenuTab>()
    private var selectedNode: MenuTab? = null

    init {
        createHeader()
        createDrawer()
    }

    internal fun createDrawer() {
        if (treeGrid.isAttached) {
            this.remove(treeGrid)
            treeGrid = TreeGrid<MenuTab>()
        }
        val menuRepository = Arc.container().instance(MenuRepository::class.java).get()
        val list: List<Menu> = menuRepository.getUserMenuByUserId(BigInteger.valueOf(-44L))
        val routerList = mutableListOf<MenuTab>()
        list.forEach {
            val route = RouteConfiguration.forSessionScope().getRoute(it.path)
            val routerLink = MenuTab(it, RouterLink("", route.get()))
            routerList.add(routerLink)
        }
        val parents = routerList.filter { it.ref == null }
        val children = { parent: MenuTab -> routerList.filter { it.ref == parent.id } }
        treeGrid.setItems(parents, children)

        if (selectedNode != null) treeGrid.expand(selectedNode)

        treeGrid.asSingleSelect().addValueChangeListener { changed ->
            if (changed.value != null) {
                selectedNode = changed.value
                val href = changed.value.router.href
                if (href.isNotEmpty()) {
                    treeGrid.select(changed.value)
                    UI.getCurrent().navigate(href)
                    //if(changed.value.ref!=null)treeGrid.expand(treeGrid.treeData.getParent(changed.value))
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