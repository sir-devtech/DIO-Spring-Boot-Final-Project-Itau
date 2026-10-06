package dio.budgeting.application;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SummarizeTransactionsUseCaseTest {

    @Test
    void somaTotalESeparaPorCategoria() {
        var memoria = new Memoria();
        memoria.save(Transaction.nova("Starbucks", new BigDecimal("50"), Category.FOOD));
        memoria.save(Transaction.nova("feira", new BigDecimal("20.5"), Category.GROCERIES));

        var resumo = new SummarizeTransactionsUseCase(memoria).execute();

        assertEquals(new BigDecimal("70.50"), resumo.total());
        assertEquals(2, resumo.quantidade());
        assertEquals(new BigDecimal("50.00"), resumo.porCategoria().get(Category.FOOD));
        assertEquals(new BigDecimal("20.50"), resumo.porCategoria().get(Category.GROCERIES));
        assertEquals(new BigDecimal("0.00"), resumo.porCategoria().get(Category.AUTO));
    }

    @Test
    void cadernoVazioAindaResponde() {
        var resumo = new SummarizeTransactionsUseCase(new Memoria()).execute();
        assertEquals(new BigDecimal("0.00"), resumo.total());
        assertEquals(0, resumo.quantidade());
    }

    private static final class Memoria implements TransactionRepository {
        private final List<Transaction> itens = new ArrayList<>();

        @Override
        public Transaction save(Transaction transaction) {
            itens.add(transaction);
            return transaction;
        }

        @Override
        public List<Transaction> findAll() {
            return List.copyOf(itens);
        }

        @Override
        public List<Transaction> findAllByCategory(Category category) {
            return itens.stream().filter(item -> item.category() == category).toList();
        }
    }
}
