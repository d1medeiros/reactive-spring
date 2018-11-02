package com.example.demoreactiveone.service;

import com.example.demoreactiveone.Event;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class ServiceApp {

    @GetMapping("/events/{id}")
    Mono<Event> eventById(@PathVariable long id) {
        return Mono.just(new Event(id, new Date()));
    }

    /**
     * gerado em tempo real, passa um a um com o intervalo de 1 segundo
     * @return
     */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        Flux<Event> eventFlux = Flux.
                fromStream(Stream.generate(() -> new Event(System.currentTimeMillis(), new Date())));
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<Tuple2<Event, Long>> zip = Flux.zip(eventFlux, interval);
        return zip.map(Tuple2::getT1);
    }

    /**
     * com base em uma colecao, passa um a um com o intervalo de 1 segundo
     * @return
     */
    @GetMapping(value = "/events/know", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> knowEvents() {
        return Flux.zip(Flux.fromStream(this::getListOfEvent), Flux.interval(Duration.ofSeconds(3)))
                .map(Tuple2::getT1);
    }

    /**
     * retorna quantidade escolhida com um intervalo escolhido
     * @return
     */
    @GetMapping(value = "/events/control", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<List<Event>> controlEvents() {
        return Flux.zip(Flux.fromStream(this::getListOfEvent).buffer(10), Flux.interval(Duration.ofSeconds(3)))
                .map(Tuple2::getT1);
    }

    private Stream<Event> getListOfEvent() {
        return IntStream.range(0, 500)
                .mapToObj(i -> new Event(i, new Date()));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApp.class, args);
    }
}
