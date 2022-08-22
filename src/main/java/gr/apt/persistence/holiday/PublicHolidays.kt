package gr.apt.persistence.holiday

import java.time.LocalDate
import java.time.Month

object PublicHolidays {
    val PROTOXRONIA = LocalDate.of(LocalDate.now().year, Month.JANUARY, 1)
    val THEOFANEIA = LocalDate.of(LocalDate.now().year, Month.JANUARY, 6)
    val MARCH25TH = LocalDate.of(LocalDate.now().year, Month.MARCH, 25)
    val AUGUST15TH = LocalDate.of(LocalDate.now().year, Month.AUGUST, 15)
    val OCTOBER28TH = LocalDate.of(LocalDate.now().year, Month.OCTOBER, 28)
    val CHRISTMAS = LocalDate.of(LocalDate.now().year, Month.DECEMBER, 25)
    val DECEMBER26TH = LocalDate.of(LocalDate.now().year, Month.DECEMBER, 26)

    @JvmStatic
    val publicHolidays: List<LocalDate>
        get() = java.util.List.of(PROTOXRONIA, THEOFANEIA, MARCH25TH, AUGUST15TH, OCTOBER28TH, CHRISTMAS, DECEMBER26TH)
}