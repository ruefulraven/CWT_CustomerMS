package com.ekart.customerms.service;

import java.util.List;

import com.ekart.customerms.entity.Cart;

public interface CartService {

	public Cart SaveToCart(Cart cart);
	public Cart OrderExists(Integer customerId, Integer productId);
	public List<Cart> CustomerCart(Integer customerId);
	
	public void deleteCartProduct(Integer cartId);
	public Cart retrieveCart(Integer cartId);
	public void reduceCartProduct(Integer cartId);
	public void payCartProducts(Integer customerId, List<Cart> customerProducts);
}
