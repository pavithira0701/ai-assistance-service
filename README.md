# AI Assistance Service

A Spring Boot microservice that adds AI capabilities to an order/product microservice system. It uses **Spring AI with OpenAI** to:

- Answer natural-language questions about orders and products by calling the other services as **AI tools** (an agent).
- Generate short, professional **order summaries** automatically whenever a new order is created (via **Kafka**).
- Handle failed events safely with a **Dead Letter Topic (DLT)** and a recovery flow.

## Features

- **AI agent endpoint** – ask questions like *"What are the details of order 5?"* or *"How many units of product 3 are available?"*. The model decides which tool to call.
- **Order summary endpoint** – send an order and get an AI-written summary back.
- **Event-driven summaries** – consumes `order-created` events from Kafka and logs an AI-generated summary for each.
- **Retry + DLT** – failed messages are retried and then published to `order-created-dlt`.
- **Service discovery** – registers with Eureka and calls other services through OpenFeign using their service names.

## Tech Stack

| Area | Technology |
|------|------------|
| Language / Runtime | Java 21 |
| Framework | Spring Boot 4.1.1 |
| AI | Spring AI 2.0.1 (OpenAI starter) |
| Messaging | Apache Kafka (`spring-kafka`) |
| Service discovery | Spring Cloud 2025.1.2 – Netflix Eureka Client |
| Inter-service calls | Spring Cloud OpenFeign |
| Build | Maven (wrapper included) |

## Architecture

```
                 ┌──────────────────────────────┐
  HTTP client ──►│     AI-ASSISTANCE-SERVICE    │──► OpenAI (chat model)
                 │          (port 8084)         │
                 │                              │──► ORDER-SERVICE   (Feign, via Eureka)
                 │   AI tools: OrderTools,      │──► PRODUCT-SERVICE (Feign, via Eureka)
                 │             ProductTools     │
                 └───────────────▲──────────────┘
                                 │
        Kafka topic: order-created  (+ order-created-dlt for failures)
```

### Package layout

```
src/main/java/com/ai/assistance
├── client/       Feign clients for ORDER-SERVICE and PRODUCT-SERVICE
├── config/       OpenAI ChatClient and Kafka consumer/producer/error-handler config
├── consumer/     OrderEventConsumer – listens on `order-created`
├── controller/   REST controllers (AIAgentController, AIController)
├── dto/          OrderRequest, OrderCreatedEvent
├── kafka/        DltRecoveryListener – listens on `order-created-dlt`
├── service/      AIAgentService, OpenAIService, DltRecoveryService (+ model/ response DTOs)
└── tool/         OrderTools, ProductTools – Spring AI @Tool methods
```

## Prerequisites

- JDK 21
- An OpenAI API key
- A running **Eureka server** at `http://localhost:8761`
- A running **Kafka broker** at `localhost:9092`
- `ORDER-SERVICE` and `PRODUCT-SERVICE` registered in Eureka. They must expose:
  - `GET /api/orders/{id}` (returns `id`, `productId`, `quantity`)
  - `GET /api/products/{id}` (returns `id`, `productId`, `category`, `brand`, `model`, `quantity`)

## Configuration

Settings live in `src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8084` | HTTP port |
| `spring.application.name` | `AI-ASSISTANCE-SERVICE` | Eureka service name |
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka/` | Eureka server |
| `spring.ai.openai.api-key` | `${OPENAI_API_KEY}` | OpenAI key, read from the environment |
| `spring.ai.openai.chat.model` | `gpt-5.6` | Chat model to use |
| `spring.ai.openai.chat.options.reasoning-effort` | `none` | Reasoning effort |
| `spring.kafka.bootstrap-servers` | `localhost:9092` | Kafka brokers |
| `logging.file.name` | `logs/ai-assistance-service.log` | Log file |

Set your API key as an environment variable. Never commit it:

```bash
# macOS / Linux
export OPENAI_API_KEY=your-key-here

# Windows (PowerShell)
$env:OPENAI_API_KEY="your-key-here"
```

> Note: the Kafka consumer factory in `KafkaConsumerConfig` currently hardcodes `localhost:9092` and the consumer group `ai-assistance-group`. Change it there if your broker differs.

## Getting Started

```bash
# 1. Clone
git clone https://github.com/pavithira0701/ai-assistance-service.git
cd ai-assistance-service

# 2. Set your OpenAI key (see above)

# 3. Run
./mvnw spring-boot:run        # use mvnw.cmd on Windows
```

The service starts on **http://localhost:8084**.

To build a jar instead:

```bash
./mvnw clean package
java -jar target/ai-assistance-service-0.0.1-SNAPSHOT.jar
```

## API

### Ask the AI agent

`GET /ai/ask?question=<text>`

The model can call the order and product tools to answer from live data.

```bash
curl -G "http://localhost:8084/ai/ask" \
  --data-urlencode "question=What are the details of order 1 and how many units of its product are in stock?"
```

### Generate an order summary

`POST /ai/order-summary`

```bash
curl -X POST http://localhost:8084/ai/order-summary \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "productId": 2, "quantity": 3}'
```

Returns a short, professional AI-written summary as plain text.

## Kafka Events

| Topic | Direction | Payload | Purpose |
|-------|-----------|---------|---------|
| `order-created` | Consumed | `OrderCreatedEvent` | Triggers an AI order summary (logged to the console) |
| `order-created-dlt` | Consumed (manual) | `OrderCreatedEvent` | Recovery of failed events |

Example `order-created` message:

```json
{ "orderId": 1, "productId": 2, "quantity": 3 }
```

### Error handling and DLT recovery

- If processing fails (for example, OpenAI is unavailable), the message is retried **2 times with a 2-second delay**.
- After retries are exhausted, it is published to **`order-created-dlt`**.
- `DltRecoveryListener` is configured with `autoStartup = "false"`, so DLT messages are **not** reprocessed automatically. Once the underlying issue is fixed, start that listener (group `ai-assistance-dlt-recovery-group`) and it will republish each event to `order-created`.

## Testing

```bash
./mvnw test
```

Unit tests cover `OrderTools` and `ProductTools` (including not-found and service-unavailable handling), plus an application context test.

## Logs

Logs are written to `logs/ai-assistance-service.log` (rolled daily).

## Roadmap / Ideas

- Move hardcoded Kafka settings into `application.properties`
- Persist or publish generated summaries instead of only logging them
- Replace `System.out.println` with a logger
- Add authentication to the AI endpoints

## License

No license has been specified yet. Add one (for example MIT) if you plan to share this project publicly.
