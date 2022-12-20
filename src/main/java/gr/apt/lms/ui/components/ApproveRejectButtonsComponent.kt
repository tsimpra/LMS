package gr.apt.lms.ui.components

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class ApproveRejectButtonsComponent(
    approveAction: ComponentEventListener<ClickEvent<Button>>,
    rejectAction: ComponentEventListener<ClickEvent<Button>>
) : HorizontalLayout() {

    private val approve = Button(Icon(VaadinIcon.CHECK_CIRCLE))
    private val reject = Button(Icon(VaadinIcon.CLOSE_CIRCLE))

    init {
        approve.addClickListener(approveAction)
        reject.addClickListener(rejectAction)
        this.defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
        this.add(approve, reject)
    }

}