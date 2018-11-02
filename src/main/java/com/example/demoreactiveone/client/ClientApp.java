package com.example.demoreactiveone.client;

import com.example.demoreactiveone.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Collections;

@SpringBootApplication
public class ClientApp {

    @Bean
    public WebClient clientOne() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner demo(WebClient clientOne) {
        return args -> clientOne.get()
                        .uri("/events/know")
                        .accept(MediaType.APPLICATION_STREAM_JSON)
                        .exchange()
                        .flatMapMany(this::getStringFluxParser)
                        .subscribe(System.out::println);
    }

    private Flux<Event> getStringFluxParser(ClientResponse clientResponse) {
        return clientResponse
                .bodyToFlux(String.class)
                .map(s -> {
                    Event event = new Event();
                    try {
                        event = new ObjectMapper().readValue(s, Event.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return event;
        });
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApp.class)
                .properties(Collections.singletonMap("server.port", "8081"))
                .run(args);
    }

}
