# This is a demo payments API.
This service exposes endpoints for creating, updating, retrieving and deleting payment resources.

## How to:
1. To run the application open a terminal and run the following command: `mvn spring-boot:run`
2. Once the application starts, it will expose the available endpoints on the port `8080`

## API Usage Example
Below you can find examples of requests for each of the available endpoints

### Create a Payment
```
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust_12345",
    "amount": 100.0,
    "currency": "USD",
    "paymentMethodType": "CARD"
  }'
```

### Get All Payments
```
curl http://localhost:8080/api/payments
```

### Get Payment by id
```
curl http://localhost:8080/api/payments/{id}
```

### Update a Payment
```
curl -X PATCH http://localhost:8080/api/payments/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 150.0,
    "currency": "EUR",
    "paymentMethodType": "BANK_TRANSFER"
  }'
```

### Delete a Payment
```
curl -X DELETE http://localhost:8080/api/payments/{id}
```

Replace `{id}` with the actual payment id.
