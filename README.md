# Projeto de Academia

## 📌 Arquitetura com foco em manter cada arquivo único e bem definido

### Pasta frontend
 Possui os seguintes arquivos:
 
- **img** – Com a logo usada
- **Login HTML/Login-ScriptJS** – A pagina de login e as funções para consumir a API
- **Index HTML/Style CSS / ScriptJS ** –  São as relacionadas a pagina principal e suas funções
  
### Pasta src(backend)

 Feita em Java com SpringWeb, JPA, H2 e Maven

- **Entities** – Pasta com a criação da classe de Instrutores e Alunos, seus get and setters e suas relação, e os notations necessários para criar o banco de dados
- **Repositories** - Pasta que lida somente com a herança de interface JPA pra criação de banco de dados, podem ser implementadas funções gerais relacionadas a banco nela no futuro
- **Controllers** - Pasta que lida com os endpoints, sejam as funções de comunicação ou de buscas especificas pra classe.
-  **UnigymApplication** - Arquivo para iniciar o funcionamento do springboot e suas funcionalidades.
-  **Resources** Configuração do banco H2 para visualização e arquivo sql para implementar dados manuamente.

---

## ✅ Funcionalidades

- [x] Pagina de Login 
- [x] Persistência em banco de dados 
- [ ] Criação de set de exercicios para cada aluno.

---
# 🧪 Tutorial de Teste dos Servidores Secundários

Será definido depois.

---
