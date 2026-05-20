package ch.cpnv.es.payroll.Controllers;

import ch.cpnv.es.payroll.Entities.Employee;
import ch.cpnv.es.payroll.Repositories.EmployeeRepository;
import ch.cpnv.es.payroll.Services.EmployeeServicePost;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeControllerPost {

    EmployeeControllerPost(EmployeeServicePost employeeServicePost) {
    }

    /* curl sample :
    curl -i -X POST localhost:8080/api/v1/employees ^
        -H "Content-type:application/json" ^
        -d "{\"name\": \"Russel George\", \"role\": \"gardener\"}"
    */
    @PostMapping("")
    public ResponseEntity<Employee> hireEmployee(@RequestBody Employee employee) {
        Employee created = EmployeeServicePost.hire(employee);

        return ResponseEntity
                .created(URI.create("/api/v1/employees/" + created.getId()))
                .body(created);
    }
}