# Payroll

This repository has been created as a learning tool for getting to grips with spring boot.

## First build

After cloning this repository, run this command:

```
   mvn clean spring-boot:run
```

to retrieve the dependencies, compile and run the program for the first time.

```
  [...]
  2024-04-09T21:27:27.338+02:00  INFO 21340 --- [payroll] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
  2024-04-09T21:27:27.517+02:00  WARN 21340 --- [payroll] [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be per
  formed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
  2024-04-09T21:27:27.752+02:00  INFO 21340 --- [payroll] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
  2024-04-09T21:27:27.760+02:00  INFO 21340 --- [payroll] [           main] payroll.ch.cpnv.es.PayrollApplication     : Started PayrollApplication in 2.972 seconds (process running for 3.247)
  2024-04-09T21:27:27.802+02:00  INFO 21340 --- [payroll] [           main] c.e.payroll.Repositories.LoadDatabase    : Preloading Employee{id=1, name='Bilbo Baggins', role='burglar'}
  2024-04-09T21:27:27.803+02:00  INFO 21340 --- [payroll] [           main] c.e.payroll.Repositories.LoadDatabase    : Preloading Employee{id=2, name='Frodo Baggins', role='thief'}
  [...]
```

## Test using http requests manually

Got the file [project]\src\main\java\ch\cpnv\es\payroll\Controllers\EmployeeGet.java

Before all routes, you will find a curl sample.

## Run tests

```bash
mvn test
```

```bash
mvn -Dtest=EmployeeGetTest test
```

## Run the api with Docker

* Build the image

```docker
docker build -t ms-payroll-employees-get:latest .
```

* How to run the test inside the container ?

The multi-stage Dockerfile integrates a test stage that prevents image creation unless all tests pass.

* Run the image

```
docker run -p 8080:8080 --name ms-payroll-employees-get ms-payroll-employees-get:latest
```
