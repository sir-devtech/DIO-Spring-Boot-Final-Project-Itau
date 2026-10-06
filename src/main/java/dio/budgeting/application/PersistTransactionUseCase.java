package dio.budgeting.application;

import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class PersistTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "persist-transaction", description = "Grava um gasto. Recusa valor zero ou negativo.")
    public TransactionOutput execute(PersistTransactionInput input) {
        var transaction = Transaction.nova(input.description(), input.amount(), input.category());
        return TransactionOutput.from(transactionRepository.save(transaction));
    }
}
