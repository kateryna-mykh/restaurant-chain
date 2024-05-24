# backend-ts
This is an node js project using typescript, consul, express, mongoose, mocha.

## Endpoints
- GET: http://localhost:8888/ping (For base check - response will be `PONG`)  
- GET: http://localhost:8888/api/reviews/{id}?from={from}&size={size} (Get all reviews by specific restaurant id, sorted from newest to oldest by date. Default `from` is 0, default `size` is 10)  
- POST: http://localhost:8888/api/reviews (Create a review)
- POST: http://localhost:8888/api/reviews/_counts (Return report with counting number of reviews by restaurant's ids)

## Input file examples
Request to /api/reviews

```
{
    "text": "test",
    "phoneNumber": "0994414063",
    "restaurantId": 2,
    "name": "test",
    "mark": 7
}
```

Request to /api/reviews/_counts

```
{ 
   "ids": [1,2,3]
} 
```

## How launch this project
1. This project depends from `backend` module. First run with docker `backend` project.
2. Run following commands in the terminal from `backend-ts` module:

```
npm install

npm run build

docker-compose up
```

If image `backend-ts` not running in container - run it;


## Tests
Test coverage - 86.04%
