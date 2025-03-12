package com.project.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.employee.entity.Employee;
import com.project.employee.service.EmployeeService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

   @PostMapping
    
    public ResponseEntity<Employee> addEmployee(@RequestBody @Valid Employee employee) {
        employee.getAddresses().forEach(address -> address.setEmployee(employee)); 
        return ResponseEntity.ok(employeeService.addEmployee(employee));
    }

    
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return employees.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(employees);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody @Valid Employee updatedEmployee) {
        updatedEmployee.getAddresses().forEach(address -> address.setEmployee(updatedEmployee)); 
        Employee employee = employeeService.updateEmployee(id, updatedEmployee);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        return employeeService.deleteEmployee(id) ? 
               ResponseEntity.ok("Employee deleted successfully") : 
               ResponseEntity.notFound().build();
    }
}
