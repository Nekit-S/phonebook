# Телефонная книга (Phonebook)

REST API приложение для управления контактами в телефонной книге. Проект демонстрирует создание API на основе Spring Boot с использованием Docker для контейнеризации.

## Технологии

- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven
- OpenAPI (Swagger)

## Функциональность

- Создание, чтение, обновление и удаление пользователей (CRUD-операции)
- Валидация данных
- Пагинация и сортировка результатов
- REST API с документацией Swagger
- Контейнеризация с Docker

## Запуск с Docker

Самый быстрый способ запустить приложение:

```bash
# Клонировать репозиторий
git clone https://github.com/Nekit-S//phonebook.git

# Перейти в директорию проекта
cd phonebook

# Запустить с Docker Compose
docker-compose up
```

После запуска приложение будет доступно по адресу: http://localhost:8080

## Документация API

Swagger UI доступен по адресу: http://localhost:8080/swagger-ui.html

### API Endpoints

| Метод  | URL             | Описание                                       |
|--------|-----------------|------------------------------------------------|
| GET    | /users          | Получить всех пользователей (с пагинацией)     |
| GET    | /users/{id}     | Получить пользователя по ID                    |
| POST   | /users          | Создать нового пользователя                    |
| PUT    | /users/{id}     | Обновить существующего пользователя            |
| DELETE | /users/{id}     | Удалить пользователя                           |

### Примеры запросов

#### Создание пользователя
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Иван Иванов","email":"ivan@example.com"}'
```

#### Получение всех пользователей с пагинацией
```bash
curl -X GET "http://localhost:8080/users?page=0&size=10&sort=name,asc"
```

## Разработка

### Предварительные требования

- JDK 17 или выше
- Maven
- Docker и Docker Compose
- Ваша любимая IDE (VS Code, IntelliJ IDEA и т.д.)

### Запуск для разработки

1. Запустите только базу данных:
   ```bash
   docker-compose up -d db
   ```

2. Запустите приложение с профилем `local`:
   
   **PowerShell:**
   ```powershell
   mvn spring-boot:run "-Dspring-boot.run.profiles=local"
   ```
   
   **Linux/MacOS/Git Bash:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

3. Приложение запустится на порту 8080: http://localhost:8080

### Запуск тестов

```bash
mvn test
```

## Структура проекта

```
phonebook/
├── src/main/java/com/example/phonebook/
│   ├── controller/    # REST контроллеры
│   ├── dto/           # Объекты передачи данных
│   ├── exception/     # Обработчики исключений
│   ├── mapper/        # Маппинг DTO <-> Entity
│   ├── model/         # Сущности JPA
│   ├── repository/    # Spring Data репозитории
│   ├── service/       # Бизнес-логика приложения
│   └── PhonebookApplication.java # Точка входа
├── src/main/resources/
│   ├── application.properties    # Конфигурация для Docker
│   ├── application-local.properties # Конфигурация для локальной разработки
│   └── schema.sql                # SQL скрипт для инициализации БД
├── src/test/           # Тесты
├── docker-compose.yml  # Конфигурация Docker Compose
├── Dockerfile          # Инструкции для создания Docker образа
├── pom.xml             # Конфигурация Maven
└── README.md           # Документация проекта
```
