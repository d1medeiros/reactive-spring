package com.example.demoreactiveone.client;

import com.example.demoreactiveone.Event;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventRepository extends ReactiveCrudRepository<Event, String> {
}
