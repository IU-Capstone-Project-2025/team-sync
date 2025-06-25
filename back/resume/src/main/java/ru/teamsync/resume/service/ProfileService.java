package ru.teamsync.resume.service;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.mapper.PersonMapper;
import ru.teamsync.resume.mapper.ProfessorMapper;
import ru.teamsync.resume.mapper.StudentMapper;
import ru.teamsync.resume.repository.PersonRepository;
import ru.teamsync.resume.repository.ProfessorRepository;
import ru.teamsync.resume.repository.StudentRepository;

@Service
@AllArgsConstructor
public class ProfileService {
    private final PersonRepository personRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    private final PersonMapper personMapper;
    private final StudentMapper studentMapper;
    private final ProfessorMapper professorMapper;

    public ProfileResponse getProfile(Long personId) throws NotFoundException {
        var person = personRepository.findById(personId)
            .orElseThrow(() -> new NotFoundException());

        if (studentRepository.existsById(personId)) {
            var student = studentRepository.findByPersonId(personId)
                .orElseThrow(() -> new NotFoundException());

            return new ProfileResponse(
                "student",
                personMapper.toResponse(person),
                studentMapper.toResponse(student)
            );
        } else if (professorRepository.existsByPersonId(personId)) {
            var professorProfile = professorRepository.findByPersonId(personId)
                .orElseThrow(() -> new NotFoundException());

            return new ProfileResponse(
                "professor",
                personMapper.toResponse(person),
                professorMapper.toResponse(professorProfile)
            );

        } else {
            throw new NotFoundException();
        }
    }

    public void updateStudentProfile(Long personId, UpdateStudentProfileRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        if (!personId.equals(currentUserId)) {
            throw new AccessDeniedException("You can only modify your own profile");
        }
        var student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> new NotFoundException());

        studentMapper.updateStudent(request, student);
        studentRepository.save(student);
    }

    public void updateProfessorProfile(Long personId, UpdateProfessorProfileRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        if (!personId.equals(currentUserId)) {
            throw new AccessDeniedException("You can only modify your own profile");
        }
        var professor = professorRepository.findByPersonId(personId)
            .orElseThrow(() -> new NotFoundException());

        professorMapper.updateProfessor(request, professor);
        professorRepository.save(professor);
    }

}
