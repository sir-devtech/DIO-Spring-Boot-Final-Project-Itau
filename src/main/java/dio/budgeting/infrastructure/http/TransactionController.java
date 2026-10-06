package dio.budgeting.infrastructure.http;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.application.SummarizeTransactionsUseCase;
import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.application.output.ResumoOutput;
import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;
    private final SummarizeTransactionsUseCase summarizeTransactionsUseCase;

    public TransactionController(
            PersistTransactionUseCase persistTransactionUseCase,
            ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
            SummarizeTransactionsUseCase summarizeTransactionsUseCase) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
        this.summarizeTransactionsUseCase = summarizeTransactionsUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionOutput create(@RequestBody PersistTransactionInput request) {
        return persistTransactionUseCase.execute(request);
    }

    @GetMapping("/resumo")
    public ResumoOutput resumo() {
        return summarizeTransactionsUseCase.execute();
    }

    @GetMapping("/{category}")
    public List<TransactionOutput> byCategory(@PathVariable Category category) {
        return listTransactionsByCategoryUseCase.execute(category);
    }
}
