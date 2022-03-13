package com.montealegreluis.servicebusesproblem.validation;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblemspringboot.springboot.validation.ConstraintViolationTrait;
import com.montealegreluis.servicebuses.Action;
import com.montealegreluis.servicebusesproblem.ControllerActionTrait;
import javax.validation.ConstraintViolationException;
import org.springframework.web.context.request.NativeWebRequest;

public interface ActionValidationConstraintTrait
    extends ConstraintViolationTrait, ControllerActionTrait {
  @Override
  default void enhanceConstraintViolationProblem(
      final ApiProblemBuilder builder,
      final ConstraintViolationException exception,
      final NativeWebRequest request) {
    final Action action = actionFrom(request);

    if (action == null) return;

    builder.withDetail("Cannot " + action.toWords() + ". Invalid input provided");
    builder.with("code", action.toSlug() + "-invalid-input");
  }

  @Override
  default void enhanceConstraintViolationActivity(
      ActivityBuilder builder, ApiProblem problem, NativeWebRequest request) {
    builder.with("errors", problem.getAdditionalProperties().get("errors"));

    final Action action = actionFrom(request);

    if (action == null) return;

    builder.withMessage(problem.getDetail());
    builder.withIdentifier((String) problem.getAdditionalProperties().get("code"));
    builder.with("action", action.toSlug());
  }
}
