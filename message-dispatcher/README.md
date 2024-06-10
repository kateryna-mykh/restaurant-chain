# message-dispatcher service

Java microservice that is responsible for sending email messages.  
The service receives these messages asynchronously through the Message Broker.  
Stores the message in its database and tries to send it to the mail server using the SMTP protocol.

## Technologies
* Java 17
* Spring Boot, Spring Data JPA
* Docker
* ElasticSearch
* Kibana
* RabbitMQ

## How launch this project
1. Configure your settings in the .env file.
2. Build the project with Maven. (For now build with skipping tests `mvn clean install -DskipTests`)
3. Build and run with Docker Compose.
4. Launch`backend` module.
5. Use Postman endpoint POST /api/restaurants.

Fill .env file with following environment variables:

```
SPRING_LOCAL_PORT=
SPRING_DOCKER_PORT=
SPRING_MAIL_USERNAME=
SPRING_MAIL_PASSWORD=
```

## Notes

For SPRING_MAIL use gmail email and app password. Follow [this guide](https://support.google.com/mail/answer/185833?hl=en).

Your antivirus program can block sending email messages. Turn it off while the app is being used.  
Or uncomment `spring.mail.properties.mail.smtp.ssl.trust` property in the application.properties file.
