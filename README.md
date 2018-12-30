# InterviewHomework

Application for Interview.

## Build

Requires Java 1.8 and maven 3.6

mvn clean install 

To start the application using mvn clean spring-boot:run

## To run

### Against in memory DB

java -jar interviewHomework-0.0.1-SNAPSHOT.jar --interview-homework.youtube.api.token=[tokenvalue]

### Against your own DB (with the youtube.sql applied)

Create an application.properties file in a directory named config where you are the running the application. 

The contents of the application.properties should be as follows
```
######## Web Configuration ########
server.servlet.context-path=/interview-homework
server.port=8080

######## Data source ########
# The connection string to the Youtube database. e.g. jdbc:mysql://127.0.0.1:3306/youtube
spring.datasource.url=[URL]
spring.datasource.driverClassName=[DRIVER]
spring.datasource.username=[DB username]
spring.datasource.password=[DB password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect #Replace with the value for your DB type

######## Application properties ########
interview-homework.youtube.api.token=[youtube apu token
interview-homework.youtube.search.filter.name=[name of the search filter file]
```

java -jar interviewHomework-0.0.1-SNAPSHOT.jar

 