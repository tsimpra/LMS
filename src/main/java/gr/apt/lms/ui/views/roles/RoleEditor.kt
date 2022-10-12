package gr.apt.lms.ui.views.roles

import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.RoleDto
import gr.apt.lms.metamodel.dto.RoleDto_
import gr.apt.lms.service.RoleService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.Editor
import javax.inject.Inject

@UIScoped
class RoleEditor @Inject constructor(roleService: RoleService) : Editor<RoleDto>(roleService) {

    private val id: TextField = TextField(RoleDto_.ID_HEADER)
    private val role: TextField = TextField(RoleDto_.ROLE_HEADER)
    override val binder: Binder<RoleDto> = Binder(RoleDto::class.java)
    override lateinit var refreshable: Refreshable

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        //Bind form items
        binder.forField(id)
            .withNullRepresentation("")
            .withConverter(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(RoleDto::id, null)
        binder.forField(role)
            .bind(RoleDto::role) { roleDto, text -> roleDto.role = text }


        fillFormLayoutWithComponents(id, role)
    }

    override fun RoleDto.isNewObject() = this.id == null
}