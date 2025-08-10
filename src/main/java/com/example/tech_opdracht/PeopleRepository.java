package com.example.tech_opdracht;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PeopleRepository {
    private final Map<Integer, Person> people = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> deletedPeople = new ConcurrentHashMap<>();

    public synchronized Optional<Person> get(Integer id) {
        if (deletedPeople.containsKey(id)) {
           return Optional.empty();
        }

        return Optional.ofNullable(people.get(id));
    }

    public synchronized void put(Person person) {
        deletedPeople.remove(person.getId());
        people.put(person.getId(), person);
    }

    public synchronized List<Person> people() {
        return people.values().stream().filter(x -> !deletedPeople.containsKey(x.getId())).toList();
    }

    public synchronized void delete(Integer id) {
        deletedPeople.put(id, id);
    }
}
