# Introduction

This is a small web application that I've written while studying Spring and Kotlin through [this](https://www.udemy.com/course/kotlin-spring/) Udemy course.
I have changed many things compared to the instructor's original source though, mainly because I tried to follow Clean Architecture principles.
Indeed, this is my take on how to build a Monolithic application following Clean Architecture.

This is how the source code is structured:

```
* core            - The heart and soul of this app. High level policies and its design are coded here. 
  * entities      - The entities involved in the app's core, like Book and Customer for example. They should not depend on anything else in the entire app.
  * usecases      - The use cases provided by the app's core.
    * ports       - Interfaces required by the use cases in order for them to deliver their functionality (see Ports and Adapters architecture).
* configuration   - Classes needed to configure the Spring App properly
* adapters        - Implementations of core ports for the particular delivery mechanisms currently in use in the app (JPA and some Spring utils).
* controller      - All controller layer as required in an MVC architecture. Spring is our chosen delivery mechanism here.
```

The source code is fully covered by unit tests (except by configuration code). JaCoCo is configured to output coverage information for all packages and classes, except for `com.mercadolivro.configuration`.

`./gradlew test` will fail if 100% coverage of instructions and branches is not reached.