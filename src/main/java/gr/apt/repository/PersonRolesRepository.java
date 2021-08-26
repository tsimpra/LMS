package gr.apt.repository;

import gr.apt.persistence.entity.PersonRoles;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;

@ApplicationScoped
public class PersonRolesRepository implements PanacheRepositoryBase<PersonRoles, BigInteger> {
}
