package gr.apt.lms.ui.persons

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.service.PersonService
import gr.apt.lms.ui.GridList
import gr.apt.lms.ui.MainLayout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Route(value = "/persons", layout = MainLayout::class)
class PersonList
@Inject constructor(personService: PersonService) : SplitLayout() {
    private var gridList: GridList<PersonDto>
    private var editor: PersonEditor

    init {
        this.className = "persons-list"
        editor = PersonEditor(personService)
        gridList = GridList(PersonDto::class.java, personService, editor)
        configureGrid()

        editor.isVisible = false
        editor.refreshable = gridList

        //create the page UI
        this.addToPrimary(gridList)
        this.addToSecondary(editor)
        this.setSizeFull()
    }

    /*grid configuration. Adds columns and calls rest configuration methods */
    fun configureGrid() {
        gridList.grid.addClassName("persons-grid")
        val stringComparator: (String?, String?) -> Int =
            { a: String?, b: String? -> if (a == null) -1 else if (b == null) 1 else a.compareTo(b) }
        gridList.grid.addColumn(PersonDto::fname).setHeader("First Name")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.fname, b.fname) }
        gridList.grid.addColumn(PersonDto::lname).setHeader("Last Name")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.lname, b.lname) }
        gridList.grid.addColumn(PersonDto::dateOfBirth).setHeader("Date of Birth")
        gridList.grid.addColumn(PersonDto::dateOfEmployment).setHeader("Date of Employment")
        gridList.grid.addColumn(PersonDto::email).setHeader("Email")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.email, b.email) }
        gridList.grid.addColumn(PersonDto::job).setHeader("Job")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.job?.name, b.job?.name) }
        gridList.grid.addColumn(PersonDto::enabled).setHeader("Enabled")
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.enabled?.name, b.enabled?.name) }
//        grid.addColumn(ValueProvider<Person, Any> { person: Person ->
//            val titles: List<Title> = person.getTitles()
//            var arr = "["
//            for (title in titles) {
//                arr += title.getName() + ","
//            }
//            if (arr.length > 1) arr.substring(0, arr.length - 1) + "]" else "-"
//        }).setHeader("Titles")
        gridList.grid.setItemDetailsRenderer(ComponentRenderer { person: PersonDto ->
            configureItemsDetails(
                person
            )
        })
    }

    //configuration for grid item details
    private fun configureItemsDetails(person: PersonDto): Div {
        val div = Div()
        div.className = "details-upload"
        div.setSizeFull()
        div.add(
            HorizontalLayout(
                Label("Username:${person.username}"),
                Label("Used Leaves:${person.usedLeaves.toString()}"),
                Label("Remaining Leaves:${person.remainingLeaves.toString()}")
            )
        )
        return div
    }
}




