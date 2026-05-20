package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.services.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService departmentService) {
        this.service = departmentService;
    }

    @GetMapping("")
    public List<Department> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Department one(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @PostMapping("")
    public ResponseEntity<Department> createDepartment(
            @RequestBody Department department) {

        Department created = service.create(department);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {

        if (!service.existsById(id)) {
            throw new DepartmentNotFoundException(id);
        }

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}