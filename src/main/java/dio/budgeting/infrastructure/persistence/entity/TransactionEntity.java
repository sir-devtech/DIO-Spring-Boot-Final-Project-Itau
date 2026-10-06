package dio.budgeting.infrastructure.persistence.entity;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transaction_entry")
public class TransactionEntity {

    @Id
    private UUID id;

    private String description;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Category category;

    protected TransactionEntity() {
    }

    public static TransactionEntity from(Transaction transaction) {
        var entity = new TransactionEntity();
        entity.id = transaction.id().value();
        entity.description = transaction.description();
        entity.amount = transaction.amount();
        entity.category = transaction.category();
        return entity;
    }

    public Transaction toDomain() {
        return new Transaction(new TransactionId(id), description, amount, category);
    }
}
