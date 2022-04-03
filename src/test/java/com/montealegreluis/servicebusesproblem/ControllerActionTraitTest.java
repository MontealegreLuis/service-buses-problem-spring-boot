package com.montealegreluis.servicebusesproblem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.montealegreluis.servicebuses.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

final class ControllerActionTraitTest {
  @Test
  void it_extracts_action_from_controller_name() {
    var builder = MockMvcRequestBuilders.get(path);
    var context = new MockServletContext();
    var nativeRequest = new ServletWebRequest(builder.buildRequest(context));

    var action = controllerAction.actionFrom(nativeRequest);

    assertTrue(action.isPresent());
    assertEquals(Action.withoutSuffix("SearchConcertsController", "Controller"), action.get());
  }

  @Test
  void it_does_not_extract_action_from_controller_name_when_no_servlet_request_is_present() {
    NativeWebRequest nativeWebRequest = mock(NativeWebRequest.class);
    when(nativeWebRequest.getNativeRequest()).thenReturn(null);

    var action = controllerAction.actionFrom(nativeWebRequest);

    assertFalse(action.isPresent());
  }

  @Test
  void it_does_not_extract_action_from_controller_name_when_no_handler_is_found() {
    var builder = MockMvcRequestBuilders.get("/path-without-handler");
    var context = new MockServletContext();
    var nativeRequest = new ServletWebRequest(builder.buildRequest(context));

    var action = controllerAction.actionFrom(nativeRequest);

    assertFalse(action.isPresent());
  }

  @BeforeEach
  void let() throws NoSuchMethodException {
    path = "/concerts";
    var aMapping = new RequestMappingHandlerMapping();
    aMapping.registerMapping(
        RequestMappingInfo.paths(path).build(),
        new SearchConcertsController(),
        SearchConcertsController.class.getMethod("handle"));
    controllerAction = () -> aMapping;
  }

  private String path;
  private ControllerActionTrait controllerAction;

  private static class SearchConcertsController {
    public ResponseEntity<Object> handle() {
      return null;
    }
  }
}
