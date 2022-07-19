package com.ekart.customerms.dto;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ekart.customerms.entity.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {

	private Integer paymentId;
	private Integer customerId;
	private String cardType;
	private String customerCardNumber;
	@NotNull(message = "CVV must not be empty")
	@NotBlank(message = "CVV must not be blank")
	private Integer cvvNumber;
	@NotBlank(message = "Card Name must not be blank")
	private String cardName;
	private Date expDate;
	
	public PaymentDTO(Payment payment) {
		this.paymentId = payment.getPaymentId();
		this.customerId = payment.getCustomerId();
		this.cardType = payment.getCardType();
		this.customerCardNumber = payment.getCardNumber();
		this.cvvNumber = payment.getCvvNumber();
		this.cardName = payment.getCardName();
		this.expDate = payment.getExpDate();
	}
}
