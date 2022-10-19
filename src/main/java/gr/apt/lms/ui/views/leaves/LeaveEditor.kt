package gr.apt.lms.ui.views.leaves

import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToBigIntegerConverter
import com.vaadin.quarkus.annotation.UIScoped
import gr.apt.lms.dto.leave.LeaveDto
import gr.apt.lms.dto.person.PersonDto
import gr.apt.lms.dto.person.fullname
import gr.apt.lms.metamodel.dto.LeaveDto_
import gr.apt.lms.persistence.enumeration.LeaveType
import gr.apt.lms.service.LeaveService
import gr.apt.lms.service.PersonService
import gr.apt.lms.ui.Refreshable
import gr.apt.lms.ui.components.AutoCompletableSelect
import gr.apt.lms.ui.components.Editor
import io.quarkus.arc.Arc
import java.time.LocalDate
import javax.inject.Inject

@UIScoped
class LeaveEditor @Inject constructor(leaveService: LeaveService) : Editor<LeaveDto>(leaveService) {

    private val id: TextField = TextField(LeaveDto_.ID_HEADER)
    private val description: TextField = TextField(LeaveDto_.DESCRIPTION_HEADER)
    private val type: AutoCompletableSelect<LeaveType> = AutoCompletableSelect()
    private val startDate: DatePicker = DatePicker(LeaveDto_.START_DATE_HEADER, LocalDate.now())
    private val endDate: DatePicker = DatePicker(LeaveDto_.END_DATE_HEADER)
    private val personId: AutoCompletableSelect<PersonDto> = AutoCompletableSelect()
    override val binder: Binder<LeaveDto> = Binder(LeaveDto::class.java)
    override lateinit var refreshable: Refreshable

    init {
        id.themeName = "custom-text-field-label"
        //Setting the read only fields
        id.isReadOnly = true

        type.label = LeaveDto_.TYPE_HEADER
        type.setItems(*LeaveType.values())

        personId.label = LeaveDto_.PERSON_ID_HEADER
        val personService = Arc.container().instance(PersonService::class.java).get()
        personId.setItems(personService.findAll(null, null))
        // set how we display the select list items
        personId.setItemLabelGenerator {
            it.fullname
        }

        //Bind form items
        binder.forField(id)
            .withNullRepresentation("")
            .withConverter(StringToBigIntegerConverter("Not a valid value for ID"))
            .bind(LeaveDto::id, null)
        binder.forField(description)
            .bind(LeaveDto::description) { leave, text -> leave.description = text }
        binder.forField(type)
            .bind(LeaveDto::type) { leave, text -> leave.type = text }
        binder.forField(startDate)
            .bind(LeaveDto::startDate) { leave, date -> leave.startDate = date }
        binder.forField(endDate)
            .bind(LeaveDto::endDate) { leave, date -> leave.endDate = date }
        binder.forField(personId)
            .bind({ leaveDto: LeaveDto ->
                if (leaveDto.personId != null)
                    personService.findById(leaveDto.personId!!)
                else
                    PersonDto()
            }) { leave, text -> leave.personId = text.id }
        //not sure if we need this
        binder.bindInstanceFields(this)

        fillFormLayoutWithComponents(id, description, type, startDate, endDate, personId)
    }

    override fun LeaveDto.isNewObject() = this.id == null
}