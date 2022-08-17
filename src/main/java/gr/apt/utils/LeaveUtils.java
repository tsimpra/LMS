package gr.apt.utils;

import gr.apt.persistence.entity.Leave;
import gr.apt.persistence.entity.Person;
import gr.apt.persistence.enumeration.LeaveType;
import gr.apt.persistence.enumeration.YesOrNo;
import gr.apt.persistence.holiday.MobileHolidays;
import gr.apt.persistence.holiday.PublicHolidays;
import gr.apt.persistence.holiday.RestHolidays;
import gr.apt.repository.RestHolidaysRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static gr.apt.utils.CommonUtils.isNeitherNullNorEmpty;

@ApplicationScoped
public class LeaveUtils {

    private static RestHolidaysRepository restHolidaysRepository;

    @Inject
    public LeaveUtils(RestHolidaysRepository restHolidaysRepository) {
        LeaveUtils.restHolidaysRepository = restHolidaysRepository;
    }

    public static Integer getNumberOfRequestedLeaves(Leave leave) {
        int daysCount = leave.getEndDate().compareTo(leave.getStartDate()) + 1;
        int weekendsCount = 0;
        int publicHolidays = 0;
        AtomicInteger restHolidays = new AtomicInteger();
        List<RestHolidays> restHolidaysList = restHolidaysRepository.list("startDate >= ?1 and endDate <= ?2", leave.getStartDate(), leave.getEndDate());
        for (int i = 0; i < daysCount; i++) {
            DayOfWeek day = leave.getStartDate().plusDays(i).getDayOfWeek();
            AtomicReference<LocalDate> leaveDate = new AtomicReference<>(leave.getStartDate().plusDays(i));
            if (day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY)) {
                weekendsCount++;
                continue;
            }
            if (PublicHolidays.getPublicHolidays().contains(leaveDate.get()) ||
                    MobileHolidays.getMobileHolidays().contains(leaveDate.get())) {
                publicHolidays++;
            }
            if(isNeitherNullNorEmpty(restHolidaysList)){
                restHolidaysList.forEach(restHoliday -> {
                    restHoliday.getStartDate().datesUntil(restHoliday.getEndDate())
                            .forEach(innerDate ->{
                                if(leaveDate.get().equals(innerDate)){
                                    restHolidays.getAndIncrement();
                                }
                            });
                });
            }
        }
        return daysCount - weekendsCount - publicHolidays - restHolidays.get();
    }

    public static Integer calculateTotalNumberOfLeaves(LocalDate dateOfEmployment) {
        int leaves = 0;
        long totalDays = ChronoUnit.DAYS.between(dateOfEmployment,LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31));//compareTo(dateOfEmployment);
        if (totalDays > 25 * 365) {
            leaves = 26;
        } else if (totalDays > 10 * 365) {
            leaves = 25;
        } else if (totalDays > 2 * 365) {
            leaves = 22;
        } else if (totalDays >= 365) {
            leaves = 21;
        } else {
            int workingDays = 0;
            for (int i = dateOfEmployment.getDayOfMonth(); i <= dateOfEmployment.getMonth().maxLength(); i++) {
                DayOfWeek day = LocalDate.of(dateOfEmployment.getYear(), dateOfEmployment.getMonth(), i).getDayOfWeek();
                if (!(/*day.equals(DayOfWeek.SATURDAY) || */day.equals(DayOfWeek.SUNDAY))) {
                    workingDays++;
                }
            }
            leaves = (int) Math.round(((12 - dateOfEmployment.getMonthValue()) * 25 + workingDays) * (20. / 12. / 25.));
        }
        return leaves;
    }

    public static Integer getRemainingLeaves(Person person) {
        return person.getNumberOfLeaves() - getUsedLeaves(person);
    }

    public static Integer getUsedLeaves(Person person) {
        Integer used = 0;
        if (isNeitherNullNorEmpty(person.getLeavesById())) {
            for (Leave leave : person.getLeavesById()) {
                if(leave.getApproved() != null && leave.getApproved().equals(YesOrNo.YES)) {
                    if (leave.getType().equals(LeaveType.PAID_LEAVE)) {
                        used = used + getNumberOfRequestedLeaves(leave);
                    }
                }
            }
        }
        return used;
    }
}
