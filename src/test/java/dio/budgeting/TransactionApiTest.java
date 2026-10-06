package dio.budgeting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionApiTest {

    @LocalServerPort
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void zeroNaoEntraEResumoSomaOQueEntrou() throws Exception {
        var recusado = post("/transactions", pedido("nada", "0", "FOOD"));
        assertEquals(400, recusado.statusCode());
        assertEquals("Valor tem que ser maior que zero.", mapa(recusado.body()).get("erro"));

        assertEquals(201, post("/transactions", pedido("Starbucks", "50", "FOOD")).statusCode());
        assertEquals(201, post("/transactions", pedido("oficina", "300", "AUTO")).statusCode());

        var resumo = mapa(get("/transactions/resumo").body());
        assertEquals(0, new BigDecimal("350.00").compareTo(new BigDecimal(resumo.get("total").toString())));
        assertEquals(2, ((Number) resumo.get("quantidade")).intValue());

        @SuppressWarnings("unchecked")
        var porCategoria = (Map<String, Object>) resumo.get("porCategoria");
        assertEquals(0, new BigDecimal("50.00").compareTo(new BigDecimal(porCategoria.get("FOOD").toString())));
        assertEquals(0, new BigDecimal("300.00").compareTo(new BigDecimal(porCategoria.get("AUTO").toString())));

        var lista = JsonParserFactory.getJsonParser().parseList(get("/transactions/FOOD").body());
        @SuppressWarnings("unchecked")
        var primeiro = (Map<String, Object>) lista.getFirst();
        assertEquals("Starbucks", primeiro.get("description"));
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String path) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static Map<String, Object> mapa(String body) {
        return JsonParserFactory.getJsonParser().parseMap(body);
    }

    private static String pedido(String description, String amount, String category) {
        return """
                {"description":"%s","amount":%s,"category":"%s"}
                """.formatted(description, amount, category);
    }
}
