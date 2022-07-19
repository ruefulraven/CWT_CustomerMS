package com.ekart.customerms.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ekart.customerms.repository.CustomerRepository;

public class PhoneNumberExistsValidator implements ConstraintValidator<PhoneNumberExistsConstraint, Long>{

	@Autowired
	CustomerRepository repository;
	
	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return repository.findAll().
				stream().
				filter(customer -> customer.getPhoneNumber()
						.equals(value)).
				findAny().isEmpty();
	}

}
