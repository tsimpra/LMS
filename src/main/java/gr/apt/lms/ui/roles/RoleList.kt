package gr.apt.lms.ui.roles

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.RoleDto
import gr.apt.lms.metamodel.dto.RoleDto_
import gr.apt.lms.persistence.entity.Menu
import gr.apt.lms.repository.MenuRolesRepository
import gr.apt.lms.service.RoleService
import gr.apt.lms.ui.GridList
import gr.apt.lms.ui.MainLayout
import gr.apt.lms.ui.menus.MenuTree
import gr.apt.lms.utils.stringComparator
import io.quarkus.arc.Arc
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Route(value = "/roles", layout = MainLayout::class)
class RoleList
@Inject constructor(roleService: RoleService) : SplitLayout() {
    private var gridList: GridList<RoleDto>
    private var editor: RoleEditor

    init {
        this.className = "rest-holidays-list"
        editor = RoleEditor(roleService)
        gridList = GridList(RoleDto::class.java, roleService, editor)
        configureGrid()

        editor.isVisible = false
        editor.refreshable = gridList

        //create the page UI
        this.addToPrimary(gridList)
        this.addToSecondary(editor)
        this.setSizeFull()
    }

    private fun configureGrid() {
        gridList.grid.addClassName("grid-list")
        gridList.grid.addColumn(RoleDto::role).setHeader(RoleDto_.ROLE_HEADER)
            .setComparator { a: RoleDto, b: RoleDto -> stringComparator(a.role, b.role) }
        gridList.grid.addComponentColumn { MenuButton(it.id) }.setHeader("Menu")
    }

}

class MenuButton(val roleId: BigInteger?) : Button() {

    private val menuTree = MenuTree()
    private val menuRolesRepository: MenuRolesRepository
    private var menuIds: Collection<BigInteger>

    init {
        this.icon = Icon(VaadinIcon.PENCIL)
        menuRolesRepository = Arc.container().instance(MenuRolesRepository::class.java).get()
        menuIds = if (roleId != null) menuRolesRepository.getMenuListByRoleId(roleId) else emptyList();
        this.addClickListener {
            val treeDialog = Dialog()
            calculateCheckboxColumn()
            treeDialog.add(menuTree)
            treeDialog.width = "40%"
            treeDialog.height = "60%"
            treeDialog.open()
        }
    }

    fun calculateCheckboxColumn() {
        if (menuTree.getColumnByKey("checkboxes") != null) menuTree.removeColumnByKey("checkboxes")
        menuIds = if (roleId != null) menuRolesRepository.getMenuListByRoleId(roleId) else emptyList();
        menuTree.addComponentColumn {
            if (menuIds.contains(it.id)) MenuCheckBox(it, roleId, true) else MenuCheckBox(it, roleId)
        }
            .setHeader("Selected")
            .key = "checkboxes"
    }
}

class MenuCheckBox(menu: Menu, roleId: BigInteger?, var selected: Boolean = false) : Checkbox(selected) {

    init {
        val menuRolesRepository = Arc.container().instance(MenuRolesRepository::class.java).get()
        this.addValueChangeListener {
            selected = this.value
            if (menu.id != null && roleId != null) {
                if (value) menuRolesRepository.create(menu.id!!, roleId)
                else menuRolesRepository.delete(menu.id!!, roleId)
            }
        }
    }
}