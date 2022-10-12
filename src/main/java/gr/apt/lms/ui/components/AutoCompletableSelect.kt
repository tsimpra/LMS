package gr.apt.lms.ui.components

import com.vaadin.flow.component.select.Select
import gr.apt.lms.exception.LmsException

class AutoCompletableSelect<T : Any>(header: String? = null, clearable: Boolean = false) : Select<T>() {
    init {
        this.label = header
        this.isEmptySelectionAllowed = clearable
        this.addAttachListener {
            if (this.listDataView == null) throw LmsException("Select list data are not initialized")
            if (this.listDataView.itemCount == 1) this.value = this.listDataView.getItem(0)
        }
    }
}