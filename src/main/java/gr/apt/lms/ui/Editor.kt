package gr.apt.lms.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationException
import gr.apt.lms.exception.LmsException
import gr.apt.lms.service.CrudService

abstract class Editor<T : Any>(
    private var service: CrudService<T>,
    private var dataHolder: Refreshable,
) : VerticalLayout() {

    //Create the buttons for our UI
    private val save = Button("Save", Icon(VaadinIcon.CHECK))
    private val cancel = Button("Cancel", Icon(VaadinIcon.CLOSE))
    private val delete = Button("Delete", Icon(VaadinIcon.MINUS))

    private var selected: T? = null
    private val formLayout = FormLayout()
    abstract val binder: Binder<T>

    init {

        formLayout.setResponsiveSteps(
            FormLayout.ResponsiveStep("1px", 1),
            FormLayout.ResponsiveStep("200px", 2),
            FormLayout.ResponsiveStep("700px", 3)
        )

        configureButtons()

        this.add(formLayout, groupButtons(save, delete, cancel))
        this.width = "35%"

    }

    abstract fun T.isNewObject(): Boolean

    internal fun fillFormLayoutWithComponents(vararg c: Component) {
        formLayout.add(*c)
    }

    internal fun configureButtons() {
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
                    dataHolder.refresh()
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
                dataHolder.refresh()
                Notification.show("Person deleted successfully.")
            }
        }

        cancel.addClickListener {
            clearForm()
            //dataHolder.refresh()
        }
    }

    internal fun clearForm() {
        populateForm(null)
        save.isEnabled = false
        delete.isEnabled = false
        this.isVisible = false
    }

    internal fun enableForm(value: T, enableDelete: Boolean = false) {
        populateForm(value)
        save.isEnabled = true
        this.isVisible = true
        delete.isEnabled = enableDelete
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