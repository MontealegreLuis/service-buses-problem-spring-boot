package com.montealegreluis.servicebusesproblem.validation;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.servicebuses.Action;

public interface WithActionInValidationErrors {
  default void addActionToValidationProblem(final ApiProblemBuilder builder, final Action action) {
    builder.withDetail("Cannot " + action.toWords() + ". Invalid input provided");
    builder.with("code", action.toSlug() + "-invalid-input");
  }

  default void addActionToValidationActivity(
      final ActivityBuilder builder, final ApiProblem problem, final Action action) {
    builder.withMessage(problem.getDetail());
    builder.withIdentifier((String) problem.getAdditionalProperties().get("code"));
    builder.with("action", action.toSlug());
  }
}
