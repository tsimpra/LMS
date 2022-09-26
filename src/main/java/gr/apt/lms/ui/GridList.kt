package gr.apt.lms.ui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import gr.apt.lms.service.CrudService

class GridList<T : Any>(
    private val clas: Class<T>,
    private val service: CrudService<T>,
    private val editor: Editor<T>
) : VerticalLayout(), Refreshable {

    internal val grid = Grid(clas, false)

    //Create the buttons for our UI
    internal val create = Button("Create", Icon(VaadinIcon.PLUS))

    val filters = mutableMapOf<String, (T) -> Boolean>()

    init {
        grid.asSingleSelect().addValueChangeListener {
            configureSingleSelect(it.value)
        }
        configureButtons()
        refresh()
        grid.setSizeFull()

        this.add(create, grid)
        this.setSizeFull()
    }

    //Buttons Configuration
    private fun configureButtons() {
        create.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        create.addClickListener {
            editor.enableForm(clas.getConstructor().newInstance())
        }
    }

    //configuration for single select on grid item
    private fun configureSingleSelect(value: T?) {
        if (value != null) {
            editor.enableForm(value, true)
        } else {
            editor.clearForm()
        }
    }

    //Refreshes the grid
    override fun refresh() {
        grid.select(null)
        val items = grid.setItems(service.findAll(null, null))
        filters.forEach {
            items.addFilter(it.value)
        }
        grid.dataProvider.refreshAll()
    }
}