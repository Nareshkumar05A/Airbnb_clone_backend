package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.airbnb.entity.Booking;
import com.airbnb.entity.BookingStatus;
import com.airbnb.entity.Payment;
import com.airbnb.entity.PaymentStatus;
import com.airbnb.entity.PaymentMethod;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private BookingRepository bookingRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Booking booking;
    private Payment payment;

    @BeforeEach
    void setUp() {

        booking = new Booking();

        booking.setBookingId(100L);
        booking.setTotalPrice(6000);
        booking.setBookingStatus(null);

        payment = new Payment();

        payment.setPaymentId(1L);
        payment.setBooking(booking);
        payment.setPaymentMethod(PaymentMethod.values()[0]);
    }

    @Test
    void makePayment_success() {

        when(bookingRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        when(paymentRepo.existsByBooking(booking))
                .thenReturn(false);

        when(paymentRepo.save(payment))
                .thenReturn(payment);

        String result =
                paymentService.makePayment(payment);

        assertEquals(
                "Payment Successful",
                result
        );

        assertEquals(
                6000,
                payment.getAmount()
        );

        assertEquals(
                PaymentStatus.SUCCESS,
                payment.getPaymentStatus()
        );

        assertEquals(
                LocalDate.now(),
                payment.getPaymentDate()
        );

        assertEquals(
                BookingStatus.CONFIRMED,
                booking.getBookingStatus()
        );

        verify(bookingRepo).findById(100L);
        verify(paymentRepo).existsByBooking(booking);
        verify(bookingRepo).save(booking);
        verify(paymentRepo).save(payment);
    }

    @Test
    void makePayment_bookingNotFound_shouldReturnError() {

        when(bookingRepo.findById(100L))
                .thenReturn(Optional.empty());

        String result =
                paymentService.makePayment(payment);

        assertEquals(
                "Booking Not Found",
                result
        );

        verify(bookingRepo).findById(100L);
        verify(paymentRepo, never()).existsByBooking(any());
        verify(bookingRepo, never()).save(any());
        verify(paymentRepo, never()).save(any());
    }

    @Test
    void makePayment_cancelledBooking_shouldReturnError() {

        booking.setBookingStatus(
                BookingStatus.CANCELLED
        );

        when(bookingRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        String result =
                paymentService.makePayment(payment);

        assertEquals(
                "Cancelled Booking Cannot Be Paid",
                result
        );

        verify(bookingRepo).findById(100L);
        verify(paymentRepo, never()).existsByBooking(any());
        verify(bookingRepo, never()).save(any());
        verify(paymentRepo, never()).save(any());
    }

    @Test
    void makePayment_confirmedBooking_shouldReturnError() {

        booking.setBookingStatus(
                BookingStatus.CONFIRMED
        );

        when(bookingRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        String result =
                paymentService.makePayment(payment);

        assertEquals(
                "Booking Already Confirmed",
                result
        );

        verify(bookingRepo).findById(100L);
        verify(paymentRepo, never()).existsByBooking(any());
        verify(bookingRepo, never()).save(any());
        verify(paymentRepo, never()).save(any());
    }

    @Test
    void makePayment_paymentAlreadyCompleted_shouldReturnError() {

        booking.setBookingStatus(null);

        when(bookingRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        when(paymentRepo.existsByBooking(booking))
                .thenReturn(true);

        String result =
                paymentService.makePayment(payment);

        assertEquals(
                "Payment Already Completed",
                result
        );

        verify(bookingRepo).findById(100L);
        verify(paymentRepo).existsByBooking(booking);
        verify(bookingRepo, never()).save(any());
        verify(paymentRepo, never()).save(any());
    }

    @Test
    void makePayment_paymentMethodMissing_shouldReturnError() {

        booking.setBookingStatus(null);

        payment.setPaymentMethod(null);

        when(bookingRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        when(paymentRepo.existsByBooking(booking))
                .thenReturn(false);

        String result =
                paymentService.makePayment(payment);

        assertEquals(
                "Select Payment Method",
                result
        );

        verify(bookingRepo).findById(100L);
        verify(paymentRepo).existsByBooking(booking);
        verify(bookingRepo, never()).save(any());
        verify(paymentRepo, never()).save(any());
    }

    @Test
    void getPayment_existingPayment_shouldReturnPayment() {

        when(paymentRepo.findById(1L))
                .thenReturn(Optional.of(payment));

        Payment result =
                paymentService.getPayment(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getPaymentId()
        );

        verify(paymentRepo).findById(1L);
    }

    @Test
    void getPayment_paymentNotFound_shouldReturnNull() {

        when(paymentRepo.findById(100L))
                .thenReturn(Optional.empty());

        Payment result =
                paymentService.getPayment(100L);

        assertNull(result);

        verify(paymentRepo).findById(100L);
    }

    @Test
    void getAllPayments_shouldReturnPayments() {

        Payment payment2 = new Payment();

        payment2.setPaymentId(2L);

        List<Payment> payments =
                Arrays.asList(payment, payment2);

        when(paymentRepo.findAll())
                .thenReturn(payments);

        List<Payment> result =
                paymentService.getAllPayments();

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                payments,
                result
        );

        verify(paymentRepo).findAll();
    }

    @Test
    void getAllPayments_noPayments_shouldReturnEmptyList() {

        when(paymentRepo.findAll())
                .thenReturn(Collections.emptyList());

        List<Payment> result =
                paymentService.getAllPayments();

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(paymentRepo).findAll();
    }

    @Test
    void paymentHistory_shouldReturnUserPayments() {

        Long userId = 10L;

        List<Payment> payments =
                Collections.singletonList(payment);

        when(paymentRepo.findByBookingUserUId(userId))
                .thenReturn(payments);

        List<Payment> result =
                paymentService.paymentHistory(userId);

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                payments,
                result
        );

        verify(paymentRepo)
                .findByBookingUserUId(userId);
    }

    @Test
    void paymentHistory_noPayments_shouldReturnEmptyList() {

        Long userId = 100L;

        when(paymentRepo.findByBookingUserUId(userId))
                .thenReturn(Collections.emptyList());

        List<Payment> result =
                paymentService.paymentHistory(userId);

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(paymentRepo)
                .findByBookingUserUId(userId);
    }

    @Test
    void deletePayment_success_shouldDeletePayment() {

        when(paymentRepo.findById(1L))
                .thenReturn(Optional.of(payment));

        String result =
                paymentService.deletePayment(1L);

        assertEquals(
                "Payment Deleted",
                result
        );

        verify(paymentRepo).findById(1L);
        verify(paymentRepo).delete(payment);
    }

    @Test
    void deletePayment_paymentNotFound_shouldReturnError() {

        when(paymentRepo.findById(100L))
                .thenReturn(Optional.empty());

        String result =
                paymentService.deletePayment(100L);

        assertEquals(
                "Payment Not Found",
                result
        );

        verify(paymentRepo).findById(100L);
        verify(paymentRepo, never()).delete(any());
    }

    @Test
    void getPaymentsByHost_shouldReturnPayments() {

        Long hostUserId = 20L;

        List<Payment> payments =
                Collections.singletonList(payment);

        when(paymentRepo.findByBookingPropertyUserUId(hostUserId))
                .thenReturn(payments);

        List<Payment> result =
                paymentService.getPaymentsByHost(hostUserId);

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                payments,
                result
        );

        verify(paymentRepo)
                .findByBookingPropertyUserUId(hostUserId);
    }

    @Test
    void getPaymentsByHost_noPayments_shouldReturnEmptyList() {

        Long hostUserId = 200L;

        when(paymentRepo.findByBookingPropertyUserUId(hostUserId))
                .thenReturn(Collections.emptyList());

        List<Payment> result =
                paymentService.getPaymentsByHost(hostUserId);

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(paymentRepo)
                .findByBookingPropertyUserUId(hostUserId);
    }
}