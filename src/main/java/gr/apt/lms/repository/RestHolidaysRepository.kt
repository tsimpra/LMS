package gr.apt.lms.repository

import gr.apt.lms.persistence.holiday.RestHolidays
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class RestHolidaysRepository : PanacheRepositoryBase<RestHolidays?, BigInteger?>