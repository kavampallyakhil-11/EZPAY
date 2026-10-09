package com.ezpay.payment.controller;

import com.ezpay.payment.dto.BankTransferRequest;
import com.ezpay.payment.dto.UpiPaymentRequest;
import com.ezpay.payment.entity.Transaction;
import com.ezpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/upi")
    public ResponseEntity<Transaction> makeUpiPayment(
            @Valid @RequestBody UpiPaymentRequest request) {

        Transaction transaction =
                paymentService.processUpiPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    @PostMapping("/bank-transfer")
    public ResponseEntity<Transaction> makeBankTransfer(
            @Valid @RequestBody BankTransferRequest request) {

        Transaction transaction =
                paymentService.processBankTransfer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId) {
      Transaction transaction =
              paymentService.getTransaction(transactionId);

      return ResponseEntity.ok(transaction);
 }
    }
