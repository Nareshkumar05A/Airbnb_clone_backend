package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.airbnb.entity.Payment;
import com.airbnb.service.PaymentService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentServ;

    @PostMapping("/pay")
    public String pay(@RequestBody Payment payment){

    	
        return paymentServ.makePayment(payment);
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable Long id){

        return paymentServ.getPayment(id);
    }

    @GetMapping("/all")
    public List<Payment> getAllPayments(){

        return paymentServ.getAllPayments();
    }

    @GetMapping("/user/{userId}")
    public List<Payment> paymentHistory(@PathVariable Long userId){

        return paymentServ.paymentHistory(userId);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id){

        return paymentServ.deletePayment(id);
    }

}