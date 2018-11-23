# Movies watched #

This is a simple Java rest application which stores details related to movies seen.
It is intended  as an example standard Java application set up. 

### How do I get set up? ###

1. Clone the repository
2. Run ```./moviesWatched.sh start```
    * This will build the project, and launch the docker containers
3. ```./moviesWatched.sh stop```
    * When done, this will shut down the servers in question


### Access the APIs ###
When running the details of the APIs can be found here ```http://localhost:8080/swagger-ui.html#/movie-controller```

### Technologies used ###
* Java
* Spring Boot
* Gradle
* Docker
* Postgres
* Liquibase
* Swagger2
* Project Lombok
* Junit
* Mockito

### Areas of potential improvement ###
* Adding NGINX or Kong to docker-compose and have service sit behind the api gateway
* There is no security on the APIs implemented
* Testing of the Controller layer using MockMvc
* Test should be run as part of the build script