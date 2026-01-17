-- Таблица задач для фоновой обработки
-- Используем JSONB для эффективного хранения и поиска параметров задачи
-- Поддерживается только PostgreSQL 9.4+

CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       task_type VARCHAR(50) NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       payload JSONB,
                       result JSONB,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);