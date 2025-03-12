
package com.project.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.employee.controller.EmployeeController;
import com.project.employee.entity.Employee;
import com.project.employee.entity.Address;
import com.project.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfigTest.class)
@WebMvcTest(EmployeeController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private Address address1, address2;

    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setId(101);
        address1.setAddressLine1("123 Street");
        address1.setCity("Adaiyar");
        address1.setPincode("10001");
        address1.setState("Chennai");

        address2 = new Address();
        address2.setId(102);
        address2.setAddressLine1("456 Avenue");
        address2.setCity("Avadi");
        address2.setPincode("90001");
        address2.setState("Chennai");

        employee = new Employee();
        employee.setId(1);
        employee.setName("John");
        employee.setEmail("john@gmail.com");
        employee.setPosition("Developer");
        employee.setSalary(50000);
        employee.setAddresses(List.of(address1, address2));

        address1.setEmployee(employee);
        address2.setEmployee(employee);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        Employee employee2 = new Employee();
        Address address3 = new Address();
        address3.setId(103);
        address3.setAddressLine1("789 Road");
        address3.setCity("Tambaram");
        address3.setPincode("20002");
        address3.setState("Tamil Nadu");

        employee2.setId(2);
        employee2.setName("Jana");
        employee2.setEmail("jana@gmail.com");
        employee2.setPosition("Manager");
        employee2.setSalary(70000);
        employee2.setAddresses(List.of(address3));

        address3.setEmployee(employee2);

        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee, employee2));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) 
                .andExpect(jsonPath("$[0].addresses.length()").value(2)) 
                .andExpect(jsonPath("$[1].addresses.length()").value(1)) 
                .andExpect(jsonPath("$[0].addresses[0].city").value("Adaiyar")) 
                .andExpect(jsonPath("$[1].addresses[0].city").value("Tambaram"));
        
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.addresses.length()").value(2))
                .andExpect(jsonPath("$.addresses[0].city").value("Adaiyar"))
                .andExpect(jsonPath("$.addresses[1].city").value("Avadi"));
    }

    @Test
    void testAddEmployee() throws Exception {
        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses.length()").value(2))
                .andExpect(jsonPath("$.addresses[0].city").value("Adaiyar"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee();
        Address updatedAddress1 = new Address();
        Address updatedAddress2 = new Address();

        updatedAddress1.setId(201);
        updatedAddress1.setAddressLine1("789 Street");
        updatedAddress1.setCity("Tambaram");
        updatedAddress1.setPincode("20002");
        updatedAddress1.setState("Tamil Nadu");

        updatedAddress2.setId(202);
        updatedAddress2.setAddressLine1("999 Street");
        updatedAddress2.setCity("Velachery");
        updatedAddress2.setPincode("30003");
        updatedAddress2.setState("Tamil Nadu");

        updatedEmployee.setId(1);
        updatedEmployee.setName("JohnU");
        updatedEmployee.setEmail("john.updated@gmail.com");
        updatedEmployee.setPosition("Senior Developer");
        updatedEmployee.setSalary(60000);
        updatedEmployee.setAddresses(List.of(updatedAddress1, updatedAddress2));

        updatedAddress1.setEmployee(updatedEmployee);
        updatedAddress2.setEmployee(updatedEmployee);

        when(employeeService.updateEmployee(eq(1), any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("JohnU"))
                .andExpect(jsonPath("$.email").value("john.updated@gmail.com"))
                .andExpect(jsonPath("$.position").value("Senior Developer"))
                .andExpect(jsonPath("$.addresses.length()").value(2))
                .andExpect(jsonPath("$.addresses[0].city").value("Tambaram"))
                .andExpect(jsonPath("$.addresses[1].city").value("Velachery"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        when(employeeService.deleteEmployee(anyInt())).thenReturn(true);
        
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));

        verify(employeeService, times(1)).deleteEmployee(1);
    }
}

