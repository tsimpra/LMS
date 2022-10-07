package gr.apt.lms.ui.menus

import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.flow.router.RouteConfiguration
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.MenuDto
import gr.apt.lms.metamodel.dto.MenuDto_
import gr.apt.lms.service.MenuService
import gr.apt.lms.ui.AutoCompletableSelect
import gr.apt.lms.ui.Editor
import gr.apt.lms.ui.Refreshable
import java.math.BigDecimal
import javax.inject.Inject

@UIScoped
class MenuEditor @Inject constructor(private val menuService: MenuService) : Editor<MenuDto>(menuService), Refreshable {

    private val id: TextField = TextField(MenuDto_.ID_HEADER)
    private val description: TextField = TextField(MenuDto_.DESCRIPTION_HEADER)
    private val path: AutoCompletableSelect<String> = AutoCompletableSelect(MenuDto_.PATH_HEADER, true)
    private val icon: AutoCompletableSelect<VaadinIcon> = AutoCompletableSelect(MenuDto_.ICON_HEADER)
    private val parentId: AutoCompletableSelect<MenuDto> = AutoCompletableSelect(MenuDto_.PARENT_ID_HEADER, true)
    private val displayOrder: NumberField = NumberField(MenuDto_.DISPLAY_ORDER_HEADER)
    override val binder: Binder<MenuDto> = Binder(MenuDto::class.java)

    override lateinit var refreshable: Refreshable

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        parentId.setItems(menuService.findAll(null, null))
        // set how we display the select list items
        parentId.setItemLabelGenerator {
            if (it != null) it.description else ""
        }

        icon.setItems(*VaadinIcon.values())

        path.setItems(RouteConfiguration.forSessionScope().availableRoutes.map { "/${it.template}" })

        //Bind form items
        binder.forField(id)
            .withNullRepresentation("")
            .withConverter(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(MenuDto::id, null)
        binder.forField(description)
            .bind(MenuDto::description) { menu, text -> menu.description = text }
        binder.forField(path)
            .bind(MenuDto::path) { menu, text -> menu.path = text }
        binder.forField(icon)
            .bind({
                if (it.icon != null && it.icon!!.isNotEmpty()) VaadinIcon.valueOf(it.icon!!) else null
            }) { menu, text -> menu.icon = text?.name }
        binder.forField(displayOrder)
            .bind({ menuDto: MenuDto ->
                if (menuDto.displayOrder != null) menuDto.displayOrder!!.toDouble() else 0.0
            }) { menu, text -> menu.displayOrder = BigDecimal.valueOf(text).toBigInteger() }
        binder.forField(parentId)
            .bind({ menuDto: MenuDto ->
                if (menuDto.parentId != null)
                    menuService.findById(menuDto.parentId!!)
                else
                    MenuDto()
            }) { menu, text -> menu.parentId = text?.run { this.id } }
        //not sure if we need this
        binder.bindInstanceFields(this)

        fillFormLayoutWithComponents(id, description, path, icon, displayOrder, parentId)
    }

    override fun MenuDto.isNewObject(): Boolean = this.id == null

    override fun refresh() {
        parentId.setItems(menuService.findAll(null, null))
    }
}