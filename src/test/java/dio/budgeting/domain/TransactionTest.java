package dio.budgeting.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void recusaValorZero() {
        var erro = assertThrows(GastoInvalido.class,
                () -> Transaction.nova("nada", BigDecimal.ZERO, Category.FOOD));
        assertEquals("Valor tem que ser maior que zero.", erro.getMessage());
    }

    @Test
    void recusaDescricaoVazia() {
        assertThrows(GastoInvalido.class,
                () -> Transaction.nova("  ", new BigDecimal("10"), Category.GROCERIES));
    }

    @Test
    void guardaComDuasCasas() {
        var gasto = Transaction.nova("Starbucks", new BigDecimal("50"), Category.FOOD);
        assertEquals(new BigDecimal("50.00"), gasto.amount());
        assertEquals("Starbucks", gasto.description());
    }
}
