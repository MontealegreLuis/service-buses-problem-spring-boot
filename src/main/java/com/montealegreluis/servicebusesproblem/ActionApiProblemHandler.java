package com.montealegreluis.servicebusesproblem;

import com.montealegreluis.servicebusesproblem.validation.ActionValidationAdvice;
import org.springframework.web.servlet.HandlerMapping;

public abstract class ActionApiProblemHandler
    implements ActionThrowableAdvice, ActionValidationAdvice {
  private final HandlerMapping mapping;

  public ActionApiProblemHandler(HandlerMapping mapping) {
    this.mapping = mapping;
  }

  public HandlerMapping handlerMapping() {
    return mapping;
  }
}
