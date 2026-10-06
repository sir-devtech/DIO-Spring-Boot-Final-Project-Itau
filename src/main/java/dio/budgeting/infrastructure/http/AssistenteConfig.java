package dio.budgeting.infrastructure.http;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.application.SummarizeTransactionsUseCase;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Profile("voz")
public class AssistenteConfig {

    @Bean
    ChatClient assistente(
            ChatClient.Builder builder,
            @Value("classpath:prompts/sistema.st") Resource prompt,
            PersistTransactionUseCase persistTransactionUseCase,
            ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
            SummarizeTransactionsUseCase summarizeTransactionsUseCase) throws IOException {
        return builder
                .defaultSystem(prompt.getContentAsString(StandardCharsets.UTF_8))
                .defaultTools(
                        persistTransactionUseCase,
                        listTransactionsByCategoryUseCase,
                        summarizeTransactionsUseCase)
                .build();
    }
}
