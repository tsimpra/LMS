package gr.apt.lms.ui.views.persons

import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.metamodel.dto.PersonDto_
import gr.apt.lms.service.PersonService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.Editor
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonEditor @Inject constructor(
    personService: PersonService
    //refreshable: Refreshable
) : Editor<PersonDto>(personService) {

    private val id: TextField = TextField(PersonDto_.ID_HEADER)
    private val firstName: TextField = TextField(PersonDto_.FIRST_NAME_HEADER)
    private val lastName: TextField = TextField(PersonDto_.LAST_NAME_HEADER)
    private val username: TextField = TextField(PersonDto_.USERNAME_HEADER)
    private val dateOfEmployment: DatePicker = DatePicker(PersonDto_.DATE_OF_EMPLOYMENT_HEADER, LocalDate.now())
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
            .bind(PersonDto::firstName) { person, text -> person.firstName = text }
        binder.forField(lastName)
            .bind(PersonDto::lastName) { person, text -> person.lastName = text }
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
