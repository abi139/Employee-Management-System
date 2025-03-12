package com.project.employee;

import com.project.employee.entity.Employee;
import com.project.employee.entity.Address;
import com.project.employee.repo.EmployeeRepo;
import com.project.employee.repo.AddressRepo;
import com.project.employee.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private AddressRepo addressRepo; 

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Address address1, address2;

    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setId(101);
        address1.setAddressLine1("123 Street");
        address1.setCity("Kumaran Nagar");
        address1.setPincode("605001");
        address1.setState("Pondy");

        address2 = new Address();
        address2.setId(102);
        address2.setAddressLine1("456 Street");
        address2.setCity("Siruseri");
        address2.setPincode("90001");
        address2.setState("Chennai");

        employee = new Employee();
        employee.setId(1);
        employee.setName("Amita");
        employee.setEmail("amita@gmail.com");
        employee.setPosition("Engineer");
        employee.setSalary(60000);
        employee.setAddresses(List.of(address1, address2));

        address1.setEmployee(employee);
        address2.setEmployee(employee);
    }

    @Test
    void testAddEmployee() {
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);
        when(addressRepo.saveAll(anyList())).thenReturn(List.of(address1, address2));

        Employee savedEmployee = employeeService.addEmployee(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getAddresses()).isNotEmpty();
        assertThat(savedEmployee.getAddresses().get(0).getCity()).isEqualTo("Kumaran Nagar");

        verify(employeeRepo, times(1)).save(any(Employee.class));
        verify(addressRepo, times(1)).saveAll(anyList()); 
    }

    @Test
    void testGetAllEmployees() {
        Employee employee2 = new Employee();
        Address address3 = new Address();
        address3.setId(103);
        address3.setAddressLine1("789 Street");
        address3.setCity("Tambaram");
        address3.setPincode("600100");
        address3.setState("Chennai");

        employee2.setId(2);
        employee2.setName("Baby");
        employee2.setEmail("baby@gmail.com");
        employee2.setPosition("Manager");
        employee2.setSalary(80000);
        employee2.setAddresses(List.of(address3));

        address3.setEmployee(employee2);

        when(employeeRepo.findAll()).thenReturn(Arrays.asList(employee, employee2));

        List<Employee> employees = employeeService.getAllEmployees();

        assertThat(employees).hasSize(2);
        assertThat(employees.get(0).getAddresses()).isNotEmpty();
        assertThat(employees.get(1).getAddresses()).isNotEmpty();
        assertThat(employees.get(1).getAddresses().get(0).getCity()).isEqualTo("Tambaram");

        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1);

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getName()).isEqualTo("Amita");
        assertThat(foundEmployee.get().getAddresses()).isNotEmpty();
        assertThat(foundEmployee.get().getAddresses().get(0).getAddressLine1()).isEqualTo("123 Street");

        verify(employeeRepo, times(1)).findById(1);
    }

 
    
    @Test
    void testUpdateEmployee() {
        Address newAddress1 = new Address();
        newAddress1.setId(201);
        newAddress1.setAddressLine1("Nehru Street");
        newAddress1.setCity("Kovalam");
        newAddress1.setPincode("98101");
        newAddress1.setState("Chennai");

        Address newAddress2 = new Address();
        newAddress2.setId(202);
        newAddress2.setAddressLine1("Gandhi Road");
        newAddress2.setCity("Velachery");
        newAddress2.setPincode("600042");
        newAddress2.setState("Chennai");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setName("Alice");
        updatedEmployee.setEmail("alice.updated@gmail.com");
        updatedEmployee.setPosition("Senior Engineer");
        updatedEmployee.setSalary(75000);
        updatedEmployee.setAddresses(List.of(newAddress1, newAddress2));

        newAddress1.setEmployee(updatedEmployee);
        newAddress2.setEmployee(updatedEmployee);

        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(addressRepo.saveAll(anyList())).thenReturn(List.of(newAddress1, newAddress2));
        when(employeeRepo.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(1, updatedEmployee);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getAddresses()).isNotEmpty();
        assertThat(result.getAddresses().size()).isEqualTo(2);
        assertThat(result.getAddresses().get(0).getCity()).isEqualTo("Kovalam");
        assertThat(result.getAddresses().get(1).getCity()).isEqualTo("Velachery");

        verify(employeeRepo, times(1)).findById(1);
        verify(addressRepo, times(1)).saveAll(anyList());  
        verify(employeeRepo, times(1)).save(any(Employee.class));  
    }


    @Test
    void testDeleteEmployee() {
        when(employeeRepo.existsById(1)).thenReturn(true);
        doNothing().when(employeeRepo).deleteById(1);

        boolean isDeleted = employeeService.deleteEmployee(1);

        assertThat(isDeleted).isTrue();
        verify(employeeRepo, times(1)).deleteById(1);
    }
}

