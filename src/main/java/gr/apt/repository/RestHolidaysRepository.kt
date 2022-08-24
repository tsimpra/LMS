package gr.apt.repository

import gr.apt.persistence.holiday.RestHolidays
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RestHolidaysRepository : PanacheRepositoryBase<RestHolidays?, BigInteger?>