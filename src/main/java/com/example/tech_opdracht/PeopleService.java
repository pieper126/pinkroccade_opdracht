package com.example.tech_opdracht;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class PeopleService {
    private final PeopleRepository repository;

    public PeopleService(
            PeopleRepository repository
    ) {
        this.repository = repository;
    }

    public Collection<Person> getMatchingPeople() {
        final var result = new HashSet<Person>();

        for (final var person : repository.people()) {
            if (satisfiesPattern(person)) {
                result.add(person);
            }
        }

        return result;
    }

    /**
     * Add or update a person in the graph
     * Maintains bidirectional integrity automatically
     */
    public synchronized void addOrUpdatePerson(Person person) {
        final Integer personId = person.getId();
        final var existingPerson = repository.get(personId);

        existingPerson.ifPresent(this::removeAllRelationships);

        repository.put(person);

        establishRelationships(person);
    }

    public synchronized void deletePerson(Person person) {
        final var existingPerson = repository.get(person.getId());

        existingPerson.ifPresent(this::removeAllRelationships);

        repository.delete(person.getId());
    }

    /**
     * Remove all relationships for a person (used during updates)
     */
    private void removeAllRelationships(Person person) {
        final var personId = person.getId();

        // Remove partner relationship
        if (person.getPartnerId().isPresent()) {
            final var partner = repository.get(person.getPartnerId().get());
            partner.ifPresent(value -> value.setPartnerId(null));
        }

        // Remove parents
        for (final var childId : person.getChildrenIds()) {
            final var child = repository.get(childId);
            child.ifPresent(value -> value.removeParent(personId));
        }

        // Remove children
        for (final var parentId : person.getParentIds()) {
            final var parent = repository.get(parentId);
            parent.ifPresent(value -> value.removeChild(personId));
        }
    }

    /**
     * Establish bidirectional relationships for a person
     */
    private void establishRelationships(Person person) {
        addPartnerRelationship(person);
        addParentRelationshipToChildren(person);
        addChildrenToParents(person);
    }

    private void addPartnerRelationship(Person person) {
        if (person.getPartnerId().isPresent()) {
            final var maybePartner = repository.get(person.getPartnerId().get());

            if (maybePartner.isEmpty()) {
                final var partner = new Person(person.getPartnerId().get());
                partner.setPartnerId(person.getId());
                repository.put(partner);
            } else {
                maybePartner.get().setPartnerId(person.getId());
            }
        }
    }

    private void addParentRelationshipToChildren(Person person) {
        for (final var childId : person.getChildrenIds()) {
            final var maybeChild = repository.get(childId);

            if (maybeChild.isEmpty()) {
                final var child = new Person(childId);
                child.addParent(person.getId());
                repository.put(child);
            } else {
                maybeChild.get().addParent(person.getId());
            }
        }
    }

    private void addChildrenToParents(Person person) {
        for (final var parentId : person.getParentIds()) {
            final var maybeParent = repository.get(parentId);

            if (maybeParent.isEmpty()) {
                final var parent = new Person(parentId);
                parent.addChild(person.getId());
                repository.put(parent);
            } else {
                maybeParent.get().addChild(person.getId());
            }
        }
    }

    /**
     * Check if a specific person satisfies the pattern
     */
    private boolean satisfiesPattern(Person person) {
        // 1. Must have a partner
        if (person.getPartnerId().isEmpty()) {
            return false;
        }

        final var partner = repository.get(person.getPartnerId().get());
        if (partner.isEmpty()) {
            return false;
        }

        // 2. Must have exactly 3 children
        final var childrenIds = person.getChildrenIds();
        if (childrenIds.size() != 3) {
            return false;
        }

        // 2. All 3 children must have the same partner listed as mother or father
        final var partnerId = person.getPartnerId().get();
        boolean hasMinorChild = false;

        for (final var childId : childrenIds) {
            final var maybeChild = repository.get(childId);
            if (maybeChild.isEmpty()) {
                return false;
            }

            final var child = maybeChild.get();

            // Check if the partner is listed as a parent of this child
            if (!child.getParentIds().contains(partnerId)) {
                return false;
            }

            // 3. Check if at least one child is under 18
            if (child.getAge().isPresent() && child.getAge().get() < 18) {
                hasMinorChild = true;
            }
        }

        return hasMinorChild;
    }
}
