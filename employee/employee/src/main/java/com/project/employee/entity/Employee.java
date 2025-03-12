package com.project.employee.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="employee_table")
public class Employee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Position cannot be empty")
    private String position;

    @Min(value = 0, message = "Salary must be positive")
    private double salary;

    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee", orphanRemoval = true) 
    @JsonManagedReference
    private List<Address> addresses; 

    public Employee() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }


    
    public List<Address> getAddresses() { 
    	return addresses;
    	}
    public void setAddresses(List<Address> addresses) { 
        this.addresses = addresses;
        for (Address address : addresses) {
            address.setEmployee(this);
            }
    }
}
