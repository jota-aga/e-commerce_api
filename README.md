# Minha API

Esta API fornece acesso aos recursos do sistema X.  
Abaixo estão exemplos de uso, endpoints disponíveis e detalhes de autenticação.

## Base URL

```
/auth
```

## Autenticação

A API utiliza autenticação via token OAuth 2.0.

```
Authorization: Bearer <seu_token>
```

## Endpoints

### 1. Registrar usuário (ALL)

**GET** `/register`


**Corpo da requisição:**
```json
{
  "username": "joao@email.com",
  "password": "123456"
}
```

### 2. Logar usuário (ALL)

**GET** `/login`


**Corpo da requisição:**
```json
{
  "username": "joao@email.com",
  "password": "123456"
}
```

**Exemplo de resposta:**
```json
{
  "acessToken": "sdfasjdfaoskdjfnadfndjfa5sdfasdfasdf23a"
}

```

## Base URL

```
/product
```

## Endpoints

### 1. Salvar produto (ADMIN)

**POST** 


**Corpo da requisição:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml"
  "quantity": 50,
  "price": 15
}
```

### 2. Listar todos os produtos (ALL)

**GET**


**Exemplo de resposta:**
```json
[ 
  {
    "name": "Garrafa d'agua",
    "description": "black, 750ml",
    "quantity": 50,
    "price": 15
  },
  {
    "name": "mouse ryzer",
    "description": "black, 500dpi",
    "quantity": 20,
    "price": 30
  }
]
```

### 3. Procurar produto por id (ALL)

**GET** `/{id}`


**Exemplo de resposta:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml"
  "quantity": 50,
  "price": 15
}

```

### 4. Procurar produto pelo nome (ALL)

**GET** `/search-name`

**Request Param:**
```json
name: Garrafa d'gua
```

**Exemplo de resposta:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml"
  "quantity": 50,
  "price": 15
}
```
### 5. Procurar produto pela categoria (ALL)

**GET** `/search-category`

**Request Param:**
```json
categoryName: Garrafas
```

**Exemplo de resposta:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml"
  "quantity": 50,
  "price": 15
}
```
### 6. Deletar produto pelo id (ADMIN)

**DELETE** `/{id}`

### 7. Editar produto (ADMIN)

**PUT** `/{id}`


**Corpo da requisição:**
```json
{
  "name": "Garrafa d'agua",
  "description": "white, 750ml"
  "quantity": 50,
  "price": 15
}
```

## Base URL

```
/category
```
### 1. Salvar categoria (ADMIN)

**POST** 


**Corpo da requisição:**
```json
{
  "name": "Garrafas"
}
```
### 2. Editar categoria (ADMIN)

**PUT** `/{id}`


**Corpo da requisição:**
```json
{
  "name": "Copos"
}
```
### 3. Deletar categoria (ADMIN)

**DELETE** `/{id}`

### 4. Listar todas as categorias (ALL)

**GET**


**Exemplo de resposta:**
```json
[ 
  {
    "name": "Garrafas",
  },
  {
    "name": "Eletrônicos"
  }
]
```

## Base URL

```
/cart
```
### 1. Procurar carrinho por id (ADMIN)

**GET** `/{id}`



**Exemplo de resposta:**
```json
{
    "user": {
        "id": 2,
        "login": "maria",
        "password": "$2a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
        "roles": [
            {
                "id": 2,
                "name": "CLIENT"
            }
        ]
    },
    "cartItems": [
        {
            "product": {
                "name": "Redmi 12",
                "description": "256gb de disco rígido, 16gm de ram",
                "price": 5000.00,
                "categoryId": 1,
                "quantity": 0
            },
            "quantity": 2
        }
    ],
    "totalValue": 10000.00
}
```
### 2. Fazer checkout de um carrinho (ADMIN)

**POST** `/{id}`

## Base URL

```
/cart-item
```
### 1. Salva um item em um carrinho (ADMIN)

**POST** 

**Corpo da requisição:**
```json
{
  "cartId": 1,
  "productId": 1,
  "quantity": 2
}
```

### 2. Edita um item de um carrinho (ADMIN)

**PUT** `/{id}`

**Corpo da requisição:**
```json
{
  "cartId": 1,
  "productId": 2,
  "quantity": 3
}
```

### 3. Deleta um item de um carrinho por um id (ADMIN)

**DELETE**

## Base URL

```
/order
```
### 1. Salvar pedido (ADMIN)

**POST** 

**Corpo da requisição:**
```json
{
  "userId": 1
}
```

### 2. Deletar pedido (ADMIN)

**DELETE** `/{id}`

### 3. Procurar pedido por id de usuário (ADMIN)

**POST** `/{id}`

**Exemplo de resposta:**
```json
[
    {
        "user": {
            "id": 2,
            "login": "maria",
            "password": "$2a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
            "roles": [
                {
                    "id": 2,
                    "name": "CLIENT"
                }
            ]
        },
        "ordersItemResponse": [
            {
                "productName": "Iphone 17",
                "productDescription": "256gb de disco rígido, 16gm de ram",
                "productPrice": 2000.00,
                "quantity": 1
            }
        ],
        "createdAt": "2025-10-01",
        "totalValue": 2000.00
    }
]
```

### 4. Procurar pedido por data (ADMIN)

**POST** `/{id}`
**Request Param**
`date: 2025/09/30`

**Exemplo de resposta:**
```json
[
    {
        "user": {
            "id": 2,
            "login": "maria",
            "password": "$2a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
            "roles": [
                {
                    "id": 2,
                    "name": "CLIENT"
                }
            ]
        },
        "ordersItemResponse": [
            {
                "productName": "Iphone 17",
                "productDescription": "256gb de disco rígido, 16gm de ram",
                "productPrice": 2000.00,
                "quantity": 1
            }
        ],
        "createdAt": "2025-10-01",
        "totalValue": 2000.00
    }
]
```

### 5. Procurar todos os pedidos (ADMIN)

**POST** `/{id}`
**Request Param**
`date: 2025-10-01`

**Exemplo de resposta:**
```json
[
    {
        "user": {
            "id": 2,
            "login": "maria",
            "password": "$2a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
            "roles": [
                {
                    "id": 2,
                    "name": "CLIENT"
                }
            ]
        },
        "ordersItemResponse": [
            {
                "productName": "Iphone 17",
                "productDescription": "256gb de disco rígido, 16gm de ram",
                "productPrice": 2000.00,
                "quantity": 1
            }
        ],
        "createdAt": "2025-10-01",
        "totalValue": 2000.00
    },
    {
        "user": {
            "id": 2,
            "login": "joao",
            "password": "24a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
            "roles": [
                {
                    "id": 2,
                    "name": "CLIENT"
                }
            ]
        },
        "ordersItemResponse": [
            {
                "productName": "Redmi 12",
                "productDescription": "256gb de disco rígido, 16gm de ram",
                "productPrice": 3600,
                "quantity": 2
            }
        ],
        "createdAt": "2025-10-01",
        "totalValue": 2000.00
    }

]
```

## Base URL

```
/order-item
```
### 1. Salvar item em um pedido (ADMIN)

**POST**

**Corpo da requisição:**
```json
{
  "orderId": 1,
  "productName": "Iphone 17",
  "productDescription": "256gb de disco rígido, 16gm de ram",
  "productPrice": 2000.00,
  "quantity": 1
}
```

### 2. Editar um item de um pedido (ADMIN)

**PUT**

**Corpo da requisição:**
```json
{
  "orderId": 1,
  "productName": "Iphone 16",
  "productDescription": "256gb de disco rígido, 16gm de ram",
  "productPrice": 1800.00,
  "quantity": 3
}
```
### 3. Deletar um item de um pedido (ADMIN)

**DELETE** `/{id}`

## Base URL

```
/client
```
### 1. Salvar item no carrinho do client (CLIENT)
**POST** `/cart/item`

**Corpo da requisição:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

### 2. Editar item no carrinho do client (CLIENT)
**PUT** `/cart/item`

**Corpo da requisição:**
```json
{
  "productId": 1,
  "quantity": 3
}
```

### 3. Deletar item no carrinho do client (CLIENT)
**DELETE** `/cart/item/{id}`

### 4. Pegar carrinho do cliente (CLIENT)
**GET** `/cart`

```json
{
    "user": {
        "id": 2,
        "login": "maria",
        "password": "$2a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
        "roles": [
            {
                "id": 2,
                "name": "CLIENT"
            }
        ]
    },
    "cartItems": [
        {
            "product": {
                "name": "Redmi 12",
                "description": "256gb de disco rígido, 16gm de ram",
                "price": 5000.00,
                "categoryId": 1,
                "quantity": 0
            },
            "quantity": 2
        }
    ],
    "totalValue": 10000.00
}
```
### 5. Pegar todos os pedidos do cliente (CLIENT)
**GET** `/order`

```json
[
    {
        "user": {
            "id": 2,
            "login": "maria",
            "password": "$2a$10$aDBIgDH9orI.nWxiUw2Rt.vXrmmLk.iW.VStavlSZJMPXZj0kyHL2",
            "roles": [
                {
                    "id": 2,
                    "name": "CLIENT"
                }
            ]
        },
        "ordersItemResponse": [
            {
                "productName": "Iphone 17",
                "productDescription": "256gb de disco rígido, 16gm de ram",
                "productPrice": 2000.00,
                "quantity": 1
            }
        ],
        "createdAt": "2025-10-01",
        "totalValue": 2000.00
    }
]
```

### 5. Fazer o checkout no carrinho do cliente (CLIENT)
**GET** `/checkout`
