package gr.apt.lms.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.PersonService
import java.time.LocalDate

@UIScoped
class PersonEditor() : VerticalLayout() {

    private lateinit var personService: PersonService
    private lateinit var dataHolder: Refreshable


    private val id: TextField
    private val firstName: TextField
    private val lastName: TextField
    private val username: TextField
    private val dateOfEmployment: DatePicker
    val binder: Binder<PersonDto> = Binder(PersonDto::class.java)

    //Create the buttons for our UI
    internal val save = Button("Save", Icon(VaadinIcon.CHECK))
    internal val cancel = Button("Cancel", Icon(VaadinIcon.CLOSE))
    internal val delete = Button("Delete", Icon(VaadinIcon.MINUS))

    private var selected: PersonDto? = null

    constructor(personService: PersonService, dataHolder: Refreshable) : this() {
        this.personService = personService
        this.dataHolder = dataHolder
    }

    init {
        id = TextField("Id")
        id.themeName = "custom-text-field-label"
        firstName = TextField("First Name")
        lastName = TextField("Last Name")
        username = TextField("Username")
        dateOfEmployment = DatePicker("Date Of Employment", LocalDate.now())
        val formLayout = FormLayout()
        formLayout.setResponsiveSteps(
            FormLayout.ResponsiveStep("1px", 1),
            FormLayout.ResponsiveStep("200px", 2),
            FormLayout.ResponsiveStep("700px", 3)
        )

        //Setting the read only fields
        id.isReadOnly = true

        //Bind form items
        binder.forField(id)
            .withConverter(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(PersonDto::id, null)
        binder.forField(firstName)
            .bind(PersonDto::fname) { person, text -> person.fname = text }
        binder.forField(lastName)
            .bind(PersonDto::lname) { person, text -> person.lname = text }
        binder.forField(username)
            .bind(PersonDto::username) { person, text -> person.username = text }
        binder.forField(dateOfEmployment)
            .bind(PersonDto::dateOfEmployment) { person, date -> person.dateOfEmployment = date }
        //not sure if we need this
        binder.bindInstanceFields(this)
        formLayout.add(id, firstName, lastName, dateOfEmployment, username)

        configureButtons()

        add(formLayout, groupButtons(save, delete, cancel))
        this.width = "35%"

    }

    fun configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        save.isEnabled = false
        save.addClickListener {
            try {
                if (selected == null) {
                    selected = PersonDto()
                }
                this.binder.writeBean(selected)
                if (selected!!.id == null) {
                    personService.create(selected ?: throw LmsException("cannot update with empty body"))
                    Notification.show("Person created successfully.")
                } else {
                    personService.update(selected ?: throw LmsException("cannot update with empty body"))
                    Notification.show("Person details stored.")
                }
                clearForm()
                dataHolder.refresh()
            } catch (validationException: ValidationException) {
                Notification.show("An exception happened while trying to store the Person's details.")
            }
        }

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        delete.addClassName("delete-button")
        delete.isEnabled = false
        delete.addClickListener {
            if (selected != null) {
                personService.delete(selected ?: throw LmsException("cannot delete with empty body"))
                clearForm()
                dataHolder.refresh()
                Notification.show("Person deleted successfully.")
            }
        }

        cancel.addClickListener {
            clearForm()
            dataHolder.refresh()
        }
    }

    internal fun clearForm() {
        populateForm(null)
        save.isEnabled = false
        delete.isEnabled = false
        this.isVisible = false
    }

    internal fun populateForm(value: PersonDto?) {
        selected = value
        this.binder.readBean(selected)
        if (value == null) dateOfEmployment.value = LocalDate.now()
    }

    private fun groupButtons(vararg b1: Component): HorizontalLayout {
        val group = HorizontalLayout()
        group.setWidthFull()
        group.isSpacing = true
        group.add(*b1)
        return group
    }

}
