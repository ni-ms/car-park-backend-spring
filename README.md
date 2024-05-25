# Car_Park_Spring
## Connect to the database by changing applications.properties file in src/main/resources . For example
```
spring.datasource.url=jdbc:mysql://localhost:3306/  <location of database>
spring.datasource.username=  <db username>
spring.datasource.password=  <db password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.devtools.restart.exclude = META-INF/maven/,META-INF/resources/,resources/,public/,templates/,**/*Test.class,**/Tests.class,git.properties,META-INF/build-info.properties
logging.level.com.zaxxer.hikari=DEBUG

```
## Run the DemoApplication.java file to run the spring app
