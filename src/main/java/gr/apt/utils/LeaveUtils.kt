package gr.apt.utils

import gr.apt.exception.LmsException
import gr.apt.persistence.entity.Leave
import gr.apt.persistence.entity.Person
import gr.apt.persistence.enumeration.LeaveType
import gr.apt.persistence.enumeration.YesOrNo
import gr.apt.persistence.holiday.MobileHolidays
import gr.apt.persistence.holiday.PublicHolidays
import gr.apt.repository.LeaveRepository
import gr.apt.repository.RestHolidaysRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import javax.enterprise.inject.spi.CDI


fun Leave.getNumberOfRequestedLeaves(): Int {
    if (this.startDate == null || this.endDate == null)
        throw LmsException(
            "Leave with id ${this.id} has null value on start date and/or end date)"
        )
    val restHolidaysRepository = CDI.current().select<RestHolidaysRepository>(
        RestHolidaysRepository::class.java
    ).get()
    val daysCount = this.endDate?.compareTo(this.startDate)?.plus(1) ?: 0
    var weekendsCount = 0
    var publicHolidays = 0
    var restHolidays = 0
    val restHolidaysList =
        restHolidaysRepository.list("startDate >= ?1 and endDate <= ?2", this.startDate, this.endDate).filterNotNull()
    for (i in 0 until daysCount) {
        val day: DayOfWeek? = this.startDate!!.plusDays(i.toLong())?.getDayOfWeek()
        val leaveDate = this.startDate!!.plusDays(i.toLong())
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            weekendsCount++
            continue
        }
        if (PublicHolidays.publicHolidays.contains(leaveDate) ||
            MobileHolidays.mobileHolidays.contains(leaveDate)
        ) {
            publicHolidays++
        }
        if (restHolidaysList.isNeitherNullNorEmpty()) {
            restHolidaysList.forEach { restHoliday ->
                if (restHoliday.startDate == null) throw LmsException("RestHoliday with id ${restHoliday.id} has null start date")
                //plus 1 day in order to include end date. if end date is null, and rest day counts only for a day, then get start date and add 1 day also for same reason.
                restHoliday.startDate!!.datesUntil(
                    restHoliday.endDate?.plusDays(1) ?: restHoliday.startDate!!.plusDays(1)
                )
                    .forEach { innerDate ->
                        if (leaveDate == innerDate) {
                            restHolidays++
                        }
                    }
            }
        }
    }
    return daysCount - weekendsCount - publicHolidays - restHolidays
}


fun calculateTotalNumberOfLeaves(dateOfEmployment: LocalDate): Int {
    var leaves = 0
    val totalDays: Long = ChronoUnit.DAYS.between(
        dateOfEmployment,
        LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31)
    ) //compareTo(dateOfEmployment);
    leaves = if (totalDays > 25 * 365) {
        26
    } else if (totalDays > 10 * 365) {
        25
    } else if (totalDays > 2 * 365) {
        22
    } else if (totalDays >= 365) {
        21
    } else {
        var workingDays = 0
        for (i in dateOfEmployment.getDayOfMonth()..dateOfEmployment.getMonth().maxLength()) {
            val day: DayOfWeek =
                LocalDate.of(dateOfEmployment.getYear(), dateOfEmployment.getMonth(), i).getDayOfWeek()
            if (day != DayOfWeek.SUNDAY) {
                workingDays++
            }
        }
        Math.round(((12 - dateOfEmployment.getMonthValue()) * 25 + workingDays) * (20.0 / 12.0 / 25.0))
            .toInt()
    }
    return leaves
}


fun Person.getRemainingLeaves(): Int {
    return this.numberOfLeaves?.minus(this.getUsedLeaves())
        ?: throw LmsException("Person with id ${this.id} cannot have null remaining leaves. Consider initializing his number of leaves")
}

fun Person.getUsedLeaves(): Int {
    var used = 0
    val leaveRepository = CDI.current().select<LeaveRepository>(LeaveRepository::class.java).get()
    val personLeaves =
        leaveRepository.getPersonLeaves(this.id ?: throw LmsException("A person cannot have null id")).filterNotNull()
    if (personLeaves.isNeitherNullNorEmpty()) {
        personLeaves.forEach {
            if (it.approved != null && it.approved == YesOrNo.YES) {
                if (it.type == LeaveType.PAID_LEAVE) {
                    used += it.getNumberOfRequestedLeaves()
                }
            }
        }
    }
    return used
}
