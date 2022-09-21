package gr.apt.lms.ui.restholidays

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import gr.apt.lms.dto.RestHolidaysDto
import gr.apt.lms.metamodel.dto.RestHolidaysDto_
import gr.apt.lms.service.RestHolidaysService
import gr.apt.lms.ui.GridList
import gr.apt.lms.ui.MainLayout
import gr.apt.lms.utils.stringComparator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Route(value = "/rest/holidays", layout = MainLayout::class)
class RestHolidaysList
@Inject constructor(restHolidaysService: RestHolidaysService) : VerticalLayout() {
    private var gridList: GridList<RestHolidaysDto>
    private var editor: RestHolidaysEditor

    init {
        this.className = "rest-holidays-list"
        editor = RestHolidaysEditor(restHolidaysService)
        gridList = GridList(RestHolidaysDto::class.java, restHolidaysService, editor)
        configureGrid()

        editor.isVisible = false
        editor.refreshable = gridList

        //create the page UI
        this.add(gridList)
        //this.addToSecondary(editor)
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