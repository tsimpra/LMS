package gr.apt.repository;

import gr.apt.persistence.entity.Leave;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;

@ApplicationScoped
public class LeaveRepository implements PanacheRepositoryBase<Leave, BigInteger> {
}
