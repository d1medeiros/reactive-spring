package com.example.demoreactiveone.client;

import com.example.demoreactiveone.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.IntStream;

@SpringBootApplication
public class ClientApp {

    @Bean
    public WebClient clientOne() {
        return WebClient.create("http://localhost:8080");
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApp.class)
                .properties(Collections.singletonMap("server.port", "8081"))
                .run(args);
    }

}

@Component
class SampleCLR implements CommandLineRunner {

    private WebClient clientOne;
    private EventRepository eventRepository;

    @Autowired
    public SampleCLR(WebClient clientOne, EventRepository eventRepository) {
        this.clientOne = clientOne;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
      wrongStreamSave();
    }

    private void wrongStreamSave() {
        clientOne.get()
                .uri("/events/know")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .flatMapMany(this::getStringFluxParser)
                .subscribe(event -> eventRepository.save(event).subscribe());
    }

    private Flux<Event> getStringFluxParser(ClientResponse clientResponse) {
        return clientResponse
                .bodyToFlux(String.class)
                .map(s -> {
                    Event event = new Event();
                    try {
                        event = new ObjectMapper().readValue(s, Event.class);
                        event.setInfo("client");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ClientApp: "+event);
                    return event;
                });
    }

    private void simpleSave() {
        IntStream.range(0, 5)
                .mapToObj(i -> new Event(i, new Date()))
                .forEach(event -> eventRepository.save(event).subscribe());
    }

    @Bean
    CommandLineRunner demo(WebClient clientOne, EventRepository eventRepository) {
        return args -> { };
//        return args -> clientOne.get()
//                .uri("/events/know")
//                .accept(MediaType.APPLICATION_STREAM_JSON)
//                .exchange()
//                .flatMapMany(this::getStringFluxParser)
//                .subscribe(System.out::println);
    }
}