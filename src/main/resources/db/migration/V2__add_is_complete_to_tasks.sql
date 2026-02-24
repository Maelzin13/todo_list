-- Campo para marcar tarefa como concluída (toggle na lista)
ALTER TABLE financas.tasks
ADD COLUMN IF NOT EXISTS is_complete BOOLEAN NOT NULL DEFAULT FALSE;
