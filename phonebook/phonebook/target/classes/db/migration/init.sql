-- Создаем расширение для UUID, если оно еще не создано
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Проверяем существование таблицы users
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'users') THEN
        -- Таблица не существует, можно добавить тестовые данные после создания
        CREATE TABLE IF NOT EXISTS users (
            id UUID PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL UNIQUE
        );
        
        -- Добавляем тестовые данные
        INSERT INTO users (id, name, email) 
        VALUES 
            (uuid_generate_v4(), 'Иван Иванов', 'ivan@example.com'),
            (uuid_generate_v4(), 'Мария Петрова', 'maria@example.com'),
            (uuid_generate_v4(), 'Алексей Сидоров', 'alex@example.com');
    END IF;
END
$$;