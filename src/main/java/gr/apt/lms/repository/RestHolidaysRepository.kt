package gr.apt.lms.repository

import gr.apt.lms.persistence.holiday.RestHolidays
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RestHolidaysRepository : PanacheRepositoryBase<RestHolidays?, BigInteger?>