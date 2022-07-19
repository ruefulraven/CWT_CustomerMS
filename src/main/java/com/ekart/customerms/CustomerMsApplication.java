package com.ekart.customerms;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.ekart.customerms.repository.ProductRepository;


@SpringBootApplication
@EnableDiscoveryClient
public class CustomerMsApplication {

	@Autowired
	ProductRepository productRepository;
	
	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(CustomerMsApplication.class, args);
		
		
	}
	
	

}
