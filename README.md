# spring-on-quarkus-demo project

This project uses Quarkus, the Supersonic Subatomic Java Framework.
This project has been used to present Quarkus in several conferences and meetups.

This project is the result of following the [Quarkus Spring Web](https://quarkus.io/guides/spring-web) and [Quarkus Spring Data JPA](https://quarkus.io/guides/spring-data-jpa) guides.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Spring on Quarkus Live coding
### Project generation and run

Generate project navigating to this url https://code.quarkus.io/?a=spring-with-quarkus&e=spring-web&e=spring-data-jpa&e=smallrye-openapi&e=jdbc-postgresql&extension-search=postgre
Download the zip and unzip it. 

Navigate to the directory and launch the application
```bash
cd spring-with-quarkus
mvn compile quarkus:dev
```
1. Open browser to http://localhost:8080
1. Open browser to http://localhost:8080/greeting

### Add an url param

1. Add a `@RequestParam` to hello method: 
    ```
    @GetMapping
    public String hello(@RequestParam(defaultValue = "world")String name) {
        return "hello "+name;
    }
    ```
1. Open browser to `http://localhost:8080/greeting?name=folks`


### Introduce a bean

1. Create class `Greeting` in the `org.acme.spring.web` package with the following content:
    ```
    package org.acme.spring.web;
    
    public class Greeting {
    
        private String message;
    
        public Greeting(String message) {
            this.message = message;
        }
    
        public String getMessage() {
            return message;
        }
    }
    ```            
2. Update the content of `GreetingController` to become:
    ```
    package org.acme.spring.web;
    
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;
    
    
    @RestController
    @RequestMapping("/greeting")
    public class GreetingController {
    
        @GetMapping
        public Greeting hello(@RequestParam(defaultValue = "world")String name) {
            return new Greeting("hello "+name);
        }
    }
    ```    
3. Open browser to http://localhost:8080/greeting?name=folks

### Introduce a Service

1. Create class `GreetingService` in the `org.acme.spring.web` package with the following content:
    ```
   package org.acme.spring.web;
   
   import org.springframework.stereotype.Service;
   
   public class GreetingService {
   
       public Greeting greet(String name){
           return new Greeting(name);
       }
   }
    ```            
2. Update the content of `GreetingController` to become:
    ```
    package org.acme.spring.web;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;
    
    
    @RestController
    @RequestMapping("/greeting")
    public class GreetingController {
    
        @Autowired
        private GreetingService greetingService;
    
        @GetMapping
        public Greeting hello(@RequestParam(defaultValue = "world")String name) {
            return greetingService.greet("hello "+name);
        }
    }
    ```    
3. Open browser to http://localhost:8080/greeting?name=folks
1. We get an error because we have not made the Service class a bean.
1. Modify the service class to add an annotation to make it a bean, for instance `@Service`
 ```
   package org.acme.spring.web;
   
   import org.springframework.stereotype.Service;
   
   @Service
   public class GreetingService {
   
       public Greeting greeting(String name){
           return new Greeting(name);
       }
   }
   ```
1. Refresh browser


### Externalize values to configuration file

1. In the `GreetingService`, add a `message` field.
1. Use Constructor injection with `@Value` annotation which is what the Spring world use to use.
1. Change the `greet` method to use the message field.
 
    ```
    package org.acme.spring.web;
    
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    
    @Service
    public class GreetingService {
    
        private String message;
    
        public GreetingService(@Value("${greeting.message}")String message) {
            this.message = message;
        }
    
        public Greeting greet(String name){
            return new Greeting(message + " " + name);
        }
    }
    ```
1. Open browser to http://localhost:8080/greeting?name=folks
1. We get an error because we have not added the property `greeting.message` to the configuration file
1. Open the `application.properties` file and add:
    ```
    greeting.message=hola
    ``` 
1. Refresh browser


## Accessing data with Spring Data Jpa 

### Add needed quarkus extensions dependencies

1. Open the `pom.xml` and add the `quarkus-spring-data-jpa` and `quarkus-jdbc-postgresql`
    ```
        <dependency>
          <groupId>io.quarkus</groupId>
          <artifactId>quarkus-spring-data-jpa</artifactId>
        </dependency>
        <dependency>
          <groupId>io.quarkus</groupId>
          <artifactId>quarkus-jdbc-postgresql</artifactId>
        </dependency>
    
    ```

You can also add the extensions to your project by running the following command in your project base directory

```bash
./mvnw quarkus:add-extension -Dextensions="quarkus-spring-data-jpa,quarkus-jdbc-postgresql"
```

### Create entity, repository and controller classes

1. Create class `Book` in the `org.acme.spring.web` package with the following content:
    ```
    package org.acme.spring.web;

    import io.quarkus.hibernate.orm.panache.PanacheEntity;
    import jakarta.persistence.Entity;
       
    @Entity
    public class Book extends PanacheEntity {
       
    public String name;
       
    public Integer publicationYear;
       
    }
    ```
   1. Create a `BookRepository` interface and make it a Spring repository extending the Spring `CrudRepository`

       ```
       package org.acme.spring.web;
    
       import org.springframework.data.repository.CrudRepository;
    
       import java.util.List;
    
       public interface BookRepository extends CrudRepository<Book, Long> {
    
       }
    
       ```
      1. Create the `BookController` class in order to expose the BookRepository via REST.

          ```
          package org.acme.spring.web;
    
    
          import org.springframework.web.bind.annotation.*;
    
          import java.util.List;
    
          @RestController
          @RequestMapping("/book")
          public class BookController {
    
              @Autowired
              private BookRepository bookRepository;
    
              @GetMapping
              public Iterable<Book> findAll() {
                  return bookRepository.findAll();
              }
          }
          ```

### Set up the data base and some data in it

As Quarkus supports the automatic provisioning of unconfigured services in development and test mode, we don't need at the moment to configure anything regarding the database access.
Quarkus will automatically start a Postgresql service and wire up your application to use this service.
However, this database is empty. To add some books, follow the next step:

1. Add database population script `import.sql` in resources folder with the following content
````properties
    INSERT INTO book(id, name, publicationYear) VALUES (1, 'Sapiens' , 2011);
    INSERT INTO book(id, name, publicationYear) VALUES (2, 'Homo Deus' , 2015);
    INSERT INTO book(id, name, publicationYear) VALUES (3, 'Enlightenment Now' , 2018);
    INSERT INTO book(id, name, publicationYear) VALUES (4, 'Factfulness' , 2018);
    INSERT INTO book(id, name, publicationYear) VALUES (5, 'Sleepwalkers' , 2012);
    INSERT INTO book(id, name, publicationYear) VALUES (6, 'The Silk Roads' , 2015);
    ALTER SEQUENCE book_seq RESTART WITH 7;
````

1. Open browser to http://localhost:8080/book

## Add a custom method in the repository

1. Modify the `BookRepository` to add a custom method allowing retrieve books between publication dates.
    ```
    package org.acme.spring.web;
    
    import org.springframework.data.repository.CrudRepository;
    
    import java.util.List;
    
    public interface BookRepository extends CrudRepository<Book, Long> {
    
        List<Book> findByPublicationYearBetween(Integer lower,Integer higher);
    }
    
    ```

1. Add the following method to the `BookController`

    ```
    @GetMapping("year/{lower}/{higher}")
        public List<Book> findByPublicationYear(@PathVariable Integer lower, @PathVariable Integer higher){
            return bookRepository.findByPublicationYearBetween(lower,higher);
        }
    
    ```

1. Open browser to http://localhost:8080/book/year/2012/2015

## Add an Exception to customize responses

1. Add a delete method in the `BookController` as follows:

    ```
        @DeleteMapping("/{id}")
        public void deleteBook(@PathVariable Long id){
                bookRepository.deleteById(id);
        }
    ```

1. Try to delete the book with `id` 10 by running the following command
    ```
     curl -X DELETE localhost:8080/book/10
    ```
    We get an `500 Internal Server Error` because any book with id 10 exist.

1. Create a `MissingBookException` class with following content:

    ```
    package org.acme.spring.web;
    
    import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.ResponseStatus;
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class MissingBookException extends RuntimeException {
    }
    
    ```

1. Use this custom Exception in the delete method in the `BookController`
    ```
    @DeleteMapping("/{id}")
        public void deleteBook(@PathVariable Long id){
            try {
                bookRepository.deleteById(id);
            } catch (Exception e) {
                throw new MissingBookException();
            }
        }
    ```

1. Retry to delete the book with `id` 10 by running the following command
    ```
    http DELETE localhost:8080/book/10
    ```
    We get a `Bad Request` response
   ```
   HTTP/1.1 400 Bad Request
   Content-Length: 0
   Content-Type: text/plain
   ```


## Packaging and running the application
### Database configuration for PROD environment

Open the `application.properties` file and add database access configuration
````properties
%prod.quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/quarkus-library
%prod.quarkus.datasource.username=book
%prod.quarkus.datasource.password=book

%prod.quarkus.datasource.jdbc.min-size=2
%prod.quarkus.datasource.jdbc.max-size=8
````

1. Configure the loading of data adding the following properties in the `application.properties` file

```properties
%prod.quarkus.hibernate-orm.sql-load-script=import.sql
%prod.quarkus.hibernate-orm.database.generation=drop-and-create
```

1. At last, start a postgresql database by running the following command:

```bash
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus-database -e POSTGRES_USER=book -e POSTGRES_PASSWORD=book -e POSTGRES_DB=quarkus-library -p 5432:5432 postgres:14.5
```

As already mentioned, these steps are optional in `dev` and `test` modes.

1. Open browser to http://localhost:8080/book

The application can be packaged using `./mvnw package`.
It produces several outputs:
- the `spring-on-quarkus-demo-1.0.0-SNAPSHOT.jar` file in the `/target` directory.
- the quarkus-app directory which contains the `quarkus-run.jar`

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.


## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.
You can then execute your native executable with: `./target/spring-on-quarkus-demo-1.0-SNAPSHOT-runner`

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.
Then, build the docker image with `docker build -f src/main/docker/Dockerfile.native -t quarkus/spring-on-quarkus-demo .`
Finally, run the container using `docker run -i --net=host --rm -p 8080:8080 quarkus/spring-on-quarkus-demo`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.