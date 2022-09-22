package gr.apt.lms.utils

import com.vaadin.flow.component.select.Select

fun <T : Any> Select<T>.autocomplete() {
    this.addAttachListener {
        if (this.listDataView.itemCount == 1) this.value = this.listDataView.getItem(0)
    }
}