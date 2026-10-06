package dio.budgeting.application.output;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionOutput(UUID id, String description, BigDecimal amount, Category category) {

    public static TransactionOutput from(Transaction transaction) {
        return new TransactionOutput(
                transaction.id().value(),
                transaction.description(),
                transaction.amount(),
                transaction.category());
    }
}
