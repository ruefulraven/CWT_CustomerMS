package com.ekart.customerms.dto;

import java.util.List;

import com.ekart.customerms.entity.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentList {

	private List<Payment> PaymentList;
}
