# API Documentation

## Endpoints

### Produtos

#### GET /produtos
Lista todos os produtos cadastrados.

**Resposta**
```json
[
  {
    "id": 1,
    "nome": "Produto 1",
    "preco": 10.50
  }
]
```

#### POST /produtos
Cadastra um novo produto.

**Requisição**
```json
{
  "nome": "Novo Produto",
  "preco": 15.90
}
```

### Clientes (Usuários)

#### POST /usuarios/criar
Cadastra um novo cliente.

**Requisição**
```json
{
  "nome": "Nome Cliente",
  "email": "cliente@email.com",
  "senha": "123456",
  "endereco": "Rua Exemplo, 123",
  "telefone": "(11) 98765-4321"
}
```

#### GET /usuarios/todos
Lista todos os clientes cadastrados.

### Compras

#### POST /compras
Registra uma nova compra.

**Requisição**
```json
{
  "clienteId": "uuid-do-cliente",
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
```

#### GET /compras/detalhes?clienteId={uuid}
Retorna o histórico detalhado de compras de um cliente.

**Resposta**
```json
[
  {
    "id": 1,
    "dataCompra": "2025-05-10T14:30:00",
    "valorTotal": 31.80,
    "itens": [
      {
        "nomeProduto": "Produto 1",
        "quantidade": 2,
        "precoUnitario": 15.90,
        "subtotal": 31.80
      }
    ]
  }
]
```

## Modelos de Dados

### Produto
- `id`: Integer (PK)
- `nome`: String
- `preco`: Double

### Usuario
- `id`: UUID (PK)
- `nome`: String
- `email`: String (unique)
- `senha`: String
- `endereco`: String
- `telefone`: String

### Compra
- `id`: Integer (PK)
- `cliente`: Usuario (FK)
- `dataCompra`: LocalDateTime
- `valorTotal`: BigDecimal
- `itens`: List<CompraItem>

### CompraItem
- `id`: CompraItemPK (compraId + produtoId)
- `compra`: Compra
- `produto`: Produto
- `quantidade`: Integer
- `precoUnit`: BigDecimal
