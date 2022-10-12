package gr.apt.lms.ui.views.restholidays

import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import gr.apt.lms.dto.RestHolidaysDto
import gr.apt.lms.metamodel.dto.RestHolidaysDto_
import gr.apt.lms.service.RestHolidaysService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.Editor
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestHolidaysEditor
@Inject
constructor(restHolidaysService: RestHolidaysService) : Editor<RestHolidaysDto>(restHolidaysService) {

    private val id: TextField = TextField(RestHolidaysDto_.ID_HEADER)
    private val description: TextField = TextField(RestHolidaysDto_.DESCRIPTION_HEADER)
    private val startDate: DatePicker = DatePicker(RestHolidaysDto_.START_DATE_HEADER, LocalDate.now())
    private val endDate: DatePicker = DatePicker(RestHolidaysDto_.END_DATE_HEADER)

    override val binder: Binder<RestHolidaysDto> = Binder(RestHolidaysDto::class.java)
    override lateinit var refreshable: Refreshable

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        //Bind form items
        binder.forField(id)
            .withNullRepresentation("")
            .withConverter(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(RestHolidaysDto::id, null)
        binder.forField(description)
            .bind(RestHolidaysDto::description) { restHolidaysDto, text -> restHolidaysDto.description = text }
        binder.forField(startDate)
            .bind(RestHolidaysDto::startDate) { restHolidaysDto, date -> restHolidaysDto.startDate = date }
        binder.forField(endDate)
            .bind(RestHolidaysDto::endDate) { restHolidaysDto, date -> restHolidaysDto.endDate = date }
        //not sure if we need this
        binder.bindInstanceFields(this)

        fillFormLayoutWithComponents(id, description, startDate, endDate)
    }

    override fun RestHolidaysDto.isNewObject() = this.id == null


}