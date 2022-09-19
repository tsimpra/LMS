package gr.apt.lms.ui.leaves

import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.leave.LeaveDto
import gr.apt.lms.metamodel.dto.LeaveDto_
import gr.apt.lms.service.LeaveService
import gr.apt.lms.ui.GridList
import gr.apt.lms.ui.MainLayout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Route(value = "/leaves", layout = MainLayout::class)
class LeaveList
@Inject constructor(leaveService: LeaveService) : SplitLayout() {
    private var gridList: GridList<LeaveDto>
    private var editor: LeaveEditor

    init {
        this.className = "persons-list"
        editor = LeaveEditor(leaveService)
        gridList = GridList(LeaveDto::class.java, leaveService, editor)
        configureGrid()

        editor.isVisible = false
        editor.refreshable = gridList

        //create the page UI
        this.addToPrimary(gridList)
        this.addToSecondary(editor)
        this.setSizeFull()
    }

    private fun configureGrid() {
        gridList.grid.addClassName("leaves-grid")
        val stringComparator: (String?, String?) -> Int =
            { a: String?, b: String? -> if (a == null) -1 else if (b == null) 1 else a.compareTo(b) }
        gridList.grid.addColumn(LeaveDto::description).setHeader(LeaveDto_.DESCRIPTION_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto -> stringComparator(a.description, b.description) }
        gridList.grid.addColumn(LeaveDto::type).setHeader(LeaveDto_.TYPE_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto -> stringComparator(a.type?.name, b.type?.name) }
        gridList.grid.addColumn(LeaveDto::startDate).setHeader(LeaveDto_.START_DATE_HEADER)
        gridList.grid.addColumn(LeaveDto::endDate).setHeader(LeaveDto_.END_DATE_HEADER)
        gridList.grid.addColumn(LeaveDto::approved).setHeader(LeaveDto_.APPROVED_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto -> stringComparator(a.approved?.name, b.approved?.name) }
        gridList.grid.addColumn(LeaveDto::approvedBy).setHeader(LeaveDto_.APPROVED_BY_HEADER)
            .setComparator { a: LeaveDto, b: LeaveDto ->
                stringComparator(
                    a.approvedBy?.lastName,
                    b.approvedBy?.lastName
                )
            }
        gridList.grid.addColumn(LeaveDto::approvedDate).setHeader(LeaveDto_.APPROVED_DATE_HEADER)
    }

}