package ch.cpnv.es.payroll.Controllers;

import ch.cpnv.es.payroll.Entities.Employee;
import ch.cpnv.es.payroll.PayrollApplication;
import ch.cpnv.es.payroll.Repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = PayrollApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class EmployeeGetTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee existingEmployee;

    @BeforeEach
    void given_an_existing_employee() {
        // GIVEN
        employeeRepository.deleteAll();

        Employee employee = new Employee("Doe", "Supervisor");
        Employee employee2 = new Employee("Smith", "Developer");
        existingEmployee = employeeRepository.save(employee);
        employeeRepository.save(employee2);
    }

    @Test
    void when_getting_existing_employee_then_success() {
        // WHEN
        ResponseEntity<Employee> response =
                restTemplate.getForEntity(
                        "/api/v1/employees/{id}",
                        Employee.class,
                        existingEmployee.getId()
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        // THEN (body)
        Employee body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(existingEmployee.getId());
        assertThat(body.getName()).isEqualTo("Doe");
        assertThat(body.getRole()).isEqualTo("SUPERVISOR");
    }

    @Test
    void when_getting_all_employees_then_success() {
        // WHEN
        ResponseEntity<List<Employee>> response =
                restTemplate.exchange(
                        "/api/v1/employees",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Employee>>() {}
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        // THEN (body)
        List<Employee> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).hasSizeGreaterThanOrEqualTo(2);

        assertThat(body)
                .extracting(Employee::getName)
                .contains("Doe", "Smith");
    }
}
