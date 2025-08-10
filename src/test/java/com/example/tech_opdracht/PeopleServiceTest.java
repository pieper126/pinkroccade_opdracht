package com.example.tech_opdracht;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static java.time.LocalDate.*;
import static org.junit.jupiter.api.Assertions.*;

class PeopleServiceTest {

    final Integer parentId1 = 1;
    final Integer parentId2 = 2;
    final Integer parentId3 = 100;
    final Integer childId1 = 3;
    final Integer childId2 = 4;
    final Integer childId3 = 5;
    final Integer childId4 = 1000;

   @Test
   void adheresToCriteria() {
       final var service = new PeopleService(new PeopleRepository());

       Person parent1 = new Person(parentId1);
       parent1.setPartnerId(parentId2);
       parent1.setChildrenIds(Set.of(childId1, childId2, childId3));

       Person parent2 = new Person(parentId2);
       parent2.setPartnerId(parentId1);
       parent2.setBirthDate(of(1980, 1, 1));
       parent2.setChildrenIds(Set.of(childId1, childId2, childId3));

       Person child1 = new Person(childId1);
       child1.setBirthDate(of(2010, 1, 1));
       child1.setParentIds(Set.of(parentId1, parentId2));

       Person child2 = new Person(childId2);
       child2.setBirthDate(of(2005, 1, 1));
       child2.setParentIds(Set.of(parentId1, parentId2));

       Person youngest = new Person(childId3);
       youngest.setBirthDate(LocalDate.now().minus(1, ChronoUnit.YEARS));
       youngest.setParentIds(Set.of(parentId1, parentId2));

       service.addOrUpdatePerson(parent1);
       service.addOrUpdatePerson(parent2);
       service.addOrUpdatePerson(child1);
       service.addOrUpdatePerson(child2);
       service.addOrUpdatePerson(youngest);
       final var result = service.getMatchingPeople();

       assertEquals(2, result.size(), "should adhere to all criteria!");
   }

    @Test
    void hasADifferentParent() {
        final var service = new PeopleService(new PeopleRepository());

        // Create a family that satisfies the pattern
        Person parent1 = new Person(parentId1);
        parent1.setPartnerId(parentId2);
        parent1.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person parent2 = new Person(parentId2);
        parent2.setPartnerId(parentId1);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        Person youngest = new Person(childId3);
        youngest.setBirthDate(LocalDate.now().minus(1, ChronoUnit.YEARS));
        youngest.setParentIds(Set.of(parentId1, parentId3));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        service.addOrUpdatePerson(youngest);
        final var result = service.getMatchingPeople();

        assertEquals(0, result.size(), "has a different parent!");
    }

    @Test
    void noChildUnderEighteen() {
        final var service = new PeopleService(new PeopleRepository());

        // Create a family that satisfies the pattern
        Person parent1 = new Person(parentId1);
        parent1.setPartnerId(parentId2);
        parent1.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person parent2 = new Person(parentId2);
        parent2.setPartnerId(parentId1);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        Person youngest = new Person(childId3);
        youngest.setBirthDate(LocalDate.now().minus(30, ChronoUnit.YEARS));
        youngest.setParentIds(Set.of(parentId1, parentId3));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        service.addOrUpdatePerson(youngest);
        final var result = service.getMatchingPeople();

        assertEquals(0, result.size(), "children are too old");
    }

    @Test
    void hasLessThanThreeChildren() {
        final var service = new PeopleService(new PeopleRepository());

        // Create a family that satisfies the pattern
        Person parent1 = new Person(parentId1);
        parent1.setPartnerId(parentId2);
        parent1.setChildrenIds(Set.of(childId1, childId2));

        Person parent2 = new Person(parentId2);
        parent2.setPartnerId(parentId1);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        final var result = service.getMatchingPeople();

        assertEquals(0, result.size(), "less than 3 children");
    }

    @Test
    void hasMoreThanThreeChildren() {
        final var service = new PeopleService(new PeopleRepository());

        // Create a family that satisfies the pattern
        Person parent1 = new Person(parentId1);
        parent1.setPartnerId(parentId2);
        parent1.setChildrenIds(Set.of(childId1, childId2, childId3, childId4));

        Person parent2 = new Person(parentId2);
        parent2.setPartnerId(parentId1);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2, childId3, childId4));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        Person child3 = new Person(childId3);
        child3.setBirthDate(of(2005, 1, 1));
        child3.setParentIds(Set.of(parentId1, parentId2));

        Person child4 = new Person(childId4);
        child4.setBirthDate(of(2005, 1, 1));
        child4.setParentIds(Set.of(parentId1, parentId2));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        service.addOrUpdatePerson(child3);
        service.addOrUpdatePerson(child4);
        final var result = service.getMatchingPeople();

        assertEquals(0, result.size(), "more than 3 children");
    }

    @Test
    void hasNoPartner() {
        final var service = new PeopleService(new PeopleRepository());

        // Create a family that satisfies the pattern
        Person parent1 = new Person(parentId1);
        parent1.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person parent2 = new Person(parentId2);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        Person youngest = new Person(childId3);
        youngest.setBirthDate(LocalDate.now().minus(1, ChronoUnit.YEARS));
        youngest.setParentIds(Set.of(parentId1, parentId2));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        service.addOrUpdatePerson(youngest);
        final var result = service.getMatchingPeople();

        assertEquals(0, result.size(), "has no partner");
    }

    @Test
    void deletingCriticalNodeLeadsToCriteriaFailing() {
        final var service = new PeopleService(new PeopleRepository());

        Person parent1 = new Person(parentId1);
        parent1.setPartnerId(parentId2);
        parent1.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person parent2 = new Person(parentId2);
        parent2.setPartnerId(parentId1);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        Person youngest = new Person(childId3);
        youngest.setBirthDate(LocalDate.now().minus(1, ChronoUnit.YEARS));
        youngest.setParentIds(Set.of(parentId1, parentId2));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        service.addOrUpdatePerson(youngest);

        service.deletePerson(youngest);
        final var result = service.getMatchingPeople();

        assertEquals(0, result.size(), "should no longer adhere to criteria!");
    }

    @Test
    void deletingAndAddingAgainRemovesTheDelete() {
        final var service = new PeopleService(new PeopleRepository());

        Person parent1 = new Person(parentId1);
        parent1.setPartnerId(parentId2);
        parent1.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person parent2 = new Person(parentId2);
        parent2.setPartnerId(parentId1);
        parent2.setBirthDate(of(1980, 1, 1));
        parent2.setChildrenIds(Set.of(childId1, childId2, childId3));

        Person child1 = new Person(childId1);
        child1.setBirthDate(of(2010, 1, 1));
        child1.setParentIds(Set.of(parentId1, parentId2));

        Person child2 = new Person(childId2);
        child2.setBirthDate(of(2005, 1, 1));
        child2.setParentIds(Set.of(parentId1, parentId2));

        Person youngest = new Person(childId3);
        youngest.setBirthDate(LocalDate.now().minus(1, ChronoUnit.YEARS));
        youngest.setParentIds(Set.of(parentId1, parentId2));

        service.addOrUpdatePerson(parent1);
        service.addOrUpdatePerson(parent2);
        service.addOrUpdatePerson(child1);
        service.addOrUpdatePerson(child2);
        service.addOrUpdatePerson(youngest);

        service.deletePerson(youngest);
        service.addOrUpdatePerson(youngest);
        final var result = service.getMatchingPeople();

        assertEquals(2, result.size(), "should once again be known");
    }
}