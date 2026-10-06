package dio.budgeting.application;

import dio.budgeting.application.output.ResumoOutput;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

@Service
public class SummarizeTransactionsUseCase {

    private final TransactionRepository transactionRepository;

    public SummarizeTransactionsUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "summarize-transactions", description = "Soma o que já foi gasto, no total e por categoria")
    public ResumoOutput execute() {
        var porCategoria = new EnumMap<Category, BigDecimal>(Category.class);
        for (Category category : Category.values()) {
            porCategoria.put(category, dinheiro(BigDecimal.ZERO));
        }

        var gastos = transactionRepository.findAll();
        var total = BigDecimal.ZERO;
        for (Transaction gasto : gastos) {
            total = total.add(gasto.amount());
            porCategoria.merge(gasto.category(), gasto.amount(), BigDecimal::add);
        }

        porCategoria.replaceAll((categoria, valor) -> dinheiro(valor));
        return new ResumoOutput(dinheiro(total), gastos.size(), Map.copyOf(porCategoria));
    }

    private static BigDecimal dinheiro(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }
}
