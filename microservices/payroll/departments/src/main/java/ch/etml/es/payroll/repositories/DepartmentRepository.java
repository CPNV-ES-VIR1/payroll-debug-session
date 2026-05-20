package ch.etml.es.payroll.repositories;

import ch.etml.es.payroll.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    Optional<Department> findByAcronym(String acronym);
}
