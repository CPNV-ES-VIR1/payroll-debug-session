package ch.cpnv.es.payroll.Repositories;

import ch.cpnv.es.payroll.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
