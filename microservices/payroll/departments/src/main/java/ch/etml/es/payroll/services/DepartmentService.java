package ch.etml.es.payroll.services;

import ch.etml.es.payroll.controllers.DepartmentAlreadyExistsException;
import ch.etml.es.payroll.controllers.DepartmentNotFoundException;
import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public Department create(Department department) {
        Department existing = repository.findByAcronym(department.getAcronym())
                .orElse(null);

        if (existing != null) {
            throw new DepartmentAlreadyExistsException(department.getAcronym());
        }
        return repository.save(department);
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

    //TODO Eval Simple
    public Optional<Department> findById(Long id) {
        return Optional.of(repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id)));
    }

    //TODO Eval Simple
    public Department upsert(Long id, Department department) {
        boolean exists = repository.existsById(id);

        department.setId(id);

        return repository.save(department);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
