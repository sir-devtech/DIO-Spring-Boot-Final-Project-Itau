# Anota

API de orçamento por voz, do módulo de Spring AI do bootcamp Java com IA (DIO / Itaú).

A aula mostra o caminho: áudio vira texto, o modelo escolhe uma função Java, a função grava ou consulta, e a resposta volta em áudio. Eu segui esse caminho. O que está a mais é o que eu senti falta quando fui usar.

## O que eu acrescentei

No projeto da trilha, qualquer número entra. Zero também. Aqui o domínio recusa valor menor ou igual a zero, e descrição vazia, antes de chegar no banco. O REST e a ferramenta da IA passam pelo mesmo use case, então os dois obedecem a mesma regra.

Também não tinha como perguntar "quanto já foi". Criei o resumo: total, quantidade e total por categoria. Dá para ver em `GET /transactions/resumo` ou pedir para a IA, que chama a tool `summarize-transactions`.

Entrou a categoria `FOOD`. No vídeo o exemplo é "gastei 50 no Starbucks", e o enum original só tinha mercado, farmácia e carro. Café ficava sem lugar.

O valor fica em reais (`BigDecimal`, duas casas). Na trilha o campo era centavos em `long`. "Gastei 50" virar 50 centavos era o tipo de erro que eu não queria explicar depois.

## Como rodar

JDK 25.

```bat
gradlew.bat test
gradlew.bat bootRun
```

Abre em `http://localhost:8080`. O banco é H2 na memória. Sem Docker. Sem chave da OpenAI para criar, listar e ver o resumo.

```bat
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d "{\"description\":\"Starbucks\",\"amount\":50,\"category\":\"FOOD\"}"

curl http://localhost:8080/transactions/resumo

curl http://localhost:8080/transactions/FOOD
```

`amount` 0 ou negativo volta 400 com `{"erro":"Valor tem que ser maior que zero."}`.

Categorias: `GROCERIES`, `PHARMA`, `AUTO`, `FOOD`.

Console do H2: `http://localhost:8080/h2-console`  
JDBC `jdbc:h2:mem:budget`, usuário `sa`, senha vazia.

## Voz

`POST /transactions/ai` recebe um arquivo de áudio e devolve um mp3. Esse pedaço só sobe com o profile `voz` e a variável `OPENAI_API_KEY`. Sem a chave, o resto da API continua de pé. Eu deixei assim de propósito: não queria a aplicação inteira presa num segredo só para gravar um café.

No Windows o prompt em português é lido em UTF-8. Com o charset padrão da máquina, o acento quebrava e o modelo recebia texto torto.

```bat
set OPENAI_API_KEY=sua-chave
gradlew.bat bootRun --args="--spring.profiles.active=voz"
```

A chave fica na variável de ambiente. Não está no repositório.

Fluxo: Whisper transcreve, o `ChatClient` chama `persist-transaction`, `list-transactions-by-category` ou `summarize-transactions`, e o TTS devolve o áudio.

## Stack

Spring Boot 4.0.5, Spring AI 2.0.0-M4, Java 25, Gradle, Spring Data JPA, H2. OpenAI (gpt-4o-mini, Whisper, TTS) só no profile `voz`.

## Como está organizado

```
http  ->  application  ->  domain
               ^
         JPA + H2
```

O controller não decide se o valor presta. Quem decide é `Transaction`. A IA também não grava direto no banco: ela chama o mesmo use case do REST.

## O que ficou na cabeça

Tool calling é a IA escolhendo um método Java. Quem executa é o código, com a regra que eu escrevi. STT e TTS são as pontas. Se a regra mora no meio, a voz e o JSON não divergem.

A parte que mais me atrasou foi fazer a API subir sem a chave. O starter da OpenAI exige a key mesmo quando você só quer testar o banco. Por isso chat, transcrição e fala ficam desligados (`none`) até o profile `voz`.
