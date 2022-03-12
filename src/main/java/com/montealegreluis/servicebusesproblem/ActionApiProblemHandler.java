package com.montealegreluis.servicebusesproblem;

import org.springframework.web.servlet.HandlerMapping;

public class ActionApiProblemHandler {
  private final HandlerMapping mapping;

  public ActionApiProblemHandler(HandlerMapping mapping) {
    this.mapping = mapping;
  }

  public HandlerMapping handlerMapping() {
    return mapping;
  }
}
