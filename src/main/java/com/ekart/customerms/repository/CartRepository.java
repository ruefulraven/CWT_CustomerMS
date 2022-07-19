package com.ekart.customerms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ekart.customerms.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	@Query(value = "SELECT * FROM cart c WHERE c.customer_id = :customerId AND c.product_id = :productId", nativeQuery = true)
	public Cart orderExists(@Param(value = "customerId") Integer customerId, @Param(value = "productId") Integer productId);
}
