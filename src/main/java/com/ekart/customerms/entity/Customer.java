package com.ekart.customerms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ekart.customerms.dto.CustomerDTO;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Integer customerId;
	@Column(name = "email", columnDefinition = "email")
	private String email;
	@Column(name = "first_name", columnDefinition = "first_name")
	private String firstName;
	@Column(name = "last_name", columnDefinition = "last_name")
	private String lastName;
	@Column(name = "password", columnDefinition = "password")
	private String password;
	@Column(name = "phone_number", columnDefinition = "phone_number")
	private Long phoneNumber;
	
	
	
	
	public Customer(CustomerDTO dto) {
		this.customerId= dto.getCustomer_id();
		this.email = dto.getEmail();
		this.firstName = dto.getFirstName();
		this.lastName = dto.getLastName();
		this.password = dto.getPassword();
		this.phoneNumber = dto.getPhoneNumber();
	}
}
