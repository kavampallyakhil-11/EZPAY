package com.ezpay.payment.service;

import com.ezpay.payment.dto.BankTransferRequest;
import com.ezpay.payment.dto.UpiPaymentRequest;
import com.ezpay.payment.entity.Transaction;
import com.ezpay.payment.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final TransactionRepository transactionRepository;

    public PaymentService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    public Transaction getTransaction(String transactionId) {

        return transactionRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));
    }
    @Transactional
    public Transaction processUpiPayment(UpiPaymentRequest request) {

        Transaction transaction = new Transaction();

        transaction.setTransactionId(generateTransactionId());
        transaction.setUserId(request.getUserId());
        transaction.setPaymentType("UPI");
        transaction.setRecipient(request.getRecipientUpiId());
        transaction.setAmount(request.getAmount());
        transaction.setPurpose(request.getNote());
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction processBankTransfer(BankTransferRequest request) {

        Transaction transaction = new Transaction();

        transaction.setTransactionId(generateTransactionId());
        transaction.setUserId(request.getUserId());
        transaction.setPaymentType("BANK_TRANSFER");
        transaction.setRecipient(
                request.getAccountNumber() + "/" + request.getIfscCode()
        );
        transaction.setAmount(request.getAmount());
        transaction.setPurpose(request.getPurpose());
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    private String generateTransactionId() {
        return "EZP-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}