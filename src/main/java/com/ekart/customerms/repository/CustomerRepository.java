package com.ekart.customerms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ekart.customerms.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	@Query(value = "SELECT * FROM customer c WHERE c.email = ?1  AND c.password = ?2", nativeQuery = true)
	Customer login(String email, String password);
}
