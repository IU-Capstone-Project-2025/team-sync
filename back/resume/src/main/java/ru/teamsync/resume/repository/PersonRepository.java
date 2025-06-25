package ru.teamsync.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.teamsync.resume.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
