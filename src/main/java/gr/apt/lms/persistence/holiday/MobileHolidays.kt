package gr.apt.lms.persistence.holiday

import java.time.LocalDate
import java.time.Month

object MobileHolidays {
    private val pascha: LocalDate
        get() {
            val year = LocalDate.now().year
            val a = year / 100 - 16 - (year / 100 - 16) / 4 + 10
            val b = (19 * (year % 19) + 15) % 30
            val c = b - (year + year / 4 + b) % 7 + a
            val day = 1 + (c + 27 + (c + 6) / 40) % 31
            val month = 3 + (c + 26) / 30
            return LocalDate.of(year, month, day)
        }

    //System.out.println("Clean Monday: "+cleanMonday+" Great Friday: "+greatFriday+" Pascha's Monday: "+paschaMonday+" Holy Spirit: "+holySpirit+ "May 1st: "+may1st);
    val mobileHolidays: List<LocalDate>
        get() {
            val cleanMonday = pascha.minusDays(48)
            val greatFriday = pascha.minusDays(2)
            val paschaMonday = pascha.plusDays(1)
            val holySpirit = pascha.plusDays(50)
            var may1st = LocalDate.of(LocalDate.now().year, Month.MAY, 1)
            if (!may1st.isBefore(greatFriday) && !may1st.isAfter(paschaMonday)) {
                may1st = paschaMonday.plusDays(1)
            }
            //System.out.println("Clean Monday: "+cleanMonday+" Great Friday: "+greatFriday+" Pascha's Monday: "+paschaMonday+" Holy Spirit: "+holySpirit+ "May 1st: "+may1st);
            return listOf(cleanMonday, greatFriday, paschaMonday, holySpirit, may1st)
        }
}