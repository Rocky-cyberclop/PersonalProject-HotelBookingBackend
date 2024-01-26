package com.rocky.gatewayservice.filters;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> whiteApi = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> whiteApi
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
