# BioTrio
First year final project, implemented in Java Spring Boot with MySQL

# Application setup:
## Prerequisites:
- Java SDK (minimum version 8)
- Apache Maven
- MySQL / MariaDB running as a service
- Java and Maven binaries included in the system's path environment variable
## Steps:
1. Clone this repository to a local directory on your PC
2. With your favourite DBMS administration tool, execute the 'sql\1_create.sql' script as root
3. If you want some dummy data, execute the queries in 'sql\2_populate.sql'. We would strongly recommend running at least the very first query that creates the user accounts.
4. Open a terminal / command line and 'cd' to the directory where you cloned the repository
5. Execute 'mvn spring-boot:run'
6. Point your browset to 'http://localhost:8080/'
7. If you ran the query for creating the user accounts, you can log in with the 'Admin', 'Manager', and 'User' accounts, with the password '1234' on all three, and explore the application from the point of view of the three different permission levels. The fourth level is a customer's perspective, which is the equivalent of not being logged in.
