package ru.ivanov.apigateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallBackController {

    @RequestMapping("/userFallback")
    public Mono<String> clientServiceFallback() {
        return Mono.just("User-service не отвечает!");
    }

}
