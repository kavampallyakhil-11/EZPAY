package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionNotFoundException.class);

    public TransactionNotFoundException(String message) {
        super(message);

        logger.warn("Transaction not found: {}", message);
    }
}