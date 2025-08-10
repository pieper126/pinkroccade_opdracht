package com.example.tech_opdracht;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Person {
    private final Integer id;
    private Integer partnerId;
    private Set<Integer> childrenIds;
    private Set<Integer> parentIds;
    private LocalDate birthDate;
    private String name;

    public Person(Integer id) {
        this.id = id;
        this.childrenIds = new HashSet<>();
        this.parentIds = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public Optional<Integer> getPartnerId() {
        return Optional.ofNullable(partnerId);
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Set<Integer> getChildrenIds() {
        return new HashSet<>(childrenIds);
    }

    public void setChildrenIds(Set<Integer> childrenIds) {
        this.childrenIds = new HashSet<>(childrenIds);
    }

    public Set<Integer> getParentIds() {
        return new HashSet<>(parentIds);
    }

    public void setParentIds(Set<Integer> parentIds) {
        this.parentIds = new HashSet<>(parentIds);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Optional<Integer> getAge() {
        if (birthDate == null) {
            return Optional.empty();
        }

        return Optional.of(Period.between(birthDate, LocalDate.now()).getYears());
    }

    public void addChild(Integer childId) {
        this.childrenIds.add(childId);
    }

    public void removeChild(Integer childId) {
        this.childrenIds.remove(childId);
    }

    public void addParent(Integer parentId) {
        this.parentIds.add(parentId);
    }

    public void removeParent(Integer parentId) {
        this.parentIds.remove(parentId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
