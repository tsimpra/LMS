package gr.apt.lms.ui.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationException
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.CrudService
import gr.apt.lms.ui.Refreshable

abstract class Editor<T : Any>(
    private var service: CrudService<T>
    //private var refreshable: Refreshable
) : Dialog() {

    //Create the buttons for our UI
    private val save = Button("Save", Icon(VaadinIcon.CHECK))
    private val cancel = Button("Cancel", Icon(VaadinIcon.CLOSE))
    private val delete = Button("Delete", Icon(VaadinIcon.MINUS))

    private var selected: T? = null
    private val formLayout = FormLayout()
    abstract var refreshable: Refreshable
    abstract val binder: Binder<T>

    init {

        formLayout.setResponsiveSteps(
            FormLayout.ResponsiveStep("1px", 1),
            FormLayout.ResponsiveStep("200px", 2),
            FormLayout.ResponsiveStep("700px", 3)
        )

        configureButtons()

        //this.width = "35%"
        this.add(formLayout, groupButtons(save, delete, cancel))
        this.setHeight(60.0f, Unit.PERCENTAGE)
        this.setWidth(40.0f, Unit.PERCENTAGE)
    }

    abstract fun T.isNewObject(): Boolean

    internal fun fillFormLayoutWithComponents(vararg c: Component) {
        formLayout.add(*c)
    }

    private fun configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        save.isEnabled = false
        save.addClickListener {
            try {
                if (selected != null) {
                    binder.writeBean(selected)
                    if (selected!!.isNewObject()) {
                        service.create(selected ?: throw LmsException("cannot update with empty body"))
                        Notification.show("Person created successfully.")
                    } else {
                        service.update(selected ?: throw LmsException("cannot update with empty body"))
                        Notification.show("Person details stored.")
                    }
                    clearForm()
                    refreshable.refresh()
                }
            } catch (validationException: ValidationException) {
                Notification.show("An exception happened while trying to store the Person's details.")
            }
        }

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        delete.addClassName("delete-button")
        delete.isEnabled = false
        delete.addClickListener {
            if (selected != null) {
                service.delete(selected ?: throw LmsException("cannot delete with empty body"))
                clearForm()
                refreshable.refresh()
                Notification.show("Person deleted successfully.")
            }
        }

        cancel.addClickListener {
            clearForm()
            refreshable.refresh()
        }
    }

    internal fun clearForm() {
        populateForm(null)
        save.isEnabled = false
        delete.isEnabled = false
        this.close()
        //this.isVisible = false
    }

    internal fun enableForm(value: T, enableDelete: Boolean = false) {
        computeEditorComponentsData(value)
        populateForm(value)
        save.isEnabled = true
        this.isVisible = true
        delete.isEnabled = enableDelete
        this.open()
    }

    internal open fun computeEditorComponentsData(value: T) {
        //do nothing normally
    }

    private fun populateForm(value: T?) {
        selected = value
        binder.readBean(selected)
    }

    private fun groupButtons(vararg b1: Component): HorizontalLayout {
        val group = HorizontalLayout()
        group.setWidthFull()
        group.isSpacing = true
        group.add(*b1)
        return group
    }
}