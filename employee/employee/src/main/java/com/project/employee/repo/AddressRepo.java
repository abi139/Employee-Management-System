package com.project.employee.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.employee.entity.Address;

public interface AddressRepo extends JpaRepository<Address, Integer> {
}
