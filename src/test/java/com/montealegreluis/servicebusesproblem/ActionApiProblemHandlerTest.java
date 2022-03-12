package com.montealegreluis.servicebusesproblem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

final class ActionApiProblemHandlerTest {
  @Test
  void it_gets_its_handler_mapping() {
    var mapping = new RequestMappingHandlerMapping();
    var handler = new ActionApiProblemHandler(mapping);

    var handlerMapping = handler.handlerMapping();

    assertEquals(mapping, handlerMapping);
  }
}
