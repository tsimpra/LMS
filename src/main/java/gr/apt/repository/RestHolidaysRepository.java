package gr.apt.repository;

import gr.apt.persistence.holiday.RestHolidays;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;

@ApplicationScoped
public class RestHolidaysRepository implements PanacheRepositoryBase<RestHolidays, BigInteger> {
}
