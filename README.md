# Spring Projects

## spring-boot-demo
- Trying out Spring Boot.
- Utilizes H2 for local storage, JPA, and Thymeleaf.

## spring-data-rest-demo
- Utilizing Spring Boot REST backend
- ReactJS frontend
- To run the app, make sure you have Maven installed and run this command **./mvnw spring-boot:run**
- http://localhost:8080/   --> displays employees
- http://localhost:8080/api/ --> displays api

## spring-mvc-form-tags-demo
- Utilizes Spring MVC with JSP
- Testing out form and form validators
![mvctesthome](https://user-images.githubusercontent.com/16873263/30838128-9276f4a8-a21e-11e7-9d50-724839c5c143.JPG)
************
![fromtagstestbed](https://user-images.githubusercontent.com/16873263/30838127-92745194-a21e-11e7-8150-c11eb9f3f00a.JPG)

## spring-security-and-angular-js
- Spring Security web app with AngularJS frontend
- Utilizes wro4j - https://github.com/wro4j/wro4j
  - Is a Java-based toolchain for preprocessing and packaging front end assets
- Created UI and resource servers that do not have a common origin, so they cannot share cookies (even though we can use Spring Session to force them to share sessions).
- Utilizes redis server via docker.  Use 'docker-compose up' to launch redis server.
- This app so far is a home page with a greeting fetched from a remote backend, with login and logout links in a navigation bar. The greeting comes from a resource server that is a standalone, instead of being embedded in the UI server.
- Used Spring Session here to share sessions between 2 servers that are not logically the same application. It’s a neat trick, and it isn’t possible with "regular" JEE distributed sessions.

## spring-boot-security-mysql-login
- mvn spring-boot:run
- Utilizes Spring Boot, Spring Security, Spring MVC, JPA, MySQL, Thymeleaf
- Encrypted password storage
- See README.md inside folder for screenshots
