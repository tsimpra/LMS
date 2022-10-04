package gr.apt.lms.repository

import gr.apt.lms.persistence.entity.Menu
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.math.BigInteger
import javax.inject.Singleton

@Singleton
class MenuRepository : PanacheRepositoryBase<Menu?, BigInteger?> {
}