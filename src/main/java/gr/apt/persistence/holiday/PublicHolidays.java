package gr.apt.persistence.holiday;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class PublicHolidays {
    public static LocalDate PROTOXRONIA = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY,1);
    public static LocalDate THEOFANEIA = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY,6);
    public static LocalDate MARCH25TH = LocalDate.of(LocalDate.now().getYear(), Month.MARCH,25);
    public static LocalDate AUGUST15TH = LocalDate.of(LocalDate.now().getYear(), Month.AUGUST,15);
    public static LocalDate OCTOBER28TH = LocalDate.of(LocalDate.now().getYear(), Month.OCTOBER,28);
    public static LocalDate CHRISTMAS = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER,25);
    public static LocalDate DECEMBER26TH = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER,26);

    public static List<LocalDate> getPublicHolidays(){
        return List.of(PROTOXRONIA,THEOFANEIA,MARCH25TH,AUGUST15TH,OCTOBER28TH,CHRISTMAS,DECEMBER26TH);
    }
}
