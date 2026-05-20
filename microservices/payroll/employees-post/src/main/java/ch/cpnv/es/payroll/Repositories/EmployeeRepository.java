package ch.cpnv.es.payroll.Repositories;

import ch.cpnv.es.payroll.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    Optional<Employee> findByName(String name);
}
