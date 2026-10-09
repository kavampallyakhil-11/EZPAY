
package controller;

import Service.TransactionService;
import dto.TransactionResponse;
import enums.TransactionStatus;
import enums.TransactionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final JdbcTemplate jdbcTemplate;

    public TransactionController(
            TransactionService transactionService,
            JdbcTemplate jdbcTemplate) {

        this.transactionService = transactionService;
        this.jdbcTemplate = jdbcTemplate;

        logger.info("TransactionController initialized");
    }

    // ==========================================
    // GET ALL TRANSACTIONS
    // ==========================================

    @GetMapping
    public List<TransactionResponse> getTransactions(
            @RequestParam Long userId) {

        logger.info(
                "GET /api/transactions called with userId={}",
                userId
        );

        List<TransactionResponse> transactions =
                transactionService.getTransactions(userId);

        logger.info(
                "Controller received {} transactions for userId={}",
                transactions.size(),
                userId
        );

        return transactions;
    }

    // ==========================================
    // DATABASE DIAGNOSTIC
    // ==========================================

    @GetMapping("/database-check")
    public String databaseCheck() {

        logger.info("Running database diagnostic");

        String databaseUser =
                jdbcTemplate.queryForObject(
                        "SELECT USER FROM dual",
                        String.class
                );

        Integer transactionCount =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM transactions WHERE user_id = 1",
                        Integer.class
                );

        String databaseName =
                jdbcTemplate.queryForObject(
                        "SELECT SYS_CONTEXT('USERENV', 'DB_NAME') FROM dual",
                        String.class
                );

        String serviceName =
                jdbcTemplate.queryForObject(
                        "SELECT SYS_CONTEXT('USERENV', 'SERVICE_NAME') FROM dual",
                        String.class
                );

        logger.info(
                "Database user={}",
                databaseUser
        );

        logger.info(
                "Database name={}",
                databaseName
        );

        logger.info(
                "Service name={}",
                serviceName
        );

        logger.info(
                "Transaction count for userId=1={}",
                transactionCount
        );

        return "Database User: " + databaseUser
                + "\nDatabase Name: " + databaseName
                + "\nService Name: " + serviceName
                + "\nTransactions for userId=1: " + transactionCount;
    }

    // ==========================================
    // SEARCH TRANSACTIONS
    // ==========================================

    @GetMapping("/search")
    public List<TransactionResponse> searchTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) TransactionType type) {

        logger.info(
                "GET /api/transactions/search: userId={}, status={}, type={}",
                userId,
                status,
                type
        );

        List<TransactionResponse> transactions =
                transactionService.searchTransactions(
                        userId,
                        status,
                        type
                );

        logger.info(
                "Search returned {} transactions for userId={}",
                transactions.size(),
                userId
        );

        return transactions;
    }

    // ==========================================
    // GET SINGLE TRANSACTION
    // ==========================================

    @GetMapping("/{transactionId}")
    public TransactionResponse getTransaction(
            @RequestParam Long userId,
            @PathVariable Long transactionId) {

        logger.info(
                "GET /api/transactions/{} called with userId={}",
                transactionId,
                userId
        );

        return transactionService.getTransaction(
                userId,
                transactionId
        );
    }

    // ==========================================
    // GET TRANSACTION STATUS
    // ==========================================

    @GetMapping("/{transactionId}/status")
    public TransactionResponse getTransactionStatus(
            @RequestParam Long userId,
            @PathVariable Long transactionId) {

        logger.info(
                "GET /api/transactions/{}/status called with userId={}",
                transactionId,
                userId
        );

        return transactionService.getTransactionStatus(
                userId,
                transactionId
        );
    }
}

