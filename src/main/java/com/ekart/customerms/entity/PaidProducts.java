package com.ekart.customerms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ekart.customerms.dto.CartDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "paid_products")
public class PaidProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer count;
	private Integer product_id;
	private Integer customer_id;
	private Integer isPaid;
	
	public PaidProducts(Cart cart) {
		this.id = cart.getId();
		this.count = cart.getCount();
		this.product_id = cart.getProduct_id();
		this.customer_id = cart.getCustomer_id();
	}
}
