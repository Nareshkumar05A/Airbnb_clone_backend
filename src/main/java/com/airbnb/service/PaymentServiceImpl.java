package com.airbnb.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.entity.Booking;
import com.airbnb.entity.BookingStatus;
import com.airbnb.entity.Payment;
import com.airbnb.entity.PaymentStatus;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Override
    public String makePayment(Payment payment) {

        Booking booking = bookingRepo.findById(
                payment.getBooking().getBookingId()).orElse(null);

        if (booking == null)
            return "Booking Not Found";

        if (booking.getBookingStatus() == BookingStatus.CANCELLED)
            return "Cancelled Booking Cannot Be Paid";

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED)
            return "Booking Already Confirmed";

        if (paymentRepo.existsByBooking(booking))
            return "Payment Already Completed";

        if (payment.getPaymentMethod() == null)
            return "Select Payment Method";

        payment.setAmount(booking.getTotalPrice());

        payment.setPaymentDate(LocalDate.now());

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        payment.setBooking(booking);

        booking.setBookingStatus(BookingStatus.CONFIRMED);

        bookingRepo.save(booking);

        paymentRepo.save(payment);

        return "Payment Successful";
    }

    @Override
    public Payment getPayment(Long paymentId) {

        return paymentRepo.findById(paymentId).orElse(null);
    }

    @Override
    public List<Payment> getAllPayments() {

        return paymentRepo.findAll();
    }

    @Override
    public List<Payment> paymentHistory(Long userId) {

        return paymentRepo.findByBookingUserUId(userId);
    }

    @Override
    public String deletePayment(Long paymentId) {

        Payment payment = paymentRepo.findById(paymentId).orElse(null);

        if (payment == null)
            return "Payment Not Found";

        paymentRepo.delete(payment);

        return "Payment Deleted";
    }
    
    @Override
    public List<Payment> getPaymentsByHost(Long hostUserId) {
        return paymentRepo.findByBookingPropertyUserUId(hostUserId);
    }

}