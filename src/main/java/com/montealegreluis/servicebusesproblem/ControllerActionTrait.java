package com.montealegreluis.servicebusesproblem;

import com.montealegreluis.servicebuses.Action;
import io.vavr.control.Try;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;

public interface ControllerActionTrait extends WithHandlerMapping {
  default Optional<Action> actionFrom(NativeWebRequest nativeRequest) {
    final HttpServletRequest request = nativeRequest.getNativeRequest(HttpServletRequest.class);
    if (request == null) return Optional.empty();

    final HandlerExecutionChain chain =
        Try.of(() -> handlerMapping().getHandler(request)).getOrNull();

    if (chain == null) return Optional.empty();

    final HandlerMethod controller = (HandlerMethod) chain.getHandler();
    return Optional.of(
        Action.withoutSuffix(controller.getBeanType().getSimpleName(), "Controller"));
  }
}
