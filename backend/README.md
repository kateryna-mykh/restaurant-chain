# restaurant-chain app
Backend part.

## Technologies
* Java 17
* Spring Boot, Spring Data JPA
* Docker
* PostgeSQL
* Liquibase

## Endpoints

- POST /api/restaurants (Create a restaurant)
- GET /api/restaurants/{id} (Get a specific restaurant)
- PUT /api//restaurants/{id} (Update a restaurant)
- DELETE /api/restaurants/{id} (Delete a restaurant)
- POST /api/restaurants/_list (Get restaurants by query params)
- POST /api/restaurants/_report (Generate report file in CSV by query params with restaurants)
- POST /api/restaurants/upload (Upload restaurants from JSON) 

For upload endpoint you can find example file here `test/resources/database/test-data-set-10-entities.json`


- POST /api/restaurant-chains (Create a new chain)
- GET /api/restaurant-chains (Get all chains)
- PUT /api/restaurant-chains/{id} (Update a chain)
- DELETE /api/restaurant-chains/{id} (Delete a chain)

##Input file examples

Request to /api/restaurants/_list:
Can be empty. If it is, return all restaurants. Search by `address` field is case insensible.
Default page is 0, default size is 10.
 
```
{ 
    "address": "wood",
    "chainId": 2,
    "page": 0,  
    "size": 5  
} 
```
Pay attention: `locationAddress` in restaurants and `name` in restaurants chain are unique.
Request 

```
{
  "locationAddress": "",
  "manager": "",
  "seetsCapacity": 10, 
  "employeesNumber":,
  "restaurantChainId":, 
  "menuItems":
}

```

## How lanch this project
Execute all commands from `backend` project folder.
1. Configure your database settings in the .env file.
2. Build the project with Maven.
3. Run DB with Docker Compose.
4. Run the app.

Fill .env file with following environment variables:

```
POSTGRES_USER=
POSTGRES_PASSWORD=
DB_LOCAL_PORT=
DB_DOCKER_PORT=
SPRING_LOCAL_PORT=
SPRING_DOCKER_PORT=
```

