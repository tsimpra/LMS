package gr.apt.lms.ui.views.menus

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.MenuDto
import gr.apt.lms.metamodel.dto.MenuDto_
import gr.apt.lms.service.MenuService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.GridList
import gr.apt.lms.ui.views.MainLayout
import gr.apt.lms.utils.stringComparator
import io.quarkus.arc.Arc
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.inject.Inject

@UIScoped
@Route(value = "/menus", layout = MainLayout::class)
@RolesAllowed("admin")
@PreserveOnRefresh
class MenuList
@Inject constructor(private val menuService: MenuService) : VerticalLayout(), Refreshable {
    private var gridList: GridList<MenuDto>
    private var editor: MenuEditor

    private var showTreeButton = Button("Show Menu Tree")

    init {
        this.className = "menu-list"
        editor = MenuEditor(menuService)
        gridList = GridList(MenuDto::class.java, menuService, editor)
        configureGrid()
        gridList.grid.recalculateColumnWidths()

        editor.isVisible = false
        editor.refreshable = this

        //showTreeButton config
        showTreeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        showTreeButton.icon = Icon(VaadinIcon.TREE_TABLE)
        showTreeButton.addClickListener {
            val treeDialog = Dialog()
            treeDialog.add(MenuTree())
            treeDialog.open()
        }

        //create the page UI
        this.add(gridList, showTreeButton)
        this.setHorizontalComponentAlignment(FlexComponent.Alignment.END, showTreeButton)
        //this.addToSecondary(editor)
        this.setSizeFull()
    }

    private fun configureGrid() {
        gridList.grid.addClassName("grid-list")
        gridList.grid.addColumn(MenuDto::description).setHeader(MenuDto_.DESCRIPTION_HEADER)
            .setComparator { a: MenuDto, b: MenuDto -> stringComparator(a.description, b.description) }
        gridList.grid.addColumn(MenuDto::path).setHeader(MenuDto_.PATH_HEADER)
            .setComparator { a: MenuDto, b: MenuDto -> stringComparator(a.path, b.path) }
        gridList.grid.addColumn(MenuDto::icon).setHeader(MenuDto_.ICON_HEADER)
            .setComparator { a: MenuDto, b: MenuDto -> stringComparator(a.icon, b.icon) }
        gridList.grid.addColumn(MenuDto::displayOrder).setHeader(MenuDto_.DISPLAY_ORDER_HEADER)
            .setComparator { a: MenuDto, b: MenuDto -> a.displayOrder?.compareTo(b.displayOrder) ?: 0 }
        gridList.grid.addColumn({
            if (it.parentId != null) menuService.findById(it.parentId!!).description!! else ""
        }).setHeader(MenuDto_.PARENT_ID)
            .setComparator { a: MenuDto, b: MenuDto -> a.parentId?.compareTo(b.parentId) ?: 0 }

    }

    //added permit all annotation cause unauthorized action was thrown from quarkus security
    @PermitAll
    override fun refresh() {
        gridList.refresh()
        editor.refresh()
        Arc.container().instance(MainLayout::class.java).get().createDrawer()
    }

}