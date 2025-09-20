-- =============================================================================
-- SCRIPT DE POPULAÇÃO INICIAL DO BANCO DE DADOS
-- =============================================================================
-- Este arquivo contém comandos SQL para inserir dados iniciais no banco de dados.
-- É executado automaticamente pelo Spring Boot durante a inicialização quando
-- a propriedade spring.jpa.hibernate.ddl-auto está configurada como create
-- ou create-drop.
--
-- Os dados inseridos incluem:
-- - Instrutores de exemplo
-- - Alunos de exemplo com credenciais para teste
-- - Treinos com suas respectivas datas e descrições
-- - Exercícios agrupados por treino e dia da semana
-- - Medidas corporais de exemplo para acompanhamento
--
-- Nota: As datas dos treinos e medidas estão configuradas para 2025
-- para garantir que apareçam como "atuais" por um bom tempo.
-- =============================================================================

-- =============================================================================
-- INSTRUTORES
-- =============================================================================
INSERT INTO Instrutores(nome) VALUES ('Sergio');
INSERT INTO Instrutores(nome) VALUES ('Caio');

-- =============================================================================
-- ALUNOS
-- =============================================================================
-- Usuários com email e senha para login
INSERT INTO Alunos(instrutor_id, nome, email, senha) VALUES (1, 'Maria', 'maria@email.com', '123456');
INSERT INTO Alunos(instrutor_id, nome, email, senha) VALUES (1, 'Bob', 'bob@email.com', '123456');
INSERT INTO Alunos(instrutor_id, nome, email, senha) VALUES (2, 'Alex', 'alex@email.com', '123456');
INSERT INTO Alunos(instrutor_id, nome, email, senha) VALUES (2, 'Ana', 'ana@email.com', '123456');

-- =============================================================================
-- TREINOS
-- =============================================================================
-- Treinos de exemplo (atualizados para 2025)
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Iniciante A', 'Treino para adaptação muscular', '2025-09-01', '2025-11-01', 1, 1);
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Hipertrofia', 'Foco em ganho de massa muscular', '2025-09-15', NULL, 1, 1);
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Cardio', 'Treino para melhorar condicionamento', '2025-09-10', NULL, 2, 1);
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Força', 'Foco em ganho de força', '2025-09-05', NULL, 3, 2);
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Perda de Peso', 'Treino com foco em queima calórica e definição', '2025-09-08', NULL, 4, 2);
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Funcional', 'Treino com foco em movimentos funcionais e mobilidade', '2025-09-12', NULL, 4, 2);
INSERT INTO Treinos(nome, descricao, data_inicio, data_fim, aluno_id, instrutor_id) VALUES ('Treino Resistência', 'Treino para aumentar resistência muscular', '2025-09-18', NULL, 3, 2);

-- =============================================================================
-- EXERCÍCIOS DO TREINO INICIANTE A (ID 1)
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Supino Reto', 'Peito', 3, 12, '10kg', 'Manter cotovelos alinhados', 'SEGUNDA', 1);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Agachamento', 'Pernas', 3, 15, 'Peso corporal', 'Não passar joelho da ponta do pé', 'SEGUNDA', 1);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Remada Baixa', 'Costas', 3, 12, '15kg', 'Manter coluna reta', 'QUARTA', 1);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Elevação Lateral', 'Ombros', 3, 12, '3kg', 'Movimento controlado', 'QUARTA', 1);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Rosca Direta', 'Braços', 3, 12, '5kg', 'Manter cotovelos fixos', 'SEXTA', 1);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Abdominal', 'Abdômen', 3, 20, 'Peso corporal', 'Respiração controlada', 'SEXTA', 1);

-- =============================================================================
-- EXERCÍCIOS DO TREINO HIPERTROFIA (ID 2)
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Supino Inclinado', 'Peito', 4, 10, '15kg', 'Descer até o meio do peito', 'SEGUNDA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Crucifixo', 'Peito', 3, 12, '8kg', 'Movimento controlado', 'SEGUNDA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Puxada Alta', 'Costas', 4, 10, '35kg', 'Puxar até o peito', 'TERCA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Remada Curvada', 'Costas', 3, 12, '12kg', 'Manter coluna estável', 'TERCA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Agachamento Smith', 'Pernas', 4, 12, '20kg', 'Descer até formar 90 graus', 'QUINTA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Leg Press', 'Pernas', 4, 15, '40kg', 'Pressionar com o calcanhar', 'QUINTA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Desenvolvimento', 'Ombros', 4, 10, '10kg', 'Não bloquear cotovelos', 'SEXTA', 2);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Rosca Alternada', 'Braços', 3, 12, '8kg', 'Alternar braços', 'SEXTA', 2);

-- =============================================================================
-- EXERCÍCIOS DO TREINO CARDIO (ID 3)
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Esteira', 'Cardiovascular', 1, 0, 'N/A', 'Corrida por 20 minutos, intensidade moderada', 'SEGUNDA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Jumping Jack', 'Cardiovascular', 3, 30, 'Peso corporal', 'Ritmo rápido, intervalo de 30 segundos', 'SEGUNDA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Bicicleta', 'Cardiovascular', 1, 0, 'N/A', 'Pedalar por 15 minutos, resistência média', 'SEGUNDA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Burpee', 'Cardiovascular', 3, 15, 'Peso corporal', 'Movimento completo, intervalo de 45 segundos', 'QUARTA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Corda', 'Cardiovascular', 3, 0, 'N/A', '1 minuto por série, ritmo moderado', 'QUARTA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Mountain Climber', 'Cardiovascular', 3, 30, 'Peso corporal', 'Ritmo rápido, intervalo de 30 segundos', 'QUARTA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Elíptico', 'Cardiovascular', 1, 0, 'N/A', '15 minutos, resistência progressiva', 'SEXTA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Agachamento com Salto', 'Pernas', 4, 15, 'Peso corporal', 'Explosão no movimento, intervalo de 40 segundos', 'SEXTA', 3);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('HIIT', 'Cardiovascular', 10, 0, 'N/A', '30 segundos de esforço máximo, 30 segundos de descanso', 'SEXTA', 3);

-- =============================================================================
-- EXERCÍCIOS DO TREINO FORÇA (ID 4)
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Agachamento Livre', 'Pernas', 5, 5, '60kg', 'Profundidade completa, cuidado com a coluna', 'SEGUNDA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Levantamento Terra', 'Posterior', 5, 5, '80kg', 'Manter coluna neutra, quadril para trás', 'SEGUNDA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Stiff', 'Posterior', 4, 8, '40kg', 'Sentir alongamento dos isquiotibiais', 'SEGUNDA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Supino Reto', 'Peito', 5, 5, '50kg', 'Pegada média, cotovelos a 45°', 'QUARTA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Remada Curvada', 'Costas', 5, 5, '45kg', 'Puxar para o abdômen, cotovelos junto ao corpo', 'QUARTA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Desenvolvimento Militar', 'Ombros', 4, 6, '30kg', 'Movimento controlado, sem impulso', 'QUARTA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Barra Fixa', 'Costas', 4, 8, 'Peso corporal', 'Pegada pronada, puxar até o queixo', 'SEXTA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Paralelas', 'Tríceps', 4, 8, 'Peso corporal', 'Descer até formar 90° nos cotovelos', 'SEXTA', 4);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Rosca Direta', 'Bíceps', 4, 8, '25kg', 'Barra reta, cotovelos fixos', 'SEXTA', 4);

-- =============================================================================
-- EXERCÍCIOS DO TREINO PERDA DE PESO (ID 5) - ANA
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Esteira', 'Cardiovascular', 1, 0, 'N/A', 'Intervalos 1min rápido/1min lento por 20min', 'SEGUNDA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Agachamento com Peso', 'Pernas', 4, 15, '15kg', 'Manter ritmo constante, descanso de 45s', 'SEGUNDA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Puxada Alta', 'Costas', 4, 15, '25kg', 'Puxar até o peito, contração no final', 'SEGUNDA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Polichinelo', 'Cardiovascular', 4, 30, 'N/A', 'Ritmo rápido, descanso de 30s', 'TERCA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Cadeira Extensora', 'Pernas', 4, 15, '20kg', 'Movimento controlado', 'TERCA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Abdominal', 'Abdômen', 4, 25, 'Peso corporal', 'Manter região lombar pressionada no chão', 'TERCA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Corda', 'Cardiovascular', 3, 0, 'N/A', 'Pular por 1min sem parar', 'QUINTA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Elevação Lateral', 'Ombros', 4, 15, '3kg', 'Controlado, sem impulso', 'QUINTA', 5);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Prancha', 'Abdômen', 4, 0, 'Peso corporal', 'Manter por 45 segundos', 'QUINTA', 5);

-- =============================================================================
-- EXERCÍCIOS DO TREINO FUNCIONAL (ID 6) - ANA
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Agachamento com Salto', 'Pernas', 3, 10, 'Peso corporal', 'Explosão no movimento, pouso suave', 'SEGUNDA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Flexão com Rotação', 'Peito/Core', 3, 8, 'Peso corporal', 'Rotacionar ao final da flexão, alternando lados', 'SEGUNDA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Turkish Get-up', 'Total', 3, 5, '5kg', 'Movimento completo, técnica perfeita', 'SEGUNDA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Kettlebell Swing', 'Posterior/Core', 3, 15, '10kg', 'Movimento de quadril, manter coluna neutra', 'QUARTA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Mountain Climber', 'Cardiovascular/Core', 3, 30, 'Peso corporal', 'Ritmo rápido e constante', 'QUARTA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Burpee', 'Total', 3, 10, 'Peso corporal', 'Movimento completo com salto', 'QUARTA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Snatch com Halteres', 'Total', 3, 8, '7kg', 'Movimento explosivo, técnica correta', 'SEXTA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Battle Rope', 'Cardiovascular/Braços', 3, 0, 'N/A', '30 segundos de ondas alternadas', 'SEXTA', 6);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Wall Ball', 'Total', 3, 15, '6kg', 'Arremesso na marcação da parede, recepção em agachamento', 'SEXTA', 6);

-- =============================================================================
-- EXERCÍCIOS DO TREINO RESISTÊNCIA (ID 7) - ALEX
-- =============================================================================
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Supino Reto', 'Peito', 4, 20, '15kg', 'Ritmo controlado, descanso de 30s', 'SEGUNDA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Puxada Frontal', 'Costas', 4, 20, '20kg', 'Contração completa, descanso de 30s', 'SEGUNDA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Desenvolvimento', 'Ombros', 4, 20, '8kg', 'Movimento sem impulso, descanso de 30s', 'SEGUNDA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Leg Press', 'Pernas', 4, 20, '30kg', 'Amplitude completa, descanso de 30s', 'QUARTA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Cadeira Flexora', 'Posterior', 4, 20, '15kg', 'Contração no topo, descanso de 30s', 'QUARTA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Panturrilha em Pé', 'Panturrilha', 4, 25, '20kg', 'Amplitude máxima, descanso de 30s', 'QUARTA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Rosca Direta', 'Bíceps', 3, 20, '10kg', 'Sem balançar o corpo, descanso de 30s', 'SEXTA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Tríceps Pulley', 'Tríceps', 3, 20, '15kg', 'Cotovelos fixos, descanso de 30s', 'SEXTA', 7);
INSERT INTO Exercicios(nome, grupo_muscular, series, repeticoes, carga, observacoes, dia_semana, treino_id) VALUES ('Abdominal Infra', 'Abdômen', 3, 25, 'Peso corporal', 'Movimento controlado, descanso de 30s', 'SEXTA', 7);

-- =============================================================================
-- MEDIDAS
-- =============================================================================
-- Medidas para o aluno Maria (ID 1)
INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-06-10', 65.5, 1.65, 24.06, 28.0, 28.0, 92.0, 75.0, 98.0, 52.0, 36.0, 'Avaliação inicial', 1, 1);

INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-07-10', 64.2, 1.65, 23.58, 27.0, 28.5, 91.5, 73.5, 97.0, 52.5, 36.0, 'Progresso após 30 dias', 1, 1);

INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-08-10', 63.0, 1.65, 23.14, 26.0, 29.0, 91.0, 72.0, 96.0, 53.0, 36.5, 'Progresso após 60 dias', 1, 1);

INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-09-10', 62.5, 1.65, 22.96, 25.0, 29.5, 90.5, 71.0, 95.5, 53.5, 37.0, 'Progresso após 90 dias', 1, 1);

-- Medidas para o aluno Bob (ID 2)
INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-07-15', 78.5, 1.78, 24.78, 18.0, 32.0, 98.0, 85.0, 95.0, 56.0, 38.0, 'Avaliação inicial', 2, 1);

INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-08-15', 80.0, 1.78, 25.25, 17.5, 33.0, 99.0, 84.0, 95.0, 57.0, 38.5, 'Progresso após 30 dias', 2, 1);

-- Medidas para o aluno Alex (ID 3)
INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-08-02', 72.0, 1.75, 23.51, 15.0, 33.0, 96.0, 82.0, 93.0, 55.0, 37.0, 'Avaliação inicial', 3, 2);

INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-09-02', 74.5, 1.75, 24.33, 14.0, 34.5, 98.0, 81.0, 92.5, 56.5, 38.0, 'Progresso após 30 dias', 3, 2);

-- Medidas para a aluna Ana (ID 4)
INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-08-10', 68.0, 1.62, 25.91, 32.0, 27.0, 90.0, 78.0, 102.0, 60.0, 35.0, 'Avaliação inicial', 4, 2);

INSERT INTO Medidas(data, peso, altura, imc, percentual_gordura, circunferencia_braco, circunferencia_peitoral, circunferencia_cintura, circunferencia_quadril, circunferencia_coxa, circunferencia_panturrilha, observacoes, aluno_id, instrutor_id) 
VALUES ('2025-09-10', 66.5, 1.62, 25.34, 30.5, 26.5, 89.0, 76.0, 100.0, 59.0, 35.0, 'Progresso após 30 dias', 4, 2);