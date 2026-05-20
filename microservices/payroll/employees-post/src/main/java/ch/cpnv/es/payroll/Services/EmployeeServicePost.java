package ch.cpnv.es.payroll.Services;

import ch.cpnv.es.payroll.Controllers.EmployeeAlreadyExistsException;
import ch.cpnv.es.payroll.Entities.Employee;
import ch.cpnv.es.payroll.Repositories.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServicePost {

    private static EmployeeRepository repository = null;

    public EmployeeServicePost(EmployeeRepository repository) {
        EmployeeServicePost.repository = repository;
    }

    public static Employee hire(Employee employee) {
        Employee existing = repository.findByName(employee.getName())
                .orElse(null);

        if (existing != null) {
            throw new EmployeeAlreadyExistsException(employee.getName());
        }
        return repository.save(employee);
    }
}
