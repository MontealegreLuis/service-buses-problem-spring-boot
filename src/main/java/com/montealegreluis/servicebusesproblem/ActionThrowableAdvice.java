package com.montealegreluis.servicebusesproblem;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblemspringboot.springboot.ThrowableAdvice;
import org.springframework.web.context.request.NativeWebRequest;

public interface ActionThrowableAdvice extends ThrowableAdvice, ControllerActionTrait {
  @Override
  default void enhanceThrowableProblem(
      final ApiProblemBuilder builder, final Throwable exception, final NativeWebRequest request) {

    actionFrom(request)
        .ifPresent(
            (action) -> {
              builder.withDetail("Cannot " + action.toWords() + ". " + exception.getMessage());
              builder.with("code", action.toSlug() + "-application-error");
            });
  }

  @Override
  default void enhanceThrowableProblemActivity(
      final ActivityBuilder builder, final ApiProblem problem, final NativeWebRequest request) {

    actionFrom(request)
        .ifPresent(
            (action -> {
              builder.withMessage(problem.getDetail());
              builder.withIdentifier((String) problem.getAdditionalProperties().get("code"));
              builder.with("action", action.toSlug());
            }));
  }
}
