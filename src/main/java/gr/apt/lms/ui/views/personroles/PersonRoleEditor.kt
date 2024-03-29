package gr.apt.lms.ui.views.personroles

import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.PersonRolesDto
import gr.apt.lms.dto.RoleDto
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.dto.person.fullname
import gr.apt.lms.metamodel.dto.PersonRolesDto_
import gr.apt.lms.repository.PersonRolesRepository
import gr.apt.lms.service.PersonRolesService
import gr.apt.lms.service.PersonService
import gr.apt.lms.service.RoleService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.AutoCompletableSelect
import gr.apt.lms.ui.components.Editor
import io.quarkus.arc.Arc
import java.math.BigInteger
import javax.inject.Inject
import kotlin.streams.toList

@UIScoped
class PersonRoleEditor @Inject constructor(personRolesService: PersonRolesService) :
    Editor<PersonRolesDto>(personRolesService), Refreshable {

    private val id: TextField = TextField(PersonRolesDto_.ID_HEADER)
    private val roleId: AutoCompletableSelect<RoleDto> = AutoCompletableSelect(PersonRolesDto_.ROLE_ID_HEADER)
    private val personId: AutoCompletableSelect<PersonDto> = AutoCompletableSelect(PersonRolesDto_.PERSON_ID_HEADER)

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
            } else {
                personId.setItems(PersonDto())
            }
        }

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        roleId.setItems(roleService.findAll(null, null))
        roleId.setItemLabelGenerator {
            it.role
        }

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

    override fun computeEditorComponentsData(value: PersonRolesDto) {
        if (personIdFilter != null) {
            val personRolesRepository = Arc.container().instance(PersonRolesRepository::class.java).get()
            val roleIdsByPersonId = personRolesRepository.getRoleIdsByPersonId(personIdFilter!!)
            roleId.setItems(roleId.listDataView.items
                .filter {
                    if (value.roleId == it.id) true else
                        !roleIdsByPersonId.contains(it.id)
                }.toList()
            )
        }
    }

    override fun PersonRolesDto.isNewObject() = this.id == null

    override fun refresh() {
        roleId.setItems(roleService.findAll(null, null))
    }
}