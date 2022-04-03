package com.montealegreluis.servicebusesproblem.validation;

import static net.logstash.logback.marker.Markers.appendEntries;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.apiproblemspringboot.validation.RequiredValueConstraintViolation;
import com.montealegreluis.servicebuses.Action;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

final class ActionValidationConstraintTraitTest {
  @Test
  void it_includes_action_prefix_in_problem_code_and_in_detail() {
    var trait =
        new ActionValidationConstraintTrait() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.of(Action.named("SearchConcerts"));
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }
        };

    var response = trait.handleConstraintViolation(constraintViolationException, null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals("Cannot search concerts. Invalid input provided", apiProblem.getDetail());
    assertEquals("search-concerts-invalid-input", apiProblem.getAdditionalProperties().get("code"));
  }

  @Test
  void it_does_not_include_action_prefix_in_problem_code_and_in_detail() {
    var trait =
        new ActionValidationConstraintTrait() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.empty();
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }
        };

    var response = trait.handleConstraintViolation(constraintViolationException, null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals("Invalid input provided", apiProblem.getDetail());
    assertEquals("invalid-input", apiProblem.getAdditionalProperties().get("code"));
  }

  @Test
  void it_adds_action_to_context_and_prefixes_identifier_in_logging_activity() {
    var activity =
        Activity.error(
            "search-concerts-invalid-input",
            "Cannot search concerts. Invalid input provided",
            (context) -> {
              context.put("errors", errors);
              context.put("action", "search-concerts");
            });
    var logger = mock(Logger.class);
    when(logger.isWarnEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var trait =
        new ActionValidationConstraintTrait() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.of(Action.named("SearchConcerts"));
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }

          @Override
          public ActivityFeed feed() {
            return aFeed;
          }
        };

    trait.handleConstraintViolation(constraintViolationException, null);

    verify(logger, times(1)).warn(appendEntries(activity.context()), activity.message());
  }

  @Test
  void it_does_not_adds_action_to_context_and_does_not_prefix_identifier_in_logging_activity() {
    var activity =
        Activity.error(
            "invalid-input", "Invalid input provided", (context) -> context.put("errors", errors));
    var logger = mock(Logger.class);
    when(logger.isWarnEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var trait =
        new ActionValidationConstraintTrait() {
          @Override
          public Optional<Action> actionFrom(NativeWebRequest request) {
            return Optional.empty();
          }

          @Override
          public HandlerMapping handlerMapping() {
            return null;
          }

          @Override
          public ActivityFeed feed() {
            return aFeed;
          }
        };

    trait.handleConstraintViolation(constraintViolationException, null);

    verify(logger, times(1)).warn(appendEntries(activity.context()), activity.message());
  }

  @BeforeEach
  void let() {
    var violations = new HashSet<ConstraintViolation<String>>();
    violations.add(new RequiredValueConstraintViolation("firstName"));
    violations.add(new RequiredValueConstraintViolation("lastName"));
    violations.add(new RequiredValueConstraintViolation("email"));
    constraintViolationException = new ConstraintViolationException(violations);
    errors = new HashMap<>();
    errors.put("lastName", "value is required");
    errors.put("firstName", "value is required");
    errors.put("email", "value is required");
  }

  private ConstraintViolationException constraintViolationException;
  private HashMap<String, String> errors;
}
