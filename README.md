# Service Buses API Problem

Service Buses API Problem is a package that includes [commands and queries](https://github.com/MontealegreLuis/service-buses) information in [API problem](https://github.com/MontealegreLuis/api-problem-spring-boot) responses and their logging events in a Spring Boot application.

## Usage

The simplest integration would look as follows

```java
@ControllerAdvice
public final class ProblemErrorHandler 
    extends ActionApiProblemHandler 
    implements ActionThrowableAdvice {

  private final HandlerMapping mapping;

  public ProblemErrorHandler(
      @Qualifier("requestMappingHandlerMapping") HandlerMapping mapping) {
      super(mapping);
  }
}
```
