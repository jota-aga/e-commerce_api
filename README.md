# API E-commerce

Esta API simula um sistema de e-commerce, oferecendo diferentes níveis de acesso para administradores e clientes. O Administrador possui controle total sobre o sistema e pode genrenciar todas as entidades presentes. Já o cliente vai possuir acesso só ao seu módulo, podendo gerenciar seu carrinho, ver todos os seus pedidos e também a partes que são abertar para os dois tipos de usuários.

Ao iniciar a aplicação, caso ainda não exista, é criado automaticamente um usuário administrador padrão:

Usuário: ecommerce_admin

Senha: joao789

O login retorna um token JWT, que deve ser utilizado para acessar os endpoints protegidos. O token de cada usuário libera a autorização que cada endpoint pede de acordo com sua função no sistema.

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

### 1. Criar um produto (ADMIN)

**POST** 

Cria um produto.

**Corpo da requisição:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml"
  "quantity": 50,
  "price": 15,
  "categoryId": 1
}
```

### 2. Listar todos os produtos (ALL)

**GET**

Lista todos os produtos.

**Exemplo de resposta:**
```json
[ 
  {
    "name": "Garrafa d'agua",
    "description": "black, 750ml",
    "category": "Garrafas",
    "quantity": 50,
    "price": 15
  },
  {
    "name": "mouse ryzer",
    "description": "black, 500dpi",
    "category": "Mouses",
    "quantity": 20,
    "price": 30
  }
]
```

### 3. Procurar produto por id (ALL)

**GET** `/{id}`

Retorna um produto do id recebido.

**Exemplo de resposta:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml",
  "category": "Garrafas",
  "quantity": 50,
  "price": 15
}

```

### 4. Procurar produto pelo nome (ALL)

**GET** `/search-name`

Retorna um produto do nome recebido.

**Request Param:**
```json
name: Garrafa d'gua
```

**Exemplo de resposta:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml",
  "category": "Garrafas",
  "quantity": 50,
  "price": 15
}
```
### 5. Procurar produto pela categoria (ALL)

**GET** `/search-category`

Retorna uma lista de produtos da categoria recebida.

**Request Param:**
```json
categoryName: Garrafas
```

**Exemplo de resposta:**
```json
{
  "name": "Garrafa d'agua",
  "description": "black, 750ml",
  "category": "Garrafas",
  "quantity": 50,
  "price": 15
}
```
### 6. Deletar produto pelo id (ADMIN)

**DELETE** `/{id}`

Deleta um produto pelo id.

### 7. Editar produto (ADMIN)

**PUT** `/{id}`
Edita um produto pelo id.
**Corpo da requisição:**
```json
{
  "name": "Garrafa d'agua",
  "description": "white, 750ml"
  "quantity": 50,
  "price": 15,
  "categoryId": 1
}
```

## Base URL

```
/category
```
### 1. Criar uma categoria (ADMIN)

**POST** 

Cria uma categoria.

**Corpo da requisição:**
```json
{
  "name": "Garrafas"
}
```
### 2. Editar categoria (ADMIN)

**PUT** `/{id}`

Edita uma categoria pelo id recebido.

**Corpo da requisição:**
```json
{
  "name": "Copos"
}
```
### 3. Deletar categoria (ADMIN)

Deleta uma categoria pelo id.

**DELETE** `/{id}`

### 4. Listar todas as categorias (ALL)

Lista todas as categorias.

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

Retorna um carrinho pelo id recebido.

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
                "category": "Celular,
                "quantity": 0
            },
            "quantity": 2
        }
    ],
    "totalValue": 10000.00
}
```
### 2. Fazer checkout de um carrinho (ADMIN)

Faz um checkout de um carrinho pelo id recebido e salva o pedido automaticamente.

**POST** `/{id}`

## Base URL

```
/cart-item
```
### 1. Salvar um item em um carrinho (ADMIN)

**POST** `/{cartId}`

Salva um item em um carrinho.

**Corpo da requisição:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

### 2. Editar um item de um carrinho (ADMIN)

Edita um item de um carrinho pelo id recebido.

**PUT** `/{id}`

**Corpo da requisição:**
```json
{
  "productId": 2,
  "quantity": 3
}
```

### 3. Deletar um item de um carrinho por um id (ADMIN)

**DELETE** `/{id}`
Deleta um item de um carrinho pelo id.
## Base URL

```
/order
```
### 1. Salvar pedido (ADMIN)

**POST** `/{userId}`

Cria um pedido para um usuário.

### 2. Deletar pedido (ADMIN)

**DELETE** `/{id}`

Deleta um pedido pelo id.

### 3. Procurar pedidos por id de usuário (ADMIN)

**POST** `/{id}`

Retorna uma lista de pedidos pelo id de um usuário.

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

### 4. Procurar pedidos por data (ADMIN)

**POST** `/{id}`

Retorna uma lista de pedidos por uma data.

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

**GET** `/{id}`

Retorna uma lista de todos os pedidos existentes.

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

**POST** `/{orderId}`

Salva um item em um pedido.

**Corpo da requisição:**
```json
{
  "productName": "Iphone 17",
  "productDescription": "256gb de disco rígido, 16gm de ram",
  "productPrice": 2000.00,
  "quantity": 1
}
```

### 2. Editar um item de um pedido (ADMIN)

**PUT**

Edita um item de um pedido.

**Corpo da requisição:**
```json
{
  "productName": "Iphone 16",
  "productDescription": "256gb de disco rígido, 16gm de ram",
  "productPrice": 1800.00,
  "quantity": 3
}
```
### 3. Deletar um item de um pedido (ADMIN)

**DELETE** `/{id}`

Deleta um item de um pedido.

## Base URL

```
/client
```

Todos endpoints vão usar o token para conseguir manipular os dados do usuário logado, ou seja, como o token possui o id do usuário todos os métodos vão ser exclusivamente para aquele que está logado com o token recebido. Se o cliente tentar manipular dados que não seja dele, como tentar passar um id de um item de carrinho que não seja dele, não vai ser permitido.

### 1. Salvar item no carrinho do client (CLIENT)

**POST** `/cart/item`

Salva um item no carrinho do cliente logado.

**Corpo da requisição:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

### 2. Editar item no carrinho do client (CLIENT)

**PUT** `/cart/item/{cartItemId}`

Edita um item no carrinho do cliente.

**Corpo da requisição:**
```json
{
  "productId": 1,
  "quantity": 3
}
```

### 3. Deletar item no carrinho do client (CLIENT)

**DELETE** `/cart/item/{cartItemId}`

Deleta um item di carrinho do cliente.


### 4. Procurar carrinho do cliente (CLIENT)

**GET** `/cart`

Retorna o carrinho do cliente.

```json
{
    "cartItems": [
        {
            "product": {
                "name": "Iphone 17",
                "description": "256gb de disco rígido, 16gm de ram",
                "price": 2000.00,
                "category": "Eletrônicos",
                "quantity": 0
            },
            "quantity": 1
        }
    ],
    "totalValue": 2000.00
}
```
### 5. Procurar todos os pedidos do cliente (CLIENT)

**GET** `/order`

Retorna uma lista dos pedidos do cliente.

```json
[
    {
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

Faz o checkout do carrinho do cliente e cria salva o pedidos automaticamente.
