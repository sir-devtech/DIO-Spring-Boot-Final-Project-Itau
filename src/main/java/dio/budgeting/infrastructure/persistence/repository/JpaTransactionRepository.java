package dio.budgeting.infrastructure.persistence.repository;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTransactionRepository implements TransactionRepository {

    private final TransactionEntityRepository entities;

    public JpaTransactionRepository(TransactionEntityRepository entities) {
        this.entities = entities;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return entities.save(TransactionEntity.from(transaction)).toDomain();
    }

    @Override
    public List<Transaction> findAll() {
        return entities.findAll().stream().map(TransactionEntity::toDomain).toList();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return entities.findAllByCategory(category).stream().map(TransactionEntity::toDomain).toList();
    }
}
