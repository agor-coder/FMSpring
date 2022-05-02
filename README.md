## FaultsManagement
- JDK 16
- Spring Boot 2.5.4
- JPA & Hibernate
- PostgreSQL
- Flyway 8.1.0
- Docker
- Lombok 1.18.22
- Spring Security
- REST API
- Swagger
- Actuator
- unit and integration tests
- deploy on Heroku
 
#### Purpose and scope of the project 
The system is designed to support the process of reporting and handling operational faults in the heat and power plant; allows to report current faults in a specific department, assign work related to the removal of a fault to a specific specialist, change the assigned specialist, change the status of a fault in accordance with the current status of work.
The system consists of a web application created on the Spring Boot platform and a relational database. The three-layer model made it possible to separate the view layer of the business logic layer and the data storage layer in a relational database.
The functionalities of the application are divided into four levels of access. The administrator manages the accounts (adding, deleting, editing, changing the password, activating, confirming), the notifier  reports the fault, the assigner assigns a specialist to the fault,
the specialist after removing the fault changes its status to "completed".
In addition, the assigner has access to the list of specialists - he can change the current specialist's assignment and to the list of plant zones to which the reported defects are assigned.
Unauthenticated user can register as a fault notifier.

The system allows access to data of many simultaneously authenticated users. Maintaining the consistency of the processed data is achieved thanks to transactional processing and the use of optimistic locks. Application security is ensured by an authentication and accountability control process.
The system provides authentication, authorization, error handling, validation, optimistic locks, object-relational mapping, database version control, event logging.

user: "admin"
password: "123"
