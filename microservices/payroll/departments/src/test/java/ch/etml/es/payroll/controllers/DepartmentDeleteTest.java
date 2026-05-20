package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.PayrollApplication;
import ch.etml.es.payroll.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = PayrollApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")

class DepartmentDeleteTest {

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

        existingDepartment = departmentRepository.save(department);
    }

    @Test
    void when_deleting_existing_employee_then_success() {

        // GIVEN
        // none

        // WHEN
        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/departments/{id}",
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        existingDepartment.getId()
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);

        // THEN (database)
        assertThat(departmentRepository.findById(existingDepartment.getId()))
                .isEmpty();
    }

    @Test
    void when_deleting_nonexistent_department_then_not_found_returned() {
        // GIVEN
        Long nonExistentDepartmentId = 999L;

        // WHEN
        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/api/v1/departments/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        nonExistentDepartmentId
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody())
                .contains("Could not find department: " + nonExistentDepartmentId);
    }
}
