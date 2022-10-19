package gr.apt.lms.ui.components

import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLink
import gr.apt.lms.persistence.entity.Menu
import java.math.BigInteger

class MenuTab(
    menu: Menu,
    val router: RouterLink = RouterLink()
) : Tab() {

    private val defaultIcon = Icon()

    var id: BigInteger? = null
    var menuLabel: String? = null
    var ref: BigInteger? = null

    init {
        this.id = menu.id
        this.menuLabel = menu.description
        this.ref = menu.parentId
        router.highlightCondition = HighlightConditions.sameLocation()
        val horizontalLayout = HorizontalLayout()
        if (menu.icon != null && menu.icon!!.isNotEmpty()) horizontalLayout.add(Icon(VaadinIcon.valueOf(menu.icon!!)))
        else horizontalLayout.add(defaultIcon)
        horizontalLayout.add(Span(menuLabel))
        router.add(horizontalLayout)
        this.add(router)
    }
}