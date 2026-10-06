package dio.budgeting.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Transaction(
        TransactionId id,
        String description,
        BigDecimal amount,
        Category category) {

    public Transaction {
        if (description == null || description.isBlank()) {
            throw new GastoInvalido("Faltou a descrição.");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new GastoInvalido("Valor tem que ser maior que zero.");
        }
        if (category == null) {
            throw new GastoInvalido("Faltou a categoria.");
        }
        description = description.trim();
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Transaction nova(String description, BigDecimal amount, Category category) {
        return new Transaction(TransactionId.nova(), description, amount, category);
    }
}
