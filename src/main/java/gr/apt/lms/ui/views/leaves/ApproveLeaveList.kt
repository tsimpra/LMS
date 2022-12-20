package gr.apt.lms.ui.views.leaves

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.leave.ApproveLeaveDto
import gr.apt.lms.dto.leave.LeaveDto
import gr.apt.lms.dto.person.fullname
import gr.apt.lms.metamodel.dto.LeaveDto_
import gr.apt.lms.persistence.enumeration.YesOrNo
import gr.apt.lms.service.LeaveService
import gr.apt.lms.service.PersonService
import gr.apt.lms.ui.components.ApproveRejectButtonsComponent
import gr.apt.lms.ui.views.MainLayout
import gr.apt.lms.utils.stringComparator
import io.quarkus.arc.Arc
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed

@Route(value = "/approve_leave", layout = MainLayout::class)
@UIScoped
@RolesAllowed("admin")
@PreserveOnRefresh
class ApproveLeaveList : Grid<LeaveDto>() {

    private val leaveService = Arc.container().instance(LeaveService::class.java).get()
    private val personService = Arc.container().instance(PersonService::class.java).get()

    init {
        this.className = "approve-leaves-grid"

        this.addAttachListener {
            refresh()
        }

        refresh()

        configureGridColumns()
        this.recalculateColumnWidths()
        this.setSizeFull()
    }

    private fun configureGridColumns() {
        this.addColumn(LeaveDto::description).setHeader(LeaveDto_.DESCRIPTION_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto -> stringComparator(a.description, b.description) }
        this.addColumn(LeaveDto::numberOfRequestedLeaves)
            .setHeader(LeaveDto_.NUMBER_OF_REQUESTED_LEAVES_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto ->
                a.numberOfRequestedLeaves?.compareTo(b.numberOfRequestedLeaves ?: 0) ?: 0
            }
        this.addColumn(LeaveDto::type).setHeader(LeaveDto_.TYPE_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto -> stringComparator(a.type?.name, b.type?.name) }
        this.addColumn(LeaveDto::startDate).setHeader(LeaveDto_.START_DATE_HEADER)
        this.addColumn(LeaveDto::endDate).setHeader(LeaveDto_.END_DATE_HEADER)
        this.addColumn({ entity ->
            if (entity.personId != null) personService.findById(entity.personId!!).fullname else ""
        }).setHeader("Person")
            .setComparator { a: LeaveDto, b: LeaveDto ->
                stringComparator(
                    personService.findById(a.personId!!).fullname,
                    personService.findById(b.personId!!).fullname
                )
            }
        this.addComponentColumn { leave ->
            ApproveRejectButtonsComponent(
                {
                    leaveService.approve(ApproveLeaveDto(leave.id!!, YesOrNo.YES))
                    refresh()
                },
                {
                    leaveService.approve(ApproveLeaveDto(leave.id!!, YesOrNo.NO))
                    refresh()
                }
            )
        }
    }

    @PermitAll
    fun refresh() {
        this.setItems(leaveService.findAllPending(null, null))
    }
}