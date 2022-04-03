package com.montealegreluis.servicebusesproblem.validation;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblemspringboot.springboot.validation.MethodArgumentNotValidTrait;
import com.montealegreluis.servicebusesproblem.ControllerActionTrait;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.NativeWebRequest;

public interface ActionMethodArgumentNotValidTrait
    extends MethodArgumentNotValidTrait, ControllerActionTrait, WithActionInValidationErrors {
  @Override
  default void enhanceMethodArgumentNotValidProblem(
      final ApiProblemBuilder builder,
      final MethodArgumentNotValidException exception,
      final NativeWebRequest request) {

    actionFrom(request).ifPresent((action -> addActionToValidationProblem(builder, action)));
  }

  @Override
  default void enhanceMethodArgumentNotValidActivity(
      final ActivityBuilder builder, final ApiProblem problem, final NativeWebRequest request) {
    addValidationErrors(builder, problem);

    actionFrom(request)
        .ifPresent(action -> addActionToValidationActivity(builder, problem, action));
  }
}
