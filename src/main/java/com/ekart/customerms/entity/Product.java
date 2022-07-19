package com.ekart.customerms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity(name = "product")
public class Product {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer id;
	@Column(columnDefinition = "details", name = "details")
	private String details;
	@Column(columnDefinition = "product_name", name = "productName")
	private String productName;
	@Column(columnDefinition = "description", name = "description")
	private String description;
	@Column(columnDefinition = "category", name = "category")
	private String category;
	@Column(columnDefinition = "brand", name = "brand")
	private String brand;
	@Column(columnDefinition = "price", name = "price")
	private double price;
	@Column(columnDefinition = "image", name = "image")
	private byte[] image;
	
	
	/*
	public ProductDTO(Product product) {
		this.details = product.getDetails();
		this.productName = product.getProductName();
		this.description = product.getDescription();
		this.category = product.getCategory();
		this.brand = product.getBrand();
		this.price = product.getPrice();
		this.image = product.getImage();
	}*/
}
