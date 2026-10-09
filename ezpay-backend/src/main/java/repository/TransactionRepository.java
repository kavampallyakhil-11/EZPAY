package repository;

import entity.Transaction;
import enums.TransactionStatus;
import enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    Optional<Transaction> findByTransactionIdAndUserId(
            Long transactionId,
            Long userId
    );

    List<Transaction> findByUserIdAndStatus(
            Long userId,
            TransactionStatus status
    );

    List<Transaction> findByUserIdAndTransactionType(
            Long userId,
            TransactionType transactionType
    );
}