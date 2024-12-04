Описание состава и работы приложения

**orders-service**

* Отвечает за создание и получение заказов пользователями.
* База данных: PostgreSQL (с миграциями Liquibase).
* Поддерживает взаимодействие через REST API.

**numbers-generate-service**

*Генерирует уникальные коды заказов и управляет их хранилищем.
*Хранилище кодов: Redis.
*Работает через REST API.

**Логика взаимодействия:**
1. При попытке создания заказа пользователем orders-service отправляет REST-запрос в numbers-generate-service.
2. numbers-generate-service:
 * Генерирует батч из 1000 уникальных кодов заказа с помощью атомарного Lua-скрипта (инкрементирует код и проверяет текущую дату).
 * Сохраняет батч в Redis
 * Отправляет сгенерированный батч обратно в orders-service.
3. orders-service:
 * Принимает батч и сохраняет его в структуру ConcurrentQueue.
 * Выдает первый доступный код для создания заказа.
 * Для последующих заказов использует коды из очереди, минимизируя запросы к numbers-generate-service и Redis.
      
**Как запустить**
1. Соберите и запустите контейнеры: docker-compose up -d --build
2. Тестирование через Swagger UI: http://localhost:9002/swagger-ui/index.html.

**Технологический стек**
Backend: Spring Boot, JDBC Template
Базы данных: PostgreSQL, Redis
Миграции: Liquibase
Документация API: OpenAPI/Swagger
Контейнеризация: Docker, Docker Compose
