# Projeto de Academia

##  Arquitetura com foco em manter cada arquivo único e bem definido

## Requisitos para Execução do Projeto

Para executar o projeto UniGym, você precisará ter os seguintes componentes instalados:

- **Java 21** - O projeto utiliza Java 21, já que foi iniciado antes do lançamento do LTS 25, porém não afeta em nada, já que a versão 25 é mais focada em concorrencia pelo que eu li
- **Maven 3.8+** - Para gerenciamento de dependências e build do projeto.
- **Banco de dados H2** - Incluído como dependência (não requer instalação separada).
- **Navegador web moderno** - Chrome, Firefox, Edge ou Safari para acessar a interface.
- **Portas disponíveis** - A aplicação utiliza por padrão a porta 8080.

### Instruções para Execução

1. Clone o repositório:
   ```
   git clone https://github.com/AstalDLyon/fullstack-unicarioca.git
   cd fullstack-unicarioca
   ```

2. Compile e execute o projeto usando Maven:
   ```
   mvn clean spring-boot:run OU rode o UnigymApplication(classe main).
   ```

3. Acesse a aplicação no navegador:
   ```
   http://localhost:8080
   ```

4. Para acessar o console H2 (banco de dados):
   ```
   http://localhost:8080/h2-console
   ```
   - JDBC URL: jdbc:h2:file:./unigym-db
   - Usuário: sa
   - Senha: (deixe em branco)

### Pasta frontend
 Possui os seguintes arquivos:
 
- **img/** – Pasta contendo imagens usadas no projeto, incluindo a logo
- **index.html** – Página inicial do sistema com apresentação da academia
- **index-style.css** – Estilos específicos para a página inicial
- **index-script.js** – Funcionalidades JavaScript da página inicial
- **login.html** – Página de autenticação do usuário
- **login-style.css** – Estilos específicos para a página de login
- **login-script.js** – Funcionalidades de autenticação e consumo da API
- **auth-script.js** – Funções de gerenciamento de autenticação
- **minhas-medidas.html** – Página para visualização de medidas do aluno
- **medidas-style.css** – Estilos para a página de medidas
- **medidas-script.js** – Funcionalidades relacionadas às medidas do aluno
- **meus-treinos.html** – Página para visualização dos treinos do aluno
- **treinos-style.css** – Estilos para a página de treinos
- **treinos-script.js** – Funcionalidades relacionadas aos treinos do aluno
- **nav-utils.css** – Estilos compartilhados para navegação entre páginas
- **script.js** – Funcionalidades JavaScript compartilhadas
- **debug-script.js** – Funções auxiliares para depuração
  
### Pasta src (backend)

Desenvolvida em Java com Spring Boot, utilizando Spring Web, Spring Data JPA, H2 Database e Maven.

#### Estrutura de pacotes

- **com.Unigym.entities** – Contém as classes de domínio da aplicação:
  - **Aluno.java** – Entidade que representa um aluno da academia
  - **Instrutor.java** – Entidade que representa um instrutor da academia
  - **Treino.java** – Entidade que representa um treino associado a um aluno
  - **Exercicio.java** – Entidade que representa um exercício dentro de um treino
  - **Medida.java** – Entidade que representa as medidas corporais de um aluno

- **com.Unigym.repositories** – Interfaces que estendem JpaRepository para persistência de dados:
  - **AlunoRepository.java** – Operações de banco de dados para entidade Aluno
  - **InstrutorRepository.java** – Operações de banco de dados para entidade Instrutor
  - **TreinoRepository.java** – Operações de banco de dados para entidade Treino
  - **ExercicioRepository.java** – Operações de banco de dados para entidade Exercicio
  - **MedidaRepository.java** – Operações de banco de dados para entidade Medida

- **com.Unigym.controllers** – Classes responsáveis pelos endpoints REST da API:
  - **AlunoController.java** – Endpoints para gerenciamento de alunos
  - **InstrutorController.java** – Endpoints para gerenciamento de instrutores
  - **TreinoController.java** – Endpoints para gerenciamento de treinos
  - **ExercicioController.java** – Endpoints para gerenciamento de exercícios
  - **MedidaController.java** – Endpoints para gerenciamento de medidas
  - **AuthController.java** – Endpoints para autenticação de usuários

- **com.Unigym.UnigymApplication.java** – Classe principal que inicializa a aplicação Spring Boot

#### Recursos

- **application.properties** – Arquivo de configuração do Spring Boot e do banco de dados H2
- **import.sql** – Script SQL para população inicial do banco de dados com dados de exemplo

---

## Funcionalidades

### Funcionalidades Implementadas

#### Autenticação e Usuários
- [x] Sistema de login para alunos
- [x] Persistência de sessão com localStorage
- [x] Proteção de rotas para páginas que exigem autenticação
- [x] Funcionalidade de logout

#### Cadastro e Gerenciamento
- [x] Cadastro de alunos, instrutores, treinos e medidas
- [x] Persistência em banco de dados H2
- [x] Relacionamentos entre entidades (aluno-treino, treino-exercício, etc.)

#### Visualização de Dados
- [x] Consulta de treinos por aluno
- [x] Exibição de treinos organizados por dia da semana
- [x] Consulta de histórico de medidas corporais
- [x] Visualização de detalhes de exercícios

#### Interface de Usuário
- [x] Página inicial com informações sobre a academia
- [x] Interface responsiva adaptada para diferentes dispositivos
- [x] Navegação intuitiva entre diferentes seções
- [x] Exibição de informações do usuário logado

#### API REST
- [x] Endpoints para autenticação
- [x] Endpoints para gerenciamento de alunos
- [x] Endpoints para gerenciamento de treinos e exercícios
- [x] Endpoints para gerenciamento de medidas corporais

### Funcionalidades Planejadas

- [ ] Análise avançada de progresso com gráficos // talvez, nunca fiz grafico em java muito menos JS preciso pesquisar

---
#  Tutorial de Teste

Será definido depois.

---
