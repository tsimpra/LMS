package gr.apt.lms.ui

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.person.PersonDto
import java.math.BigInteger

@UIScoped
class PersonEditor : Div() {
    private val id: TextField
    private val firstName: TextField
    private val lastName: TextField
    private val username: TextField
    val binder: Binder<PersonDto> = Binder(PersonDto::class.java)

    init {
        id = TextField("Id")
        id.themeName = "custom-text-field-label"
        firstName = TextField("First Name")
        lastName = TextField("Last Name")
        username = TextField("Username")
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
            .withConverter<BigInteger>(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(PersonDto::id, null)
        binder.forField(firstName)
            .bind(PersonDto::fname) { person, text -> person.fname = text }
        binder.forField(lastName)
            .bind(PersonDto::lname) { person, text -> person.lname = text }
        binder.forField(username)
            .bind(PersonDto::username) { person, text -> person.username = text }
        //not sure if we need this
        binder.bindInstanceFields(this)
        formLayout.add(id, firstName, lastName, username)
        add(formLayout)
    }
}