# E-Commerce Backend API

A Spring Boot-based REST API for an e-commerce platform with product management, shopping cart, order processing, and mock payment integration.

## Tech Stack

- **Framework**: Spring Boot 4.0.1
- **Database**: MongoDB
- **Language**: Java 21
- **Build Tool**: Gradle

## Features

- ✅ Product Management (CRUD + Search)
- ✅ Shopping Cart Operations
- ✅ Order Processing with Stock Management
- ✅ Mock Razorpay Payment Integration
- ✅ Async Payment Processing with Webhook Callbacks
- ✅ Order History
- ✅ Comprehensive Error Handling

## Prerequisites

- Java 21+
- MongoDB (running on localhost:27017)
- Gradle 8+

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd E-commerce-backend
```

### 2. Configure MongoDB

Ensure MongoDB is running on `localhost:27017`. The default database name is `ecommerce_db`.

You can modify the connection in `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/ecommerce_db
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`

### 4. Build the Project

```bash
./gradlew build
```

## API Documentation

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create a new product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/search?q={query}` | Search products by name |

### Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cart/add` | Add item to cart |
| GET | `/api/cart/{userId}` | Get user's cart |
| DELETE | `/api/cart/{userId}/clear` | Clear user's cart |
| DELETE | `/api/cart/{userId}/item/{productId}` | Remove item from cart |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create order from cart |
| GET | `/api/orders/{orderId}` | Get order by ID |
| GET | `/api/orders/user/{userId}` | Get user's order history |
| POST | `/api/orders/{orderId}/cancel` | Cancel an unpaid order |

### Payments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments/create` | Initiate payment for order |
| GET | `/api/payments/{paymentId}` | Get payment by ID |
| GET | `/api/payments/order/{orderId}` | Get payment by order ID |

### Webhooks

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/webhooks/payment` | Payment callback (internal) |

## Sample Requests

### Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 50
  }'
```

### Add to Cart

```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "productId": "<product-id>",
    "quantity": 2
  }'
```

### Create Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123"
  }'
```

### Initiate Payment

```bash
curl -X POST http://localhost:8080/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "<order-id>"
  }'
```

## Payment Flow

1. **Create Order**: User creates an order from their cart
2. **Initiate Payment**: Call `/api/payments/create` with the order ID
3. **Async Processing**: Mock payment gateway processes payment (3-second delay)
4. **Webhook Callback**: Gateway calls `/api/webhooks/payment` with result
5. **Status Update**: Payment and order status are updated accordingly

### Payment Status Flow

```
PENDING → SUCCESS (Order: PAID)
        → FAILED (Order: FAILED)
```

## Order Status

| Status | Description |
|--------|-------------|
| CREATED | Order created, awaiting payment |
| PAID | Payment successful |
| FAILED | Payment failed |
| CANCELLED | Order cancelled |

## Mock Payment Configuration

Configure in `application.yml`:

```yaml
mock:
  payment:
    delay-ms: 3000      # Processing delay in milliseconds
    success-rate: 0.8   # 80% success rate
```

## Project Structure

```
src/main/java/org/example/ecommercebackend/
├── config/              # Configuration classes
├── controller/          # REST controllers
├── dto/
│   ├── request/         # Request DTOs
│   └── response/        # Response DTOs
├── exception/           # Custom exceptions & handlers
├── model/
│   └── enums/           # Status enums
├── repository/          # MongoDB repositories
└── service/
    └── impl/            # Service implementations
```

## Error Handling

All errors return a consistent JSON structure:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Descriptive error message",
  "path": "/api/endpoint",
  "timestamp": "2026-01-19T10:30:00Z",
  "fieldErrors": []
}
```

## License

This project is for educational purposes.

