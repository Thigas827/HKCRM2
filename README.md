# HKCRM2 - Sistema de Gestão de Vendas

## Descrição
Este é um MVP (Minimum Viable Product) de um sistema de gestão de vendas desenvolvido com Spring Boot e interface web simples. O sistema permite o gerenciamento básico de produtos, clientes e vendas.

## Funcionalidades Principais

### 1. Gestão de Produtos
- Cadastro de produtos básicos (nome e preço)
- Listagem de produtos
- Busca de produtos por nome
- Suporte para diferentes tipos de produtos (produtos gerais e doces)

### 2. Gestão de Clientes
- Cadastro de clientes com:
  - Nome
  - Email (único)
  - Endereço
  - Telefone
- Sistema de login simplificado

### 3. Gestão de Vendas
- Registro de compras com múltiplos itens
- Histórico detalhado de compras por cliente
- Informações detalhadas de cada compra:
  - Data e hora
  - Itens comprados
  - Quantidade por item
  - Preço unitário
  - Subtotal por item
  - Valor total da compra

### 4. Interface do Usuário
- Design responsivo e minimalista
- Modo escuro/claro
- Navegação intuitiva entre funcionalidades
- Formulários de cadastro simplificados
- Visualização clara do histórico de compras

## Tecnologias Utilizadas

### Backend
- Java com Spring Boot
- Spring Security para autenticação básica
- JPA/Hibernate para persistência
- PostgreSQL como banco de dados
- Arquitetura em camadas (MVC)

### Frontend
- HTML5
- CSS3 
- JavaScript puro
- Interface single-page
- Comunicação via REST API

## Estrutura do Projeto

### Principais Pacotes
- `controller`: Endpoints da API REST
- `service`: Lógica de negócios
- `model`: Entidades do domínio
- `repository`: Acesso ao banco de dados
- `dto`: Objetos de transferência de dados
- `config`: Configurações do Spring

### Entidades Principais
- `Produto`: Base para produtos do sistema
- `Usuario`: Representa clientes
- `Compra`: Registra vendas
- `CompraItem`: Itens individuais de cada compra

## Como Executar

1. Configure o banco PostgreSQL (ver application.properties)
2. Execute o script SQL de criação das tabelas
3. Execute o comando: `mvnw spring-boot:run`
4. Acesse: http://localhost:8080