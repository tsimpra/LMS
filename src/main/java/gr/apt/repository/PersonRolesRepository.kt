package gr.apt.repository

import gr.apt.persistence.entity.PersonRoles
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRolesRepository : PanacheRepositoryBase<PersonRoles?, BigInteger?>