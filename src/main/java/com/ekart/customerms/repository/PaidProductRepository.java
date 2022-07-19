package com.ekart.customerms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ekart.customerms.entity.PaidProducts;

public interface PaidProductRepository extends JpaRepository<PaidProducts, Integer>{

}
