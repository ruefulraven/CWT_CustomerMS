package com.ekart.customerms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekart.customerms.entity.Cart;
import com.ekart.customerms.entity.PaidProducts;
import com.ekart.customerms.repository.CartRepository;
import com.ekart.customerms.repository.PaidProductRepository;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	CartRepository repository;
	
	@Autowired
	PaidProductRepository paidProductRepository;
	
	@Override
	public Cart SaveToCart(Cart cart) {
		return repository.save(cart);
	}

	@Override
	public Cart OrderExists(Integer customerId, Integer productId) {
		Cart cart = repository.orderExists(customerId, productId);
		cart = (cart == null) ? new Cart() : cart;
		
		if(cart.getCount() != null && cart.getCount() > 0) {
			Cart existingCart = repository.findById(cart.getId()).get();
			existingCart.setCount(existingCart.getCount() + 1);
			SaveToCart(existingCart);
		}
		return cart;
	}

	@Override
	public List<Cart> CustomerCart(Integer customerId) {
		return repository.findAll().stream()
				.filter(cart -> cart.getCustomer_id() == customerId && cart.getIsPaid() != 1).toList();
	}

	@Override
	public void deleteCartProduct(Integer cartId) {
		repository.deleteById(cartId);
	}

	@Override
	public Cart retrieveCart(Integer cartId) {
		return repository.getReferenceById(cartId);
	}

	@Override
	public void reduceCartProduct(Integer cartId) {
		Cart tempCart = repository.getReferenceById(cartId);
		tempCart.setCount(tempCart.getCount() - 1);
		repository.save(tempCart);
	}

	@Override
	public void payCartProducts(Integer customerId, List<Cart> customerProducts) {
		List<Cart> tempCustomerProducts  = new ArrayList<>();
		List<PaidProducts> tempPaidProducts = new ArrayList<>();
		customerProducts.forEach(product -> {
			//1 == Paid, 0/null == Not Paid
			product.setIsPaid(1);
			//Move Paid products to PaidProduct Table and remove the PaidProducts in Cart Table
			PaidProducts pProduct = new PaidProducts(product);
			pProduct.setIsPaid(product.getIsPaid());
			tempCustomerProducts.add(product);
			tempPaidProducts.add(pProduct);
		});
		paidProductRepository.saveAll(tempPaidProducts);
		repository.deleteAll(tempCustomerProducts);
	}

}
