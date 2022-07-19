package com.ekart.customerms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekart.customerms.entity.Customer;
import com.ekart.customerms.repository.CustomerRepository;

@Service
public class CustomerServiceImpl  implements CustomerService{

	@Autowired
	private CustomerRepository repository;
	
	@Override
	public void registerCustomer(Customer register) {
		repository.save(register);
	}

	@Override
	public Customer login(String email, String password) {
		Customer customer = repository.login(email, password);
		return customer;
	}
	
	public void sample() {
		
	}
	
}
