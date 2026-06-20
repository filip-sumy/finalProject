# Магазин бытовой техники

Веб-приложение на Spring Boot для управления магазином техники: каталог, заказы, роли сотрудников и клиентов. Есть веб-интерфейс (Thymeleaf) и REST API с JWT.

## Быстрый старт

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Откройте в браузере: http://localhost:8080

Запуск тестов:

```bash
mvn test
```

## Демо-аккаунты

| Роль | Email | Пароль |
|------|-------|--------|
| Администратор | admin@test.com | password |
| Сотрудник | employee@test.com | password |
| Клиент | john@test.com | password |

**Резервный вход (in-memory):** если email не найден в БД — `admin` / `Admin123!` или `employee` / `Employee123!`

## Как пользоваться

### Клиент
1. Войти под клиентом
2. **Shop** — выбрать товар, добавить в корзину
3. **Cart** — проверить позиции, оформить заказ
4. **My Orders** — история заказов, редактирование/удаление до подтверждения

### Сотрудник / администратор
- CRUD: клиенты, производители, техника
- Создание заказов от имени клиента
- **Approve** — подтверждение заказа (списание со склада)
- Админ дополнительно управляет сотрудниками

## REST API (JWT)

Веб-интерфейс работает через form login. API — stateless, с Bearer-токеном.

### Получить токен

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@test.com",
  "password": "password"
}
```

Ответ:

```json
{
  "token": "eyJ...",
  "email": "john@test.com",
  "roles": ["ROLE_CLIENT"]
}
```

### Примеры запросов

```http
GET /api/v1/appliances
Authorization: Bearer <token>

GET /api/v1/orders?page=0&size=10
Authorization: Bearer <token>

POST /api/v1/orders
Authorization: Bearer <token>
Content-Type: application/json

POST /api/v1/orders/{id}/approve
Authorization: Bearer <token>
```

Клиент видит только свои заказы. Подтверждение заказа — только ADMIN/EMPLOYEE.

## Стек

- Java 17, Spring Boot 4, Spring Data JPA, Spring Security
- Thymeleaf + i18n (English / Ukrainian)
- H2 (dev), PostgreSQL (prod)
- JWT (jjwt), BCrypt, ModelMapper, Lombok, AOP

## Профили

| Профиль | База данных | Назначение |
|---------|-------------|------------|
| `dev` (по умолчанию) | H2 in-memory | разработка, тесты |
| `prod` | PostgreSQL | production |

Prod:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Переменная окружения для JWT в prod: `JWT_SECRET` (base64, минимум 256 бит).

## Структура проекта

```
controller/          — MVC-контроллеры (Thymeleaf)
api/controller/      — REST API
api/security/        — JWT filter, JwtService, ApiSecurityConfig
service/             — бизнес-логика
service/cart/        — сессионная корзина
repository/          — JPA + custom @Query
dto/                 — модели форм и отображения
api/dto/request|response — DTO для API
entity/              — JPA-сущности
mapper/              — entity ↔ DTO
security/            — form login, RBAC, OrderSecurity
exception/           — кастомные исключения + i18n
resources/sql/       — начальные данные (без CommandLineRunner)
```

## Безопасность

- **Два способа входа:** БД (email) + in-memory fallback
- **RBAC:** ADMIN, EMPLOYEE, CLIENT
- **URL rules** + **@PreAuthorize** + проверка владельца заказа (`OrderSecurity`)
- **BCrypt** для паролей, политика сложности пароля
- **CSRF** включён для веб-форм (отключён только для H2 console и API)
- Stack traces и детали ошибок скрыты от пользователя
- Логирование: login/logout, отказ в доступе, бизнес-события в сервисах

## Языки интерфейса

Переключатель EN / UK в шапке сайта (`?lang=en` / `?lang=uk`).

Сообщения об ошибках (включая business exceptions) берутся из `messages_en.properties` и `messages_uk.properties`.

## Тесты

- Unit-тесты сервисов (Mockito)
- `@WebMvcTest` для MVC-контроллеров
- Тесты API auth endpoint
- Unit-тест корзины

## Соответствие заданию

| Требование | Статус |
|------------|--------|
| Spring Data JPA + custom queries | ✅ |
| Spring Security (in-memory + DB, RBAC, method/URL security) | ✅ |
| DTO, SQL scripts, validation, exceptions | ✅ |
| i18n EN + UK | ✅ |
| Logging (AOP + business + security events) | ✅ |
| H2 dev / PostgreSQL prod | ✅ |
| Unit tests (services + controllers) | ✅ |
| Order management | ✅ |
| Search, pagination, sorting | ✅ |
| JWT API | ✅ |
| BCrypt | ✅ |

## Заметки

- H2 Console (dev): http://localhost:8080/h2-console  
  JDBC URL: `jdbc:h2:mem:appliancedb`, user: `sa`, password: пустой
- Для production задайте свой `JWT_SECRET` и настройте PostgreSQL в `application-prod.properties`
