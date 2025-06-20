# Aplicação de Login com Spring Boot

![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-4.0-red.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## 📖 Sobre o Projeto

Este é um projeto de uma aplicação web completa para registro e autenticação de usuários, desenvolvida como parte de um estudo aprofundado do ecossistema Spring. A aplicação permite que novos usuários se cadastrem, façam login de forma segura e acessem uma área restrita (dashboard).

Todo o processo de autenticação e gerenciamento de sessão é controlado pelo **Spring Security**, seguindo as melhores práticas de segurança, como a criptografia de senhas com BCrypt.

---

## ✨ Funcionalidades

-   ✅ **Cadastro de Usuários**: Novos usuários podem se registrar no sistema.
-   🔐 **Autenticação Segura**: Login com validação de credenciais e senha criptografada.
-   🛡️ **Controle de Acesso**: Páginas protegidas que só podem ser acessadas após o login.
-   🚪 **Funcionalidade de Logout**: O usuário pode encerrar sua sessão de forma segura.
-   📝 **Validação de Formulários**: Campos obrigatórios para garantir a integridade dos dados.
-   ⚡ **Persistência de Dados**: Interação com um banco de dados MySQL para salvar e consultar informações.

---

## 🛠️ Tecnologias Utilizadas

As seguintes tecnologias foram utilizadas na construção do projeto:

-   **Backend**: Java 17+, Spring Boot, Spring Security, Spring Data JPA, Hibernate
-   **Frontend**: Thymeleaf, HTML5, CSS3
-   **Banco de Dados**: MySQL
-   **Build & Dependências**: Maven

---

## 🚀 Como Executar o Projeto Localmente

Siga os passos abaixo para executar a aplicação na sua máquina.

### Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:
-   [Java (JDK)](https://www.oracle.com/java/technologies/downloads/) - Versão 17 ou superior.
-   [Maven](https://maven.apache.org/download.cgi) - Gerenciador de dependências.
-   [Git](https://git-scm.com/downloads) - Para clonar o projeto.
-   [MySQL](https://dev.mysql.com/downloads/mysql/) - O banco de dados.

### 1. Clone o Repositório
```bash
git clone [https://github.com/davinatosms/aplicacao-login-spring.git](https://github.com/davimatosms/aplicacao-login-spring.git)
cd aplicacao-login-spring
```

### 2. Configure o Banco de Dados
1.  Abra seu cliente MySQL (Workbench, etc).
2.  Crie um novo banco de dados (schema) com o nome `applogin`.
    ```sql
    CREATE DATABASE applogin;
    ```
3.  A aplicação está configurada para criar a tabela `usuario` automaticamente na primeira vez que for executada (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Configure a Conexão
As configurações de conexão com o banco de dados estão no arquivo `src/main/resources/application.properties`. Verifique se o seu usuário e senha do MySQL estão corretos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/applogin
spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configurações do Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
*(Nota: É uma boa prática usar `application.properties` em vez de uma classe de configuração Java para esses dados, por isso sugiro esta estrutura.)*

### 4. Execute a Aplicação
Você pode executar a aplicação de duas formas:

1.  **Via terminal, usando o Maven:**
    ```bash
    mvn spring-boot:run
    ```
2.  **Através da sua IDE (IntelliJ):**
    -   Abra o projeto.
    -   Encontre a classe `ApploginApplication.java`.
    -   Clique com o botão direito e selecione "Run 'ApploginApplication'".

A aplicação estará disponível em `http://localhost:8080`.

---

##  endpoints

-   `GET /`: Página principal (dashboard), protegida.
-   `GET /login`: Página de login.
-   `POST /login`: Processa a autenticação (gerenciado pelo Spring Security).
-   `GET /cadastroUsuario`: Página de cadastro.
-   `POST /cadastroUsuario`: Processa o registro de um novo usuário.
-   `POST /logout`: Processa o logout (gerenciado pelo Spring Security).

---

## ✒️ Autor

Desenvolvido por **Davi Matos**.

[<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" />](https://github.com/davimatosms)