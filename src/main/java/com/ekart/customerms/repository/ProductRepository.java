package com.ekart.customerms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ekart.customerms.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

}
