-- Migration: tabela de tasks no schema financas (usuários já existem em financas.usuario)
CREATE TABLE IF NOT EXISTS financas.tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50),
    description VARCHAR(255),
    start_at TIMESTAMP,
    end_at TIMESTAMP,
    priority VARCHAR(20),
    id_usuario BIGINT NOT NULL,
    data_cadastro TIMESTAMP DEFAULT now(),
    CONSTRAINT tasks_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES financas.usuario (id)
);

CREATE INDEX IF NOT EXISTS idx_tasks_id_usuario ON financas.tasks (id_usuario);
