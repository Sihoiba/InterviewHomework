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
```

java -jar interviewHomework-0.0.1-SNAPSHOT.jar

## API EndPoints

### 1a Populate DB with video details

POST http://localhost:8080/interview-homework/populateVideos

### 1b Get All Videos

GET http://localhost:8080/interview-homework/videos

### 2 Get Video by Id

GET http://localhost:8080/interview-homework/videos/[ID]

### 3 Delete Video by Id

DELETE http://localhost:8080/interview-homework/videos/[ID]

### 4 Search videos by search term
GET http://localhost:8080/interview-homework/searchVideos?searchTermType=[TERM]&value=[VALUE TO SEARCH FOR]

Currently the only supported search terms are 'TITLE' allowed searching on Titles containing the [VALUE TO SEARCH] (case insensitive)
 