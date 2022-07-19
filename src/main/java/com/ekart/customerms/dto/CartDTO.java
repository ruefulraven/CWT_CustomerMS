package com.ekart.customerms.dto;

import com.ekart.customerms.entity.Cart;
import com.ekart.customerms.entity.Customer;
import com.ekart.customerms.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CartDTO {

	private Integer cartId;
	private Integer count;
	private Product product;
	private Customer customer;
	private Integer product_id;
	private Integer customer_id;
	private Integer isPaid;
	
	
	public CartDTO(Cart cart) {
		this.cartId = cart.getId();
		this.count = cart.getCount();
		this.product_id = this.product.getId();
		this.customer_id = this.getCustomer().getCustomerId();
	}
}
