package gr.apt.lms.ui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.PersonService
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
@Route(value = "/persons", layout = MainLayout::class)
class PersonList @Inject constructor(private val personService: PersonService) : VerticalLayout() {
    private val grid = Grid(PersonDto::class.java, false)
    private val editor = PersonEditor()


    //Create the buttons for our UI
    private val save = Button("Save", Icon(VaadinIcon.CHECK))
    private val create = Button("Create", Icon(VaadinIcon.PLUS))
    private val cancel = Button("Cancel", Icon(VaadinIcon.CLOSE))
    private val delete = Button("Delete", Icon(VaadinIcon.MINUS))

    private var selected: PersonDto? = null

    init {
        this.className = "persons-list"

        configureGrid()
        configureButtons()
        updateList()

        //create the page UI
        val splitLayout = SplitLayout()
        val leftSide = VerticalLayout()
        val rightSide = VerticalLayout()
        leftSide.add(groupButtons(create, delete), grid)
        rightSide.add(editor, groupButtons(save, cancel))
        rightSide.width = "35%"
        splitLayout.addToPrimary(leftSide)
        splitLayout.addToSecondary(rightSide)
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
            if (it.getValue() != null) {
                //save.setEnabled(true)
                //delete.setEnabled(true)
                val result: PersonDto =
                    personService.findById(it.getValue().id ?: throw LmsException("id cannot be null"))
                populateForm(result)
            } else {
                clearForm()
            }
        }
        grid.setItemDetailsRenderer(ComponentRenderer { person: PersonDto ->
            configureItemsDetails(
                person
            )
        })
    }

    //Buttons Configuration
    private fun configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        save.setEnabled(false)
        save.addClickListener {
            try {
                if (selected == null) {
                    selected = PersonDto()
                }
                editor.binder.writeBean(selected)
                personService.update(selected ?: throw LmsException("cannot update with empty body"))
                clearForm()
                updateList()
                Notification.show("Person details stored.")
            } catch (validationException: ValidationException) {
                Notification.show("An exception happened while trying to store the Person's details.")
            }
        }

        create.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        create.addClickListener {
            try {
                selected = PersonDto()
                editor.binder.writeBean(selected)
                if (selected != null) {
                    personService.create(selected ?: throw LmsException("cannot create with empty body"))
                    clearForm()
                    updateList()
                    Notification.show("Person created successfully.")
                } else {
                    Notification.show("Create failed.Form is Empty")
                }
            } catch (e: ValidationException) {
                e.printStackTrace()
            }
        }

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClassName("delete-button")
        delete.isEnabled = false
        delete.addClickListener {
            if (selected != null) {
                personService.delete(selected ?: throw LmsException("cannot delete with empty body"))
                clearForm()
                updateList()
                Notification.show("Customer deleted successfully.")
            }
        }

        cancel.addClickListener {
            clearForm()
            updateList()
        }
    }


    //configuration for single select on grid item
    private fun configureSingleSelect(person: PersonDto?) {
        if (grid.selectedItems.size > 0) {
        } else {
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

    private fun groupButtons(b1: Button, b2: Button): HorizontalLayout {
        val group = HorizontalLayout()
        group.setWidthFull()
        group.isSpacing = true
        group.add(b1, b2)
        return group
    }

    //Refreshes the grid
    private fun updateList() {
        grid.select(null)
        grid.setItems(personService.findAll(null, null))
        grid.dataProvider.refreshAll()
    }

    private fun clearForm() {
        populateForm(null)
        save.setEnabled(false)
        delete.setEnabled(false)
    }

    private fun populateForm(value: PersonDto?) {
        selected = value
        editor.binder.readBean(selected)
    }
}




