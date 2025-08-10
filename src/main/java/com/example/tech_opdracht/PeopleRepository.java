package com.example.tech_opdracht;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PeopleRepository {
    private final Map<Integer, Person> people = new ConcurrentHashMap<>();

    public synchronized Optional<Person> get(Integer id) {
        return Optional.ofNullable(people.get(id));
    }

    public synchronized void put(Person person) {
        people.put(person.getId(), person);
    }

    public synchronized List<Person> people() {
        return people.values().stream().toList();
    }
}
