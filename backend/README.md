# restorant-chain app

Fill .env file fith folowing environment veriables:

```
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
DB_LOCAL_PORT=
DB_DOCKER_PORT=

SPRING_LOCAL_PORT=
SPRING_DOCKER_PORT=
```


## Endpoints

- POST /api/restorants (Create a restorant)
- GET /api/restorants/{id} (Get a specific restorant)
- PUT /api//restorants/{id} (Update a restoarnt)
- DELETE /api/restorants/{id} (Delete a restorant)
- POST /api/restorants/_list (Get restorants by query params)
- POST /api/restorants/_report (Generate report file in CSV by query params with restorants)
- POST /api/restorants/upload (Upload restorants from JSON) 


- POST /api/restorant-chains (Create a new chain)
- GET /api/restorant-chains (Get all chains)
- PUT /api/restorant-chains/{id} (Update a chain)
- DELETE /api/restorant-chains/{id} (Delete a chain)


docker-compose -f docker-compose-postgre.yml up -d

