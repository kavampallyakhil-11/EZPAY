package Service;

import dto.TransactionResponse;
import enums.TransactionStatus;
import enums.TransactionType;

import java.util.List;

public interface TransactionService {

    List<TransactionResponse> getTransactions(Long userId);

    TransactionResponse getTransaction(
            Long userId,
            Long transactionId
    );

    TransactionResponse getTransactionStatus(
            Long userId,
            Long transactionId
    );

    List<TransactionResponse> searchTransactions(
            Long userId,
            TransactionStatus status,
            TransactionType type
    );
}