package gr.apt.lms.ui.persons

import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.service.PersonService
import gr.apt.lms.ui.Editor
import gr.apt.lms.ui.Refreshable
import java.time.LocalDate
import javax.inject.Inject

@UIScoped
class PersonEditor @Inject constructor(
    personService: PersonService
    //refreshable: Refreshable
) : Editor<PersonDto>(personService) {

    private val id: TextField = TextField("Id")
    private val firstName: TextField = TextField("First Name")
    private val lastName: TextField = TextField("Last Name")
    private val username: TextField = TextField("Username")
    private val dateOfEmployment: DatePicker = DatePicker("Date Of Employment", LocalDate.now())
    override val binder: Binder<PersonDto> = Binder(PersonDto::class.java)
    override lateinit var refreshable: Refreshable

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        //Bind form items
        binder.forField(id)
            .withNullRepresentation("")
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

        fillFormLayoutWithComponents(id, firstName, lastName, username, dateOfEmployment)

    }

    override fun PersonDto.isNewObject() = this.id == null


}
