package com.ekart.customerms.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ekart.customerms.entity.Customer;
import com.ekart.customerms.utility.PasswordConstraint;
import com.ekart.customerms.utility.PhoneNumberConstraint;
import com.ekart.customerms.utility.PhoneNumberExistsConstraint;
import com.ekart.customerms.utility.UsernameConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CustomerDTO {

	private Integer customer_id;
	
	@Email(message = "Email must be in proper format")
	@NotBlank(message = "Email cant be empty")
	private String email;
	
	@NotBlank(message = "First Name cant be empty")
	@UsernameConstraint(message = "First name cant contain special character or numbers")
	private String firstName;
	@NotBlank(message = "Last Name cant be empty")
	@UsernameConstraint(message = "Last name cant contain special character or numbers")
	private String lastName;
	@NotBlank(message = "Password cant be empty")
	@PasswordConstraint
	private String password;
	
	@PhoneNumberConstraint
	@PhoneNumberExistsConstraint
	@NotNull(message = "Phone Number cant be empty")
	private Long phoneNumber;
	
	public CustomerDTO(Customer register) {
		this.customer_id = register.getCustomerId();
		this.email = register.getEmail();
		this.firstName = register.getFirstName();
		this.lastName = register.getLastName();
		this.password = register.getPassword();
		this.phoneNumber = register.getPhoneNumber();
	}
	
}
