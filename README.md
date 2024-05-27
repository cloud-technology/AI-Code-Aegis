# AI-Code-Aegis
AI-Code-Aegis, based on the Spring AI framework, automatically generates comprehensive unit tests and optimizes code quality. It supports multiple languages, diverse testing scenarios, and integrates seamlessly with CI/CD tools like Azure DevOps.

## Introduction

[建立專案](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=3.3.0&packaging=jar&jvmVersion=17&groupId=io.github.cloudtechnology&artifactId=AICodeAegis&name=AICodeAegis&description=AI-Code-Aegis%2C%20based%20on%20the%20AI%20framework%2C%20automatically%20generates%20comprehensive%20unit%20tests%20and%20optimizes%20code%20quality.%20It%20supports%20multiple%20languages%2C%20diverse%20testing%20scenarios%2C%20and%20integrates%20seamlessly%20with%20CI%2FCD%20tools%20like%20Azure%20DevOps.&packageName=io.github.cloudtechnology.codeaegis&dependencies=devtools,lombok,configuration-processor,docker-compose,web,validation,actuator,distributed-tracing,prometheus,testcontainers,spring-shell)


## Test

```shell
./gradlew --no-daemon clean build
cp -f build/libs/AICodeAegis-0.0.1-SNAPSHOT.jar .
export 
java -jar ./AICodeAegis-0.0.1-SNAPSHOT.jar codeReview
```

## Reference

- [llm-apps-java-spring-ai](https://github.com/ThomasVitale/llm-apps-java-spring-ai)