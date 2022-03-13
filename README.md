# Service Buses API Problem Spring Boot

[![CI workflow](https://github.com/montealegreluis/service-buses-problem-spring-boot/actions/workflows/ci.yml/badge.svg)](https://github.com/montealegreluis/service-buses-problem-spring-boot/actions/workflows/ci.yml)
[![Release workflow](https://github.com/montealegreluis/service-buses-problem-spring-boot/actions/workflows/release.yml/badge.svg)](https://github.com/montealegreluis/service-buses-problem-spring-boot/actions/workflows/release.yml)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventionalcommits-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)


Service Buses API Problem is a package that includes [commands and queries](https://github.com/MontealegreLuis/service-buses) information in [API problem](https://github.com/MontealegreLuis/api-problem-spring-boot) responses and their logging events in a Spring Boot application.

## Installation

1. [Authenticating to GitHub Packages](https://github.com/MontealegreLuis/service-buses-problem-spring-boot/blob/main/docs/installation/authentication.md)
2. [Maven](https://github.com/MontealegreLuis/service-buses-problem-spring-boot/blob/main/docs/installation/maven.md)
3. [Gradle](https://github.com/MontealegreLuis/service-buses-problem-spring-boot/blob/main/docs/installation/gradle.md)

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
