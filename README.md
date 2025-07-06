# 🥭 Caju Ajuda - Sistema de Helpdesk Multiplataforma

![image](https://github.com/user-attachments/assets/49e740f7-cbc6-4d99-bad2-e876f8ab5e52)

**Caju Ajuda** é uma solução de suporte ao cliente completa, projetada com uma arquitetura moderna e desacoplada. O sistema permite que clientes abram e acompanhem chamados através de interfaces web e mobile, enquanto a equipe de suporte utiliza uma aplicação desktop focada para gerenciar e resolver essas solicitações.

## Visão Geral da Arquitetura

O projeto é construído sobre uma filosofia **API-First**, onde um backend robusto e centralizado serve como a única fonte de verdade para múltiplos clientes.

* **Backend (Servidor API):** O núcleo do sistema, construído em **Java com Spring Boot**, expõe uma API RESTful segura para todas as operações. Ele gerencia a lógica de negócio, a persistência de dados e a autenticação.
* **Clientes (Frontends):**
    * **Cliente Web:** Uma interface web para os clientes finais abrirem e gerenciarem seus chamados.
    * **Cliente Desktop:** Uma aplicação nativa (`.exe`) para os técnicos da equipe de suporte, garantindo um ambiente de trabalho focado e restrito.
    * **Cliente Mobile:** Uma futura extensão para oferecer aos clientes acesso através de seus smartphones iOS e Android.

## Funcionalidades Principais

### Área do Cliente (Web & Mobile)
-   Cadastro e autenticação de usuários.
-   Dashboard principal para navegação rápida.
-   Criação de novos chamados com título, descrição, prioridade e múltiplos anexos.
-   Visualização da lista de chamados pessoais com seus respectivos status.
-   Acesso à página de detalhes de cada chamado, com todo o histórico e arquivos.
-   Capacidade de editar chamados existentes e adicionar mais anexos.
-   Download de arquivos anexados.

### Área do Técnico (Desktop)
-   Autenticação segura via API.
-   Visualização de uma fila com todos os chamados dos clientes.
-   Capacidade de responder aos chamados, alterar seu status (ex: de 'Aberto' para 'Em Andamento') e adicionar notas internas.
-   Download dos anexos enviados pelos clientes para análise.

## Pilha de Tecnologias (Technology Stack)

* **Backend:**
    * Java 17+
    * Spring Boot 3.x
    * Spring Security (Autenticação com JWT para API e Form Login para Web)
    * Spring Data JPA / Hibernate
    * Maven
* **Frontend (Web - Cliente):**
    * Thymeleaf
    * HTML5 / CSS3
    * Bootstrap 5
* **Frontend (Desktop - Técnico):**
    * JavaFX
* **Banco de Dados:**
    * Qualquer banco de dados relacional (ex: MySQL, MariaDB, PostgreSQL).
