package gr.apt.lms.ui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.service.PersonService
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
@Route(value = "/persons", layout = MainLayout::class)
class PersonList @Inject constructor(private val personService: PersonService) : VerticalLayout(), Refreshable {
    private val grid = Grid(PersonDto::class.java, false)
    private var editor: PersonEditor


    //Create the buttons for our UI
    private val create = Button("Create", Icon(VaadinIcon.PLUS))

    init {
        this.className = "persons-list"
        editor = PersonEditor(personService, this)

        configureGrid()
        configureButtons()
        refresh()
        editor.isVisible = false

        //create the page UI
        val splitLayout = SplitLayout()
        val leftSide = VerticalLayout()
        leftSide.add(create, grid)
        splitLayout.addToPrimary(leftSide)
        splitLayout.addToSecondary(editor)
        splitLayout.setSizeFull()
        add(splitLayout)
    }

    /*grid configuration. Adds columns and calls rest configuration methods */
    private fun configureGrid() {
        grid.addClassName("persons-grid")
        val stringComparator: (String?, String?) -> Int =
            { a: String?, b: String? -> if (a == null) -1 else if (b == null) 1 else a.compareTo(b) }
        grid.addColumn(PersonDto::fname).setHeader("First Name")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.fname, b.fname) }
        grid.addColumn(PersonDto::lname).setHeader("Last Name")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.lname, b.lname) }
        grid.addColumn(PersonDto::dateOfBirth).setHeader("Date of Birth")
        grid.addColumn(PersonDto::dateOfEmployment).setHeader("Date of Employment")
        grid.addColumn(PersonDto::email).setHeader("Email")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.email, b.email) }
        grid.addColumn(PersonDto::job).setHeader("Job")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.job?.name, b.job?.name) }
        grid.addColumn(PersonDto::enabled).setHeader("Enabled")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.enabled?.name, b.enabled?.name) }
//        grid.addColumn(ValueProvider<Person, Any> { person: Person ->
//            val titles: List<Title> = person.getTitles()
//            var arr = "["
//            for (title in titles) {
//                arr += title.getName() + ","
//            }
//            if (arr.length > 1) arr.substring(0, arr.length - 1) + "]" else "-"
//        }).setHeader("Titles")
        grid.asSingleSelect().addValueChangeListener {
            configureSingleSelect(it.value)
        }
        grid.setItemDetailsRenderer(ComponentRenderer { person: PersonDto ->
            configureItemsDetails(
                person
            )
        })
    }

    //Buttons Configuration
    private fun configureButtons() {
        create.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        create.addClickListener {
            editor.save.isEnabled = true
            editor.isVisible = true
        }
    }


    //configuration for single select on grid item
    private fun configureSingleSelect(person: PersonDto?) {
        if (person != null) {
            editor.isVisible = true
            editor.save.isEnabled = true
            editor.delete.isEnabled = true
            editor.populateForm(person)
        } else {
            editor.clearForm()
        }
    }

    //configuration for grid item details
    private fun configureItemsDetails(person: PersonDto): Div {
        val div = Div()
        div.className = "details-upload"
        div.setSizeFull()
        div.add(Label(person.username))
        return div
    }

    //Refreshes the grid
    override fun refresh() {
        grid.select(null)
        grid.setItems(personService.findAll(null, null))
        grid.dataProvider.refreshAll()
    }
}




