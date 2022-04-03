package com.montealegreluis.servicebusesproblem.validation;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblemspringboot.springboot.validation.ConstraintViolationTrait;
import com.montealegreluis.servicebusesproblem.ControllerActionTrait;
import javax.validation.ConstraintViolationException;
import org.springframework.web.context.request.NativeWebRequest;

public interface ActionValidationConstraintTrait
    extends ConstraintViolationTrait, ControllerActionTrait, WithActionInValidationErrors {
  @Override
  default void enhanceConstraintViolationProblem(
      final ApiProblemBuilder builder,
      final ConstraintViolationException exception,
      final NativeWebRequest request) {

    actionFrom(request).ifPresent(action -> addActionToValidationProblem(builder, action));
  }

  @Override
  default void enhanceConstraintViolationActivity(
      ActivityBuilder builder, ApiProblem problem, NativeWebRequest request) {
    addValidationErrors(builder, problem);

    actionFrom(request)
        .ifPresent(action -> addActionToValidationActivity(builder, problem, action));
  }
}
