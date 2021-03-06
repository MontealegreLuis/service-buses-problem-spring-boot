package com.montealegreluis.servicebusesproblem;

import static com.montealegreluis.activityfeed.ExceptionContextFactory.contextFrom;
import static net.logstash.logback.marker.Markers.appendEntries;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.servicebuses.Action;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

final class ActionThrowableAdviceTest {
  @Test
  void it_includes_action_prefix_in_problem_code_property_and_in_detail() {
    var advice =
        new ActionThrowableAdvice() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.of(Action.named("SearchConcerts"));
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }
        };

    var response =
        advice.handleThrowable(new RuntimeException("Cannot connect to MySQL server"), null);

    var problem = response.getBody();
    assertNotNull(problem);
    assertEquals("Cannot search concerts. Cannot connect to MySQL server", problem.getDetail());
    assertEquals(1, problem.getAdditionalProperties().size());
    assertTrue(problem.getAdditionalProperties().containsKey("code"));
    assertEquals(
        "search-concerts-application-error", problem.getAdditionalProperties().get("code"));
  }

  @Test
  void it_does_not_includes_action_prefix_in_problem_code_property_and_in_detail() {
    var advice =
        new ActionThrowableAdvice() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.empty();
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }
        };

    var response =
        advice.handleThrowable(new RuntimeException("Cannot connect to MySQL server"), null);

    var problem = response.getBody();
    assertNotNull(problem);
    assertEquals("Cannot connect to MySQL server", problem.getDetail());
    assertEquals(1, problem.getAdditionalProperties().size());
    assertTrue(problem.getAdditionalProperties().containsKey("code"));
    assertEquals("application-error", problem.getAdditionalProperties().get("code"));
  }

  @Test
  void it_uses_problem_code_as_identifier_and_adds_action_slug_to_logging_event() {
    var exception = new RuntimeException("Cannot connect to MySQL server");
    var activity =
        Activity.error(
            "search-concerts-application-error",
            "Cannot search concerts. " + exception.getMessage(),
            (context) -> {
              context.put("exception", contextFrom(exception));
              context.put("action", "search-concerts");
            });
    var logger = mock(Logger.class);
    when(logger.isErrorEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var advice =
        new ActionThrowableAdvice() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.of(Action.named("SearchConcerts"));
          }

          @Override
          public ActivityFeed feed() {
            return aFeed;
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }
        };

    advice.handleThrowable(exception, null);

    verify(logger, times(1)).error(appendEntries(activity.context()), activity.message());
  }

  @Test
  void it_does_not_use_problem_code_as_identifier_and_does_not_add_action_slug_to_logging_event() {
    var exception = new RuntimeException("Cannot connect to MySQL server");
    var activity =
        Activity.error(
            "application-error",
            exception.getMessage(),
            (context) -> context.put("exception", contextFrom(exception)));
    var logger = mock(Logger.class);
    when(logger.isErrorEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var advice =
        new ActionThrowableAdvice() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.empty();
          }

          @Override
          public ActivityFeed feed() {
            return aFeed;
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }
        };

    advice.handleThrowable(exception, null);

    verify(logger, times(1)).error(appendEntries(activity.context()), activity.message());
  }
}
