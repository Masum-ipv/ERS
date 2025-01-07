# ERS

The goal of this project is to create a Java Full Stack Employee Reimbursement System (ERS). User can be registered as
either an Employee or a Manager. After successfully registering, the user will be notified via email and SMS.
The main use case of the ERS centers around Employees submitting Reimbursements that can either be accepted or denied by
Managers.

The tech stack will consist of a React-Based Front end, communicating via HTTP to a Spring-Based Back end. The database
will be either a local or cloud-based Postgres database. The entire application will have error handling in place to
prevent the user from invoking unauthorized functionalities or entering invalid inputs.

## Technologies Used

- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- React
- Redux
- RebbitMQ
- Junit
- Mockito
- Logback
- Maven
- Docker
- Java Mail API
- Twilio API
- JWT

## Docker Setup

- Install Docker Hub Desktop Application
- Add Docker Image for `PostgreSQL`, `RabbitMQ`, and `Redis`

## How to Run

- Clone the repository
- Open Docker Desktop Application
- `cd` into `P1Backend` and run spring project from IDE
- `cd` into `P1FrontEndReact` and run react project from terminal using `npm run dev`
- Open browser and navigate to `localhost:5173`



#### How to view postgreSQL data

- User `DBeaver` as a database viewer client
- Connect to the database using the following credentials:
    - URL: `jdbc:postgresql://localhost:5432/postgres`
    - Username: `postgres`
    - Password: `password`

#### How to view RabbitMQ data

- User `RabbitMQ` as a queue viewer client
- Connect to the queue using the following credentials:
    - URL: `http://localhost:15672`
    - Username: `username`
    - Password: `password`

#### How to view Redis data

- User `RedisInsight` as a database viewer client
- Connect to the database using the following credentials:
    - URL: `http://localhost:8001`
    - Host: `redis`
    - Port: `6379`
    - Name: `MyRedisDB`