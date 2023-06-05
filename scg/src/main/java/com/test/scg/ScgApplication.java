package com.test.scg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class ScgApplication {

	public static void main(String[] args) {
		ROUTES_NUM = Integer.parseInt(args[0]);
		SpringApplication.run(ScgApplication.class, args);
	}

	private static int ROUTES_NUM = 100;
	private final static String HOST = "http://localhost:8000";

	@Bean
	public RouteLocator customRouteLocator() {

		List<Route> list = new LinkedList<>();

		for (int i = 0; i < ROUTES_NUM; i++) {
			Route routeFalse = Route.async().id(i + "").uri(HOST)
					.asyncPredicate(swe -> Mono.just(false)).build();
			list.add(routeFalse);
		}
		Route routeFalse = Route.async().id(ROUTES_NUM + "").uri(HOST)
				.asyncPredicate(s -> Mono.just(true)).build();
		list.add(routeFalse);
		return () -> Flux.fromStream(list.stream()).hide();
	}
}
