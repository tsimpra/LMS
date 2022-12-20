package gr.apt.lms.ui.views.personroles

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.PersonRolesDto
import gr.apt.lms.metamodel.dto.PersonRolesDto_
import gr.apt.lms.service.PersonRolesService
import gr.apt.lms.service.RoleService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.GridList
import io.quarkus.arc.Arc
import io.quarkus.arc.Unremovable
import java.math.BigInteger
import javax.inject.Inject

@Unremovable
@UIScoped
class PersonRolesList @Inject constructor(personRolesService: PersonRolesService) : VerticalLayout(), Refreshable {
    private var gridList: GridList<PersonRolesDto>
    private var editor: PersonRoleEditor

    var personIdFilter: BigInteger? = null
        set(value) {
            field = value
            if (value != null) {
                gridList.filters["personId"] = { entity: PersonRolesDto ->
                    entity.personId == value
                }
                editor.personIdFilter = personIdFilter
            }
            gridList.refresh()
        }

    private val roleService = Arc.container().instance(RoleService::class.java).get()

    init {
        this.className = "leaves-list"
        editor = PersonRoleEditor(personRolesService)
        gridList = GridList(PersonRolesDto::class.java, personRolesService, editor)
        configureGrid()
        gridList.filters["personId"] = { entity: PersonRolesDto ->
            if (personIdFilter != null) entity.personId == personIdFilter else true
        }
        gridList.grid.recalculateColumnWidths()

        editor.isVisible = false
        editor.refreshable = this

        //create the page UI
        this.add(gridList)
        //this.addToSecondary(editor)
        this.setSizeFull()
    }

    private fun configureGrid() {
        gridList.grid.addClassName("grid-list")
        gridList.grid.addColumn({
            if (it.roleId != null) roleService.findById(it.roleId!!).role ?: "" else ""
        }).setHeader(PersonRolesDto_.ROLE_ID_HEADER)
            .setComparator { a: PersonRolesDto, b: PersonRolesDto -> a.roleId?.compareTo(b.roleId) ?: 0 }
//        gridList.grid.addColumn(PersonRolesDto::personId).setHeader(PersonRolesDto_.PERSON_ID_HEADER)
//            .setComparator { a: PersonRolesDto, b: PersonRolesDto -> a.personId?.compareTo(b.personId) ?:0 }
    }

    override fun refresh() {
        gridList.refresh()
        editor.refresh()
    }

}