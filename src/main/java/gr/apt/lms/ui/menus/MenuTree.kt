package gr.apt.lms.ui.menus

import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.metamodel.entity.Menu_
import gr.apt.lms.persistence.entity.Menu
import gr.apt.lms.repository.MenuRepository
import io.quarkus.arc.Arc

@UIScoped
class MenuTree : TreeGrid<Menu>() {

    init {
        val menuRepository = Arc.container().instance(MenuRepository::class.java).get()
        val parents = menuRepository.find("${Menu_.PARENT_ID} is null").list<Menu>()
        val children = { parent: Menu -> menuRepository.find("${Menu_.PARENT_ID} = ?1", parent.id).list<Menu>() }
        this.setItems(parents, children)
        this.addHierarchyColumn(Menu::description).setHeader(Menu_.DESCRIPTION_HEADER)
        this.expandRecursively(parents, 5)
    }
}