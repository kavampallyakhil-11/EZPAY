package Service;

import dto.TransactionResponse;
import entity.Transaction;
import enums.TransactionStatus;
import enums.TransactionType;
import exception.TransactionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;

        logger.info("TransactionServiceImpl initialized");
    }

    @Override
    public List<TransactionResponse> getTransactions(Long userId) {

        logger.info(
                "Fetching transactions for userId={}",
                userId
        );

        List<Transaction> transactions =
                transactionRepository.findByUserId(userId);

        logger.info(
                "Repository returned {} transactions for userId={}",
                transactions.size(),
                userId
        );

        for (Transaction transaction : transactions) {

            logger.info(
                    "Transaction found: transactionId={}, userId={}, referenceNumber={}, status={}, type={}, amount={}",
                    transaction.getTransactionId(),
                    transaction.getUserId(),
                    transaction.getReferenceNumber(),
                    transaction.getStatus(),
                    transaction.getTransactionType(),
                    transaction.getAmount()
            );
        }

        List<TransactionResponse> responses =
                transactions.stream()
                        .map(this::convertToResponse)
                        .toList();

        logger.info(
                "Returning {} transaction responses for userId={}",
                responses.size(),
                userId
        );

        return responses;
    }

    @Override
    public TransactionResponse getTransaction(
            Long userId,
            Long transactionId) {

        logger.info(
                "Fetching transaction: transactionId={}, userId={}",
                transactionId,
                userId
        );

        Transaction transaction =
                transactionRepository
                        .findByTransactionIdAndUserId(
                                transactionId,
                                userId
                        )
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Transaction not found: transactionId={}, userId={}",
                                    transactionId,
                                    userId
                            );

                            return new TransactionNotFoundException(
                                    "Transaction not found"
                            );
                        });

        logger.info(
                "Transaction found: transactionId={}, userId={}, status={}",
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getStatus()
        );

        return convertToResponse(transaction);
    }

    @Override
    public TransactionResponse getTransactionStatus(
            Long userId,
            Long transactionId) {

        logger.info(
                "Fetching transaction status: transactionId={}, userId={}",
                transactionId,
                userId
        );

        Transaction transaction =
                transactionRepository
                        .findByTransactionIdAndUserId(
                                transactionId,
                                userId
                        )
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Transaction status lookup failed: transactionId={}, userId={}",
                                    transactionId,
                                    userId
                            );

                            return new TransactionNotFoundException(
                                    "Transaction not found"
                            );
                        });

        logger.info(
                "Transaction status found: transactionId={}, status={}",
                transaction.getTransactionId(),
                transaction.getStatus()
        );

        return convertToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> searchTransactions(
            Long userId,
            TransactionStatus status,
            TransactionType type) {

        logger.info(
                "Searching transactions: userId={}, status={}, type={}",
                userId,
                status,
                type
        );

        List<Transaction> transactions;

        if (status != null) {

            logger.info(
                    "Searching transactions by status: userId={}, status={}",
                    userId,
                    status
            );

            transactions =
                    transactionRepository
                            .findByUserIdAndStatus(
                                    userId,
                                    status
                            );

        } else if (type != null) {

            logger.info(
                    "Searching transactions by type: userId={}, type={}",
                    userId,
                    type
            );

            transactions =
                    transactionRepository
                            .findByUserIdAndTransactionType(
                                    userId,
                                    type
                            );

        } else {

            logger.info(
                    "No status or type filter provided. Fetching all transactions for userId={}",
                    userId
            );

            transactions =
                    transactionRepository.findByUserId(userId);
        }

        logger.info(
                "Search returned {} transactions for userId={}",
                transactions.size(),
                userId
        );

        List<TransactionResponse> responses =
                transactions.stream()
                        .map(this::convertToResponse)
                        .toList();

        logger.info(
                "Returning {} search responses for userId={}",
                responses.size(),
                userId
        );

        return responses;
    }

    private TransactionResponse convertToResponse(
            Transaction transaction) {

        logger.debug(
                "Converting transaction to response: transactionId={}",
                transaction.getTransactionId()
        );

        TransactionResponse response =
                new TransactionResponse();

        response.setTransactionId(
                transaction.getTransactionId()
        );

        response.setReferenceNumber(
                transaction.getReferenceNumber()
        );

        response.setTransactionType(
                transaction.getTransactionType()
        );

        response.setStatus(
                transaction.getStatus()
        );

        response.setAmount(
                transaction.getAmount()
        );

        response.setSender(
                transaction.getSender()
        );

        response.setRecipient(
                transaction.getRecipient()
        );

        response.setTransactionDate(
                transaction.getTransactionDate()
        );

        response.setDescription(
                transaction.getDescription()
        );

        logger.debug(
                "Transaction conversion completed: transactionId={}",
                transaction.getTransactionId()
        );

        return response;
    }
}