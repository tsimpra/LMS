package gr.apt.lms.ui.views.restholidays

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.quarkus.annotation.VaadinSessionScoped
import gr.apt.lms.dto.RestHolidaysDto
import gr.apt.lms.metamodel.dto.RestHolidaysDto_
import gr.apt.lms.service.RestHolidaysService
import gr.apt.lms.ui.components.GridList
import gr.apt.lms.ui.views.MainLayout
import gr.apt.lms.utils.stringComparator
import javax.annotation.security.RolesAllowed
import javax.inject.Inject

@VaadinSessionScoped
@Route(value = "/rest/holidays", layout = MainLayout::class)
@RolesAllowed("admin")
@PreserveOnRefresh
class RestHolidaysList
@Inject constructor(restHolidaysService: RestHolidaysService) : VerticalLayout() {
    private var gridList: GridList<RestHolidaysDto>
    private var editor: RestHolidaysEditor

    init {
        this.className = "rest-holidays-list"
        editor = RestHolidaysEditor(restHolidaysService)
        gridList = GridList(RestHolidaysDto::class.java, restHolidaysService, editor)
        configureGrid()
        gridList.grid.recalculateColumnWidths()

        editor.isVisible = false
        editor.refreshable = gridList

        //create the page UI
        this.add(gridList)
        this.setSizeFull()
    }

    private fun configureGrid() {
        gridList.grid.addClassName("grid-list")
        gridList.grid.addColumn(RestHolidaysDto::description).setHeader(RestHolidaysDto_.DESCRIPTION_HEADER)
            .setComparator { a: RestHolidaysDto, b: RestHolidaysDto -> stringComparator(a.description, b.description) }
        gridList.grid.addColumn(RestHolidaysDto::startDate).setHeader(RestHolidaysDto_.START_DATE_HEADER)
        gridList.grid.addColumn(RestHolidaysDto::endDate).setHeader(RestHolidaysDto_.END_DATE_HEADER)
    }

}