package dio.budgeting.domain;

import java.util.UUID;

public record TransactionId(UUID value) {

    public TransactionId {
        if (value == null) {
            throw new GastoInvalido("Faltou o id do gasto.");
        }
    }

    public static TransactionId nova() {
        return new TransactionId(UUID.randomUUID());
    }
}
