## Decatrade
A simulated stock trading app using price from IEX

## Tools / Frameworks used

1. SpringBoot
2. Postgres
3. Lombok: to reduce boilerplate in java objects
4. Retrofit: For HTTP Requests
5. Rest-assured: More expressive api/integration testing
6. Auth0 JWT: For generating and validating JWT tokens
7. Swagger: For API documentation

##TODO
1. No UI: couldn't implement the frontend part of the task
2. Payment Integration
3. Better Exception management / handling

## Setup

1. **Clone the application**

	```bash
	git clone https://github.com/kreativecoder/decatrade.git
	cd decatrade
	```

2. **Create Postgres database**

	```bash
	create database decatrade
	```

3. **Change Postgres username and password**

	+ open `src/main/resources/application.yml` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties

4. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 5000.

	You can also package the application in the form of a `jar` file and then run it like so -

	```bash
	mvn package
	java -jar target/decatrade-0.0.1-SNAPSHOT.jar
	```
 
 5. **Access the API Docs by pointing your browser to**
    
    ```bash
    http://localhost:5000/swagger-ui.html
    ```
    
    
 
## Docker / Docker Compose
There is a docker-compose setup that makes getting a copy of this on your local environment faster. The docker compose runs both the app and an instance of postgres.

1. **Clone the application**

	```bash
	git clone https://github.com/kreativecoder/decatrade.git
	cd decatrade
	```
2. **Run docker-compose(assumes you have docker and docker-compose setup already.)**

    ```bash
    docker-compose up
    ```
   
## Heroku
An instance of the app is running on heroku, you can try it out [here](https://decatrade.herokuapp.com/swagger-ui.html)