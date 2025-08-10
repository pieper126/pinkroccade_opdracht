package com.example.tech_opdracht;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/people")
public class PeopleController {

    final PeopleService service;

    public PeopleController(
            PeopleService  peopleService
    ) {
        service = peopleService;
    }

    @PostMapping
    public ResponseEntity<List<PersonDTO>> addOrUpdatePerson(@RequestBody PersonDTO dto) {
        final var person = dto.toPerson();

        service.addOrUpdatePerson(person);

        final var adheresToCriteria = service.getMatchingPeople();

        if (!adheresToCriteria.isEmpty()) {
            final var body = adheresToCriteria.stream().map(PersonDTO::from).toList();

            return ResponseEntity.status(200).body(body); // HTTP 444 No Response
        } else {
            return ResponseEntity.status(444).build(); // HTTP 444 No Response
        }
    }

}

record PersonDTO(
        Integer id,
        String name,
        LocalDate birthDate,
        PersonReferenceDTO parent1,
        PersonReferenceDTO parent2,
        @JsonProperty(required = false) PersonReferenceDTO partner,
        List<PersonReferenceDTO> children
) {
    public Person toPerson() {
        final var result =  new Person(id);
        result.setName(name);
        result.setBirthDate(birthDate);
        result.setParentIds(Set.of(parent1.id(), parent2.id()));

        final var childrenSet = children
                .stream()
                .map(PersonReferenceDTO::id)
                .collect(Collectors.toSet());

        result.setChildrenIds(childrenSet);

        if (partner != null) {
            result.setPartnerId(partner.id());
        }

        return result;
    }

    public static PersonDTO from(Person person) {
        final var parents = person.getParentIds().stream().toList();
        final var partner = person.getPartnerId().isEmpty() ? new PersonReferenceDTO(person.getPartnerId().get()) : null;

        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                new PersonReferenceDTO(parents.get(0)),
                new PersonReferenceDTO(parents.get(1)),
                partner,
                person.getChildrenIds().stream().map(PersonReferenceDTO::new).toList()
        );
    }
}

record PersonReferenceDTO(
        Integer id
) {}
