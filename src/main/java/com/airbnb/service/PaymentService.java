package com.airbnb.service;

import java.util.List;

import com.airbnb.entity.Payment;

public interface PaymentService {

	String makePayment(Payment payment);

	Payment getPayment(Long id);

	List<Payment> paymentHistory(Long userId);

	String deletePayment(Long id);

	List<Payment> getAllPayments();
	
	List<Payment> getPaymentsByHost(Long hostUserId);

}
