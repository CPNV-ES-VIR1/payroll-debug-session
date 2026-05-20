package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.PayrollApplication;
import ch.etml.es.payroll.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = PayrollApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class DepartmentPostTest {

    private static final String BASE_URL = "/api/v1/departments";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void given_an_empty_department_database() {
        // GIVEN
        departmentRepository.deleteAll();
    }

    @Test
    void when_creating_new_department_then_department_is_created_and_persisted() {
        // GIVEN
        Department newDepartment = new Department("MKT", "Marketing");

        HttpEntity<Department> request = new HttpEntity<>(newDepartment);

        // WHEN
        ResponseEntity<Department> response =
                restTemplate.postForEntity(
                        BASE_URL,
                        request,
                        Department.class
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        // THEN (body)
        Department createdDepartment = response.getBody();
        assertThat(createdDepartment).isNotNull();
        assertThat(createdDepartment.getId()).isNotNull();
        assertThat(createdDepartment.getAcronym()).isEqualTo("MKT");
        assertThat(createdDepartment.getDescription()).isEqualTo("Marketing");

        // THEN (database)
        assertThat(departmentRepository.findById(createdDepartment.getId()))
                .isPresent();
    }

    @Test
    void when_creating_existing_department_then_conflict_is_returned() {
        // GIVEN
        Department existingDepartment = new Department("MKT", "Marketing");
        departmentRepository.save(existingDepartment);

        Department duplicateDepartment = new Department("MKT", "Marketing");
        HttpEntity<Department> request = new HttpEntity<>(duplicateDepartment);

        // WHEN
        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        BASE_URL,
                        request,
                        String.class
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        // THEN (error message)
        assertThat(response.getBody())
                .isNotNull()
                .contains("Department "+ existingDepartment.getAcronym() + " already exists");
    }
}
