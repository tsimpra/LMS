package gr.apt.repository;

import gr.apt.persistence.entity.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;

@ApplicationScoped
public class PersonRepository implements PanacheRepositoryBase<Person, BigInteger> {
}
