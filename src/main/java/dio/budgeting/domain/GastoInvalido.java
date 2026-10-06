package dio.budgeting.domain;

public class GastoInvalido extends RuntimeException {

    public GastoInvalido(String mensagem) {
        super(mensagem);
    }
}
