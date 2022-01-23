
# Platforma pentru alegerea temelor de licenta/disertatie

## How to use

### Prerequisites

 - **Git** installed
 - **Java** (version 8) installed
 - **Docker** installed
 - Optional: **Maven** installed

---

### Clone the repository

 - Clone the project repository using the following command:

	    git clone https://github.com/gabi-negoita/topic-management-platform.git

---

### Create docker container for database

 1. Go to the root folder of the project (where **docker-compose.yml** configuration file resides)
 2. Run the following command

		docker-compose up -d
	
	`-d` flag means *detached* - runs the process in background

	This will create a container based on the **docker-compose.yml** configuration file.

---

### Connect to the database

 - Use *MySQL Workbench* or other utility to connect to the database
 - Use the **3366 port** (which can be configured in the **docker-compose.yml** file) to connect to the database

![MySQL Workbench | Database connection](https://i.ibb.co/308r636/Screenshot-2021-11-02-212602.png)

If the connection is **successful**, a database/schema named **mtapo-db** should be visible and accessible.

---
### Build & Run the application

**You can skip these steps if you are using an IDE (e.g.: IntelliJ) in which you can build and run the application.**

1. Go to the root folder of the project (where **pom.xml** configuration file resides)
2. Build the application using the following command:
	
	If **Maven is installed** run:

		mvn clean install
	 
	
	If **Maven is NOT installed** run
	- For **Windows**: `mvnw.cmd clean install`
	
	- For **Linux**: `mvnw clean install`

	This will download the dependencies defined in the **pom.xml** file, build the project and run the tests.

3. Run the application using the following command:
	
	If **Maven is installed** run:

		mvn spring-boot:run
	
	
	If **Maven is NOT installed** run
	- For **Windows**: `mvnw.cmd spring-boot:run`
	
	- For **Linux**: `mvnw spring-boot:run`

	This command will run the application that can be accessed at: `http://localhost:8888`. 

	The port is the application runs at is **8888** and can be configured in the `src/main/resources/application.properties` file.

## Contributors

 - [**Adam-Papadatu Ionut Cristian**](https://github.com/TPzChris)
 - [**Bejan Catalina**](https://github.com/catalina-bejan)
 - [**Negoita Gavriil**](https://github.com/gabi-negoita)
 - [**Panaite Alexandru Cristi**](https://github.com/AlexPanaite)
 - [**Stratulat Diaconu Adriana**](https://github.com/astratul)