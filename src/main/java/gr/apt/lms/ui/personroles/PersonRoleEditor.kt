package gr.apt.lms.ui.personroles

import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.PersonRolesDto
import gr.apt.lms.dto.RoleDto
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.dto.person.fullname
import gr.apt.lms.metamodel.dto.PersonRolesDto_
import gr.apt.lms.service.PersonRolesService
import gr.apt.lms.service.PersonService
import gr.apt.lms.service.RoleService
import gr.apt.lms.ui.Editor
import gr.apt.lms.ui.Refreshable
import io.quarkus.arc.Arc
import java.math.BigInteger
import javax.inject.Inject

@UIScoped
class PersonRoleEditor @Inject constructor(personRolesService: PersonRolesService) :
    Editor<PersonRolesDto>(personRolesService), Refreshable {

    private val id: TextField = TextField(PersonRolesDto_.ID_HEADER)
    private val roleId: Select<RoleDto> = Select()
    private val personId: Select<PersonDto> = Select()
    override val binder: Binder<PersonRolesDto> = Binder(PersonRolesDto::class.java)
    override lateinit var refreshable: Refreshable
    private val personService: PersonService = Arc.container().instance(PersonService::class.java).get()
    private val roleService = Arc.container().instance(RoleService::class.java).get()

    var personIdFilter: BigInteger? = null
        set(value) {
            field = value
            if (value != null) {
                val person = personService.findById(value)
                personId.setItems(person)
                personId.value = person
            } else {
                personId.setItems(PersonDto())
            }
        }

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        roleId.label = PersonRolesDto_.ROLE_ID_HEADER
        roleId.setItems(roleService.findAll(null, null))
        roleId.setItemLabelGenerator {
            it.role
        }

        personId.label = PersonRolesDto_.PERSON_ID_HEADER
        //personId.setItems(personService.findAll(null, null))
        personId.setItemLabelGenerator {
            it.fullname
        }

        //Bind form items
        binder.forField(id)
            .withNullRepresentation("")
            .withConverter(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(PersonRolesDto::id, null)
        binder.forField(roleId)
            .bind({
                if (it.roleId != null) {
                    roleService.findById(it.roleId!!)
                } else
                    RoleDto()
            }) { personRolesDto, roleDto -> personRolesDto.roleId = roleDto.id }
        binder.forField(personId)
            .bind({
                if (it.personId != null) {
                    personService.findById(it.personId!!)
                } else
                    PersonDto()
            }) { personRolesDto, personDto -> personRolesDto.personId = personDto.id }


        fillFormLayoutWithComponents(id, roleId, personId)
    }

    override fun PersonRolesDto.isNewObject() = this.id == null

    override fun refresh() {
        roleId.setItems(roleService.findAll(null, null))
    }
}