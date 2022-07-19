package com.ekart.customerms.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ekart.customerms.dto.PaymentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentId;
	private Integer customerId;
	private String cardType;
	private String cardNumber;
	private Integer cvvNumber;
	private String cardName;
	private Date expDate;
	
	public Payment(PaymentDTO payment) {
		this.paymentId = payment.getPaymentId();
		this.customerId = payment.getCustomerId();
		this.cardType = payment.getCardType();
		this.cardNumber = payment.getCustomerCardNumber();
		this.cvvNumber = payment.getCvvNumber();
		this.cardName = payment.getCardName();
		this.expDate = payment.getExpDate();
	}
	
}
