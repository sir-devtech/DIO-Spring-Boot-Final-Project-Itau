package dio.budgeting.application.input;

import dio.budgeting.domain.Category;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;

public record PersistTransactionInput(
        @ToolParam(description = "O que foi o gasto, em poucas palavras") String description,
        @ToolParam(description = "Valor em reais, não em centavos. Exemplo: 50.00") BigDecimal amount,
        @ToolParam(description = "GROCERIES mercado, PHARMA farmácia, AUTO carro, FOOD café ou restaurante") Category category) {
}
