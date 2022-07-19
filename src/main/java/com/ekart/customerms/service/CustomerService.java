package com.ekart.customerms.service;

import com.ekart.customerms.entity.Customer;

public interface CustomerService {
	//RegisterCustomer
	void registerCustomer(Customer register);
	Customer login(String email, String password);
}
