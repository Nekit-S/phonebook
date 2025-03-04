-- Создаем расширение для UUID, если оно еще не создано
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создаем таблицу, если она не существует
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Вставляем тестовые данные только если таблица пуста
INSERT INTO users (id, name, email)
SELECT uuid_generate_v4(), 'Ivan Ivanov', 'ivan@example.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'ivan@example.com');

INSERT INTO users (id, name, email)
SELECT uuid_generate_v4(), 'Mariya Petrova', 'maria@example.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'maria@example.com');

INSERT INTO users (id, name, email)
SELECT uuid_generate_v4(), 'Aleksey Sidorov', 'alex@example.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'alex@example.com');