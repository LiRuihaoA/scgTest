package com.test.newSCG;


import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_ROUTE_ATTR;

/**
 * overwrite lookup without concatMap operator.
 */
public class CustomRoutePredicateHandlerMapping extends RoutePredicateHandlerMapping {

    private final RouteLocator routeLocator;

    public CustomRoutePredicateHandlerMapping(
            FilteringWebHandler webHandler,
            RouteLocator routeLocator,
            GlobalCorsProperties globalCorsProperties,
            Environment environment) {
        super(webHandler, routeLocator, globalCorsProperties, environment);
        this.routeLocator = routeLocator;
    }

    @Override
    protected Mono<Route> lookupRoute(ServerWebExchange exchange) {
        return this.routeLocator.getRoutes()
                .filterWhen(route -> {
                    // add the current route we are testing
                    exchange.getAttributes().put(GATEWAY_PREDICATE_ROUTE_ATTR, route.getId());
                    try {
                        return route.getPredicate().apply(exchange);
                    } catch (Exception e) {
                        logger.error("Error applying predicate for route: " + route.getId(), e);
                        return Mono.just(false);
                    }
                }).next()
                .map(route -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Route matched: " + route.getId());
                    }
                    validateRoute(route, exchange);
                    return route;
                });
    }
}