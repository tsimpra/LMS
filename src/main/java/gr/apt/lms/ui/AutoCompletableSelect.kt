package gr.apt.lms.ui

import com.vaadin.flow.component.select.Select
import gr.apt.lms.exception.LmsException

class AutoCompletableSelect<T : Any>() : Select<T>() {
    init {
        this.addAttachListener {
            if (this.listDataView == null) throw LmsException("Select list data are not initialized")
            if (this.listDataView.itemCount == 1) this.value = this.listDataView.getItem(0)
        }
    }
}