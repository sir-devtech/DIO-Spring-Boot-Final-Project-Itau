package dio.budgeting.application.output;

import dio.budgeting.domain.Category;

import java.math.BigDecimal;
import java.util.Map;

public record ResumoOutput(BigDecimal total, int quantidade, Map<Category, BigDecimal> porCategoria) {
}
