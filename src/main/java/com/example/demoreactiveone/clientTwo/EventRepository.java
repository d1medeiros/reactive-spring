package com.example.demoreactiveone.clientTwo;

import com.example.demoreactiveone.Event;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventRepository extends ReactiveCrudRepository<Event, String> {
}
