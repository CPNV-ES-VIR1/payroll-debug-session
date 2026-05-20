package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.PayrollApplication;
import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.repositories.DepartmentRepository;
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
class DepartmentGetTest {

    private static final String BASE_URL = "/api/v1/departments";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department existingDepartment;

    @BeforeEach
    void given_an_existing_department() {
        // GIVEN
        departmentRepository.deleteAll();

        Department department = new Department("MKT", "Marketing");
        Department department2 = new Department("SAS", "Sales");
        existingDepartment = departmentRepository.save(department);
        departmentRepository.save(department2);
    }

    @Test
    void when_getting_existing_department_then_success() {
        // WHEN
        ResponseEntity<Department> response =
                restTemplate.getForEntity(
                        BASE_URL + "/{id}",
                        Department.class,
                        existingDepartment.getId()
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        // THEN (body)
        Department body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(existingDepartment.getId());
        assertThat(body.getAcronym()).isEqualTo("MKT");
        assertThat(body.getDescription()).isEqualTo("Marketing");
    }

    @Test
    void when_getting_all_departments_then_success() {
        // WHEN
        ResponseEntity<List<Department>> response =
                restTemplate.exchange(
                        BASE_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Department>>() {}
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        // THEN (body)
        List<Department> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).hasSizeGreaterThanOrEqualTo(2);

        assertThat(body)
                .extracting(Department::getDescription)
                .contains("Marketing", "Sales");
    }
}
