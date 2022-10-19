package gr.apt.lms.ui.components

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.router.RouteConfiguration
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.config.security.LmsPrincipal
import gr.apt.lms.exception.LmsException
import gr.apt.lms.persistence.entity.Menu
import gr.apt.lms.repository.MenuRepository
import io.quarkus.arc.Arc

@UIScoped
class MenuTabTree : TreeGrid<MenuTab>() {

    private var selectedNode: MenuTab? = null

    private val principal: LmsPrincipal
        get() = VaadinServletRequest.getCurrent().userPrincipal as? LmsPrincipal
            ?: throw LmsException("user is not authenticated")

    init {
        //configureTreeGrid()
        //for when selecting the label instead of the grid
        this.addItemClickListener { this.select(it.item) }
        this.addDetachListener {
            selectedNode = null
            this.removeAllColumns()
            this.treeData.clear()
        }
        this.addAttachListener { configureTreeGrid() }
        this.setHeightFull()
    }

    private fun configureTreeGrid() {
        setTreeData()
        //expand tree if selected node exists
        if (selectedNode != null) this.expand(selectedNode)
        //for grid selection handle navigation and expand/collapse of menu tree
        this.asSingleSelect().addValueChangeListener { changed ->
            if (changed.value != null) {
                selectedNode = changed.value
                val href = changed.value.router.href
                if (href.isNotEmpty()) {
                    this.select(changed.value)
                    UI.getCurrent().navigate(href)
                } else
                    if (this.isExpanded(changed.value))
                        this.collapse(changed.value)
                    else
                        this.expand(changed.value)
            } else {
                selectedNode = null
                this.collapse(this.treeData.rootItems)
            }
        }
        this.addComponentHierarchyColumn { it }
    }

    private fun fetchUserMenu(): List<Menu> {
        val menuRepository = Arc.container().instance(MenuRepository::class.java).get()
        return menuRepository.getUserMenuByRoleId(principal.selectedRole)
    }

    private fun setTreeData() {
        //fetch user's menu
        val list: List<Menu> = fetchUserMenu()
        val routerList = list.map {
            val route = RouteConfiguration.forSessionScope().getRoute(it.path)
            MenuTab(it, RouterLink("", route.get()))
        }
        //create menu tree
        val parents = routerList.filter { it.ref == null }
        val children = { parent: MenuTab -> routerList.filter { it.ref == parent.id } }
        this.setItems(parents, children)
    }
}