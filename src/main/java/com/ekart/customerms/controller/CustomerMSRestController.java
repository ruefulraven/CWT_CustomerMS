package com.ekart.customerms.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ekart.customerms.dto.CustomerDTO;
import com.ekart.customerms.entity.Payment;
import com.ekart.customerms.entity.Product;
import com.ekart.customerms.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@RestController
public class CustomerMSRestController {

	@Autowired
	@Lazy
	RestTemplate restTemplate;
	
	@Autowired
	CustomerService service;
	
	@Autowired
	private DiscoveryClient client;
	
	String tempURI;
	URL servicePaymentMS;
	
	@Autowired
	@Lazy
	private WebClient localApiClient;
	
	private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
	
	@GetMapping("/greeting")
	public String getMessage() {
		return "Welcome :--]";
	}
	
	public CustomerDTO verifyRegisterDetails(CustomerDTO dto, BindingResult bindingResult) {
		return dto;
	}
	
	public void callURI() {
		System.out.println(tempURI);
	}
	
	public final String CUSTOMER_SERVICE_GETPRODUCTS="productServiceGetAll";
	public final String PRODUCT_SERVICE_GETPRODUCT="productServiceGetOne";
	
	@SuppressWarnings("unchecked")
	@CircuitBreaker(name = CUSTOMER_SERVICE_GETPRODUCTS, fallbackMethod = "displayTempProducts")
	@GetMapping("/productList")
	public List<Product> invokeGetProductsService() throws StreamReadException, DatabindException, MalformedURLException, IOException {
		URI service = client.getInstances("ProductMS").stream().map(services -> services.getUri())
				.findFirst()
				.map(method -> method.resolve("/products")).get();
		List<Product> pr = restTemplate.getForObject(service, List.class);
		
		URI service2 = client.getInstances("ProductMS").stream().map(services -> services.getUri())
				.findFirst().get();
		
		tempURI = service2.toURL().toString();
		return pr;
	}
	
	public List<Product> displayTempProducts(Exception e){
		return Stream.of(
				new Product(20, "13th GeForce", "Samsung Galaxy Mini", "Samsung Laptop", "GENERIC STUFFS", "GENERIC STUFFS", 79.43, null),
				new Product(21, "15th GeForce", "Toshiba Satellite", "Toshiba Laptop", "Laptop", "Toshiba", 43.47, null),
				new Product(22, "13th GeForce", "Toshiba Generic Laptop", "Toshiba Laptop", "Laptop", "Toshiba", 90.48, null)
			).collect(Collectors.toList());
	}
	
	@CircuitBreaker(name = PRODUCT_SERVICE_GETPRODUCT, fallbackMethod = "displayTempProduct")
	@GetMapping("/product")
	public Product getProductById(Integer id) {
    	localApiClient = WebClient.create(tempURI);
    	return localApiClient
				.get()
				.uri("products/" + id.toString())
				.retrieve()
				.bodyToMono(Product.class)
				.block(REQUEST_TIMEOUT);
	}
	
	public Product displayTempProduct(Exception e) {
		return new Product(20, "13th GeForce", "Samsung Galaxy Mini", "Samsung Laptop", "GENERIC STUFFS", "GENERIC STUFFS", 79.43, null);
	}
	
	@GetMapping("/getAllPaymentMethods")
	public List<Payment> allPaymentMethods(Integer id) throws MalformedURLException{
		servicePaymentMS = client.getInstances("PaymentMS").stream().map(services -> services.getUri())
				.findFirst().get().toURL();
		localApiClient = WebClient.create(servicePaymentMS.toString());
		
		return localApiClient
				.get()
				.uri("getAllPaymentMethods/" + id)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<Payment>>() {})
				//.bodyToFlux(Payment.class)	
				.block(REQUEST_TIMEOUT);
	}
	
	@PostMapping("/addCustomerPayment")
	public Payment addCustomerPayment(Payment payment) throws MalformedURLException {
		servicePaymentMS = client.getInstances("PaymentMS").stream().map(services -> services.getUri())
				.findFirst().get().toURL();
		localApiClient = WebClient.create(servicePaymentMS.toString());
		
		return localApiClient
				.post()
				.uri("/addCustomerPayment")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(payment)
				.retrieve()
				.bodyToMono(Payment.class)
				.block(REQUEST_TIMEOUT);
				
		
	}
	
	@PostConstruct
	public void initializeServiceMS() throws MalformedURLException {
		servicePaymentMS = client.getInstances("PaymentMS").stream().map(services -> services.getUri())
				.findFirst().get().toURL();
	}
	
	@Bean
    RestTemplate template() {
        return new RestTemplate();
    }
}
