package gr.apt.lms.ui.roles

import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.RoleDto
import gr.apt.lms.metamodel.dto.RoleDto_
import gr.apt.lms.service.RoleService
import gr.apt.lms.ui.GridList
import gr.apt.lms.ui.MainLayout
import gr.apt.lms.utils.stringComparator
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
    }

}