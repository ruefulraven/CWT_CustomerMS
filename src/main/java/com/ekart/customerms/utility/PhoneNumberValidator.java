package com.ekart.customerms.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;



public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, Long>{

	@Override
	public void initialize(PhoneNumberConstraint constraintAnnotation) {
		
	}
	
	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return value != null && value.toString().length() < 9;
	}

}
