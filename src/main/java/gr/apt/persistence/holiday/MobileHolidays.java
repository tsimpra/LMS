package gr.apt.persistence.holiday;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class MobileHolidays {
    public static LocalDate getPascha() {
        int year = LocalDate.now().getYear();
        int a = (year / 100) - 16 - (((year / 100) - 16) / 4) + 10;
        int b = (19 * (year % 19) + 15) % 30;
        int c = b - ((year + (year / 4) + b) % 7) + a;
        int day = 1 + (c + 27 + ((c + 6) / 40)) % 31;
        int month = 3 + (c + 26) / 30;
        return LocalDate.of(year, month, day);
    }

    public static List<LocalDate> getMobileHolidays() {
        LocalDate cleanMonday = getPascha().minusDays(48);
        LocalDate bigFriday = getPascha().minusDays(2);
        LocalDate paschaMonday = getPascha().plusDays(1);
        LocalDate holySpirit = getPascha().plusDays(50);
        LocalDate may1st = LocalDate.of(LocalDate.now().getYear(), Month.MAY, 1);
        if (!may1st.isBefore(bigFriday) && !may1st.isAfter(paschaMonday)) {
            may1st = paschaMonday.plusDays(1);
        }
        //System.out.println("Clean Monday: "+cleanMonday+" Big Friday: "+bigFriday+" Pascha's Monday: "+paschaMonday+" Holy Spirit: "+holySpirit+ "May 1st: "+may1st);
        return List.of(cleanMonday, bigFriday, paschaMonday, holySpirit, may1st);
    }

}
