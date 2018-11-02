package com.example.demoreactiveone.client;

import com.example.demoreactiveone.Event;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@SpringBootApplication
public class ClientApp {

    @Bean
    public WebClient clientOne() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner demo(WebClient clientOne) {
        return args ->
                clientOne.get()
                        .uri("/events/control")
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .exchange()
                        .flatMapMany(clientResponse -> clientResponse.bodyToFlux(String.class))
                        .subscribe(System.out::println);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApp.class)
                .properties(Collections.singletonMap("server.port", "8081"))
                .run(args);
    }

}
