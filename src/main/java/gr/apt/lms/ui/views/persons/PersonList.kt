package gr.apt.lms.ui.views.persons

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.metamodel.dto.PersonDto_
import gr.apt.lms.service.PersonRolesService
import gr.apt.lms.service.PersonService
import gr.apt.lms.ui.components.GridList
import gr.apt.lms.ui.views.MainLayout
import gr.apt.lms.ui.views.personroles.PersonRolesList
import gr.apt.lms.utils.stringComparator
import io.quarkus.arc.Arc
import javax.annotation.security.RolesAllowed
import javax.inject.Inject

@UIScoped
@Route(value = "/persons", layout = MainLayout::class)
@RolesAllowed("admin")
@PreserveOnRefresh
class PersonList
@Inject constructor(personService: PersonService) : VerticalLayout() {
    private var gridList: GridList<PersonDto>
    private var editor: PersonEditor

    init {
        this.className = "persons-list"
        editor = PersonEditor(personService)
        gridList = GridList(PersonDto::class.java, personService, editor)
        configureGrid()
        gridList.grid.recalculateColumnWidths()

        editor.isVisible = false
        editor.refreshable = gridList

        //create the page UI
        this.add(gridList)
        //this.addToSecondary(editor)
        this.setSizeFull()

    }

    /*grid configuration. Adds columns and calls rest configuration methods */
    private fun configureGrid() {
        gridList.grid.addClassName("grid-list")
        gridList.grid.addColumn(PersonDto::firstName).setHeader(PersonDto_.FIRST_NAME_HEADER)
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.firstName, b.firstName) }
        gridList.grid.addColumn(PersonDto::lastName).setHeader(PersonDto_.LAST_NAME_HEADER)
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.lastName, b.lastName) }
        gridList.grid.addColumn(PersonDto::dateOfBirth).setHeader(PersonDto_.DATE_OF_BIRTH_HEADER)
        gridList.grid.addColumn(PersonDto::dateOfEmployment).setHeader(PersonDto_.DATE_OF_EMPLOYMENT_HEADER)
        gridList.grid.addColumn(PersonDto::email).setHeader(PersonDto_.EMAIL_HEADER)
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.email, b.email) }
        gridList.grid.addColumn(PersonDto::job).setHeader(PersonDto_.JOB_HEADER)
            .setComparator { a: PersonDto, b: PersonDto -> stringComparator(a.job?.name, b.job?.name) }
        gridList.grid.addColumn(PersonDto::enabled).setHeader(PersonDto_.ENABLED_HEADER)
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
        gridList.grid.addComponentColumn { entity ->
            val editButton = Button("Edit")
            editButton.addClickListener {
                editDialog(entity)
            }
            editButton
        }.setHeader(PersonDto_.ROLES_HEADER)
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

    private fun editDialog(entity: PersonDto) {
        val dialog = Dialog()

        val personRolesGrid = PersonRolesList(Arc.container().instance(PersonRolesService::class.java).get())
        personRolesGrid.personIdFilter = entity.id
        dialog.add(personRolesGrid)
        dialog.setHeight(60.0f, Unit.PERCENTAGE)
        dialog.setWidth(40.0f, Unit.PERCENTAGE)
        dialog.open()
    }
}




