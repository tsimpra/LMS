package gr.apt.repository;

import gr.apt.persistence.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<Role, BigInteger> {
}
