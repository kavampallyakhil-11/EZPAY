package dto;

import enums.TransactionStatus;
import enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionResponse {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionResponse.class);

    private Long transactionId;
    private String referenceNumber;
    private TransactionType transactionType;
    private TransactionStatus status;
    private BigDecimal amount;
    private String sender;
    private String recipient;
    private LocalDateTime transactionDate;
    private String description;

    public TransactionResponse() {
        logger.debug("TransactionResponse object created");
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
        logger.debug("TransactionResponse transactionId set to {}", transactionId);
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        logger.debug("TransactionResponse referenceNumber set to {}", referenceNumber);
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        logger.debug("TransactionResponse transactionType set to {}", transactionType);
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
        logger.debug("TransactionResponse status set to {}", status);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        logger.debug("TransactionResponse amount set to {}", amount);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
        logger.debug("TransactionResponse sender set to {}", sender);
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
        logger.debug("TransactionResponse recipient set to {}", recipient);
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
        logger.debug("TransactionResponse transactionDate set to {}", transactionDate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        logger.debug("TransactionResponse description set to {}", description);
    }
}