package ru.teamsync.resume.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.teamsync.resume.entity.StudyGroup;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    
    Optional<StudyGroup> findByName(String name);
}