package com.ekart.customerms.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ekart.customerms.dto.CartDTO;
import com.ekart.customerms.dto.CustomerDTO;
import com.ekart.customerms.dto.PaymentDTO;
import com.ekart.customerms.entity.Cart;
import com.ekart.customerms.entity.Customer;
import com.ekart.customerms.entity.Payment;
import com.ekart.customerms.entity.Product;
import com.ekart.customerms.repository.ProductRepository;
import com.ekart.customerms.service.CartService;
import com.ekart.customerms.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Controller
public class CustomerMSController {

	@Autowired
	CustomerMSRestController restController;
	
	@Autowired
	Environment environment;
	
	@Autowired
	CustomerService service;
	
	@Autowired
	CartService cartService;
	
	
	@Autowired
	ProductRepository productRepository;
	
	//private ProductDTO product;
	private CustomerDTO customer;
	private CartDTO cart;
	private List<Product> productsList;
	
	private String maskCardNumber = " XXXX XXXX";
	private final String FALL_HOME = "loginService";
	
	@CircuitBreaker(name = FALL_HOME, fallbackMethod = "fallBackWebPage")
	@GetMapping("/")
	String index() {
		return "main";
	}
	
	public String fallBackWebPage(Exception e) {
		return "down";
	}
	
	@GetMapping("/registration")
	String registration(Model model) {
		model.addAttribute("register", new CustomerDTO());
		return "registration";
	}
	
	@GetMapping("/loginCustomer")
	public String login() {
		return "login";
	}

	public Integer count=0;
	
	
	
	
	@GetMapping("/home")
	public String home(Model model) throws StreamReadException, DatabindException, MalformedURLException, IOException {
		List<Product> productList= restController.invokeGetProductsService();
		model.addAttribute("productsList", productList);
		model.addAttribute("customer", customer);
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);

		return "home";
	}
	
	@GetMapping("/cart")
	public String cartPage(Model model) throws MalformedURLException {
		model.addAttribute("productsList", productsList);
		model.addAttribute("customer", customer);
		
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);
		List<Payment> tempPayments =  allPaymentMethods(customer.getCustomer_id());
		tempPayments.forEach(tempPayment -> {
			tempPayment.setCardNumber(tempPayment.getCardNumber().substring(1, 4) + maskCardNumber);
		});
		model.addAttribute("paymentMethods", tempPayments);
		restController.callURI();
		return "cart";
	}
	
	@GetMapping("/register")
    public String errorForm(CustomerDTO loginDTO) {
        return "registration";
    }
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute CustomerDTO registerDTO, BindingResult bindingResult, Model model ) {
		model.addAttribute("register", registerDTO);
		List<String> fieldErrors = new ArrayList<>();
		
		if(bindingResult.hasErrors()) {
			registerDTO = restController.verifyRegisterDetails(registerDTO, bindingResult);
			for(FieldError errors: bindingResult.getFieldErrors()) {
				fieldErrors.add(errors.getDefaultMessage());
				model.addAttribute("errors", fieldErrors);
			}
			return "registration";
		}
		fieldErrors.add("Account has been created");
		model.addAttribute("errors", fieldErrors);
		Customer register = new Customer(registerDTO); 
		service.registerCustomer(register);
		setFieldsToNull(registerDTO);
		return "registration";
	}
	
	public CustomerDTO setFieldsToNull(CustomerDTO registerDTO) {
		registerDTO.setPassword(null);
		registerDTO.setEmail(null);
		registerDTO.setLastName(null);
		registerDTO.setFirstName(null);
		registerDTO.setPhoneNumber(null);
		return registerDTO;
	}
	
	
	@PostMapping("/login")
	public String login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password, Model model) throws StreamReadException, DatabindException, MalformedURLException, IOException {
		
		List<String> fieldErrors = new ArrayList<>();
		Customer customer = service.login(email, password);
		customer = (customer == null)? new Customer(): customer;
		CustomerDTO customerDTO = new CustomerDTO(customer);
		if(customer.getCustomerId() == null) {
			fieldErrors.add("Username/Password Incorrect");
			
			model.addAttribute("errors", fieldErrors);
			return "login";
		} 
		List<Product> productList= restController.invokeGetProductsService();
		model.addAttribute("products", productList);
		model.addAttribute("customer", customer);
		
		model.addAttribute("errors", fieldErrors);
		this.customer = customerDTO;
		this.productsList = productList;
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);
		return "home";
	}
	
	@PostMapping("/addToCart")
	public String addToCart(@RequestParam(value = "productId") Integer id, Model model ) throws MalformedURLException {
		model.addAttribute("productsList", productsList);
		model.addAttribute("customer", customer);
		Customer customerTemp = new Customer(this.customer);
		this.cart = new CartDTO();
		this.cart.setCustomer(customerTemp);
		Cart cartExists = cartService.
				OrderExists(customer.getCustomer_id(), id);
		if(cartExists.getCount()==null) {
			cartExists.setCount(1);
			cartExists.setCustomer_id(this.customer.getCustomer_id());
			cartExists.setProduct_id(id);
			cartExists.setIsPaid(0);
			cartService.SaveToCart(cartExists);
		}
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);
		List<Payment> tempPayments =  allPaymentMethods(customer.getCustomer_id());
		tempPayments.forEach(tempPayment -> {
			tempPayment.
			setCardNumber(tempPayment.getCardNumber().substring(1, 4) + maskCardNumber);
		});
		model.addAttribute("paymentMethods", tempPayments);
		return "cart";
	}
	
	
	@PostMapping("/delete")
	public String deleteCartProduct(@RequestParam(value = "id") Integer id, Model model) throws MalformedURLException {
		model.addAttribute("productsList", productsList);
		model.addAttribute("customer", customer);
		Cart cart = cartService.retrieveCart(id);
		if(cart.getCount() == 1) {
			cartService.deleteCartProduct(id);
		}else {
			cartService.reduceCartProduct(id);
		}
		
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);
		List<Payment> tempPayments =  allPaymentMethods(customer.getCustomer_id());
		tempPayments.forEach(tempPayment -> {
			tempPayment
			.setCardNumber(tempPayment.getCardNumber().substring(1, 4) + maskCardNumber);
		});
		model.addAttribute("paymentMethods", tempPayments);
		return "cart";
	}
	
	
	@PostMapping("/addPayment")
	public String addPayment(@ModelAttribute PaymentDTO payment, BindingResult bindingResult, 
			@RequestParam(name = "customerCardNumber") String cardNumber, 
			@RequestParam(name = "tempExpDate") String expDate, 
			BindingResult paramBindingResult, Model model) throws ParseException, MalformedURLException, JsonProcessingException {
		
		List<String> fieldErrors = new ArrayList<>();
		model.addAttribute("productsList", productsList);
		model.addAttribute("customer", customer);
		
		payment.setCvvNumber(payment.getCvvNumber());
		System.out.println("Date:" + expDate);
		System.out.println("Date:" + payment.getExpDate());
		if(expDate == null || expDate == "") {
			expDate = "2025-07-09"; 
			String errObj = "";
			ObjectError error = new ObjectError(errObj, "DATE MUST NOT BE EMPTY");
			bindingResult.addError(error);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = dateFormat.parse(expDate);
		payment.setExpDate(parsedDate);
		payment.setCustomerCardNumber(cardNumber);
		payment.setCustomerId(customer.getCustomer_id());
		Payment newPayment = new Payment(payment);
		if(payment.getCvvNumber() == null || payment.getCvvNumber().equals(null)) {
			String errObj = "";
			ObjectError error = new ObjectError(errObj, "CVV MUST NOT BE EMPTY");
			bindingResult.addError(error);
		}
		
		if(bindingResult.hasErrors()) {
			for(FieldError errors: bindingResult.getFieldErrors()) {
				fieldErrors.add(errors.getDefaultMessage());
				System.out.println(errors.getDefaultMessage());
			}
			model.addAttribute("errors", fieldErrors);
			return "cart";
		}
		System.out.println("NEW PAYMENTTT: " + newPayment);
		restController.addCustomerPayment(newPayment);
		
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);
		List<Payment> tempPayments =  allPaymentMethods(customer.getCustomer_id());
		tempPayments.forEach(tempPayment -> {
			tempPayment
			.setCardNumber(tempPayment.getCardNumber().substring(1, 4) + maskCardNumber);
		});
		
		model.addAttribute("paymentMethods", tempPayments);
		
		return "cart";
	}
	
	@PostMapping("/payCart")
	public String payCart(Model model) throws MalformedURLException {
		List<Cart> tempCart = new ArrayList<>();
		List<CartDTO> tempCartDTO = getCustomerCart();
		tempCartDTO.forEach(product -> {
			Cart newCart = new Cart(product);
			newCart.setCount(product.getCount());
			newCart.setCustomer_id(customer.getCustomer_id());
			newCart.setProduct_id(product.getProduct().getId());
			tempCart.add(newCart);
		});
		cartService.payCartProducts(customer.getCustomer_id(), tempCart);
		List<Payment> tempPayments =  allPaymentMethods(customer.getCustomer_id());
		tempPayments.forEach(tempPayment -> {
			tempPayment
			.setCardNumber(tempPayment.getCardNumber().substring(1, 4) + maskCardNumber);
		});
		List<CartDTO> customerCartDTO = getCustomerCart();
		customerCartDTO = (customerCartDTO.isEmpty()) ?null : customerCartDTO;
		model.addAttribute("customerCart", customerCartDTO);
		model.addAttribute("productsList", productsList);
		model.addAttribute("customer", customer);
		return "cart";
	}
	
	public List<Payment> allPaymentMethods(Integer customerId) throws MalformedURLException {
		return restController.allPaymentMethods(customerId);
	} 
	
	public List<CartDTO> getCustomerCart() {
		List<Cart> customerCart = cartService.CustomerCart(customer.getCustomer_id());
		List<CartDTO> customerCartDTO = new ArrayList<>();
		customerCart.forEach(cartProduct -> {
			CartDTO cartDTO = new CartDTO();
			cartDTO.setCartId(cartProduct.getId());
			
			Product tempProduct = restController.getProductById(cartProduct.getProduct_id());
			cartDTO.setProduct(tempProduct);
			cartDTO.setCount(cartProduct.getCount());
			customerCartDTO.add(cartDTO); 
		});
		return customerCartDTO;
	}
}
