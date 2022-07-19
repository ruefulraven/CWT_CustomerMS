package com.ekart.customerms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ekart.customerms.dto.CartDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer count;
	private Integer product_id;
	private Integer customer_id;
	private Integer isPaid;
	
	public Cart(CartDTO dto) {
		this.id = dto.getCartId();
		this.count = dto.getCount();
		this.product_id = dto.getProduct_id();
		this.customer_id = dto.getCustomer_id();
	}
}
