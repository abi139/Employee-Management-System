package com.project.employee.service;

import com.project.employee.entity.Employee;
import com.project.employee.entity.Address;
import com.project.employee.repo.EmployeeRepo;
import com.project.employee.repo.AddressRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AddressRepo addressRepo;

    public Employee addEmployee(@Valid Employee emp) {
        if (emp.getAddresses() != null) {
            for (Address address : emp.getAddresses()) {
                address.setEmployee(emp);  
            }
            List<Address> savedAddresses = addressRepo.saveAll(emp.getAddresses());
            emp.setAddresses(savedAddresses);
        }
        return employeeRepo.save(emp);
    }
    
  
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

   
    public Optional<Employee> getEmployeeById(int id) {
        return employeeRepo.findById(id);
    }

    
    public Employee updateEmployee(int id, @Valid Employee updatedEmployee) {
        return employeeRepo.findById(id)
                .map(employee -> {
                    employee.setName(updatedEmployee.getName());
                    employee.setEmail(updatedEmployee.getEmail());
                    employee.setPosition(updatedEmployee.getPosition());
                    employee.setSalary(updatedEmployee.getSalary());

                    if (updatedEmployee.getAddresses() != null && !updatedEmployee.getAddresses().isEmpty()) {
           
                        for (Address address : updatedEmployee.getAddresses()) {
                            address.setEmployee(employee);  
                        }

                        List<Address> savedAddresses = addressRepo.saveAll(updatedEmployee.getAddresses());
                        employee.setAddresses(savedAddresses);  
                    }

                    return employeeRepo.save(employee);
                }).orElse(null);
    }

 
    public boolean deleteEmployee(int id) {
        if (employeeRepo.existsById(id)) {
            employeeRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
