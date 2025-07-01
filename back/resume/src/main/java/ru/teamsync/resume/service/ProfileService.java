package ru.teamsync.resume.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.entity.Professor;
import ru.teamsync.resume.entity.Student;
import ru.teamsync.resume.mapper.PersonMapper;
import ru.teamsync.resume.mapper.ProfessorMapper;
import ru.teamsync.resume.mapper.StudentMapper;
import ru.teamsync.resume.repository.PersonRepository;
import ru.teamsync.resume.repository.ProfessorRepository;
import ru.teamsync.resume.repository.RoleRepository;
import ru.teamsync.resume.repository.SkillRepository;
import ru.teamsync.resume.repository.StudentRepository;
import ru.teamsync.resume.service.exception.PersonNotFoundException;
import ru.teamsync.resume.service.exception.ProfileUpdateAccessDeniedException;

@Service
@AllArgsConstructor
public class ProfileService {
    private static final String STUDENT_ROLE = "student";
    private static final String PROFESSOR_ROLE = "professor";

    private final PersonRepository personRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final RoleRepository roleRepository;

    private final PersonMapper personMapper;
    private final StudentMapper studentMapper;
    private final ProfessorMapper professorMapper;

    public ProfileResponse getProfile(Long personId) {
        var person = personRepository.findById(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        Optional<Student> student = studentRepository.findByPersonId(personId);
        if (student.isPresent()) {
            return new ProfileResponse(
                STUDENT_ROLE,
                personMapper.toResponse(person),
                studentMapper.toResponse(student.get())
            );
        }

        Optional<Professor> professor = professorRepository.findByPersonId(personId);
        if (professor.isPresent()) {
            return new ProfileResponse(
                PROFESSOR_ROLE,
                personMapper.toResponse(person),
                professorMapper.toResponse(professor.get())
            );
        }

        throw PersonNotFoundException.withId(personId);
    }

    public void updateStudentProfile(Long personId, UpdateStudentProfileRequest request, Long currentUserId) {
        if (!personId.equals(currentUserId)) {
            throw ProfileUpdateAccessDeniedException.withIds(currentUserId, personId);
        }
        var student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        studentMapper.updateStudent(request, student);
        studentRepository.save(student);
    }

    public void updateProfessorProfile(Long personId, UpdateProfessorProfileRequest request, Long currentUserId) {
        if (!personId.equals(currentUserId)) {
            throw ProfileUpdateAccessDeniedException.withIds(currentUserId, personId);
        }
        var professor = professorRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        professorMapper.updateProfessor(request, professor);
        professorRepository.save(professor);
    }

    public Page<SkillResponse> getStudentSkills(Long personId, Pageable pageable) {
        Student student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        var skillIds = student.getSkills();
        if (skillIds.isEmpty()) {
            return Page.empty(pageable);
        }

        var skillsPage = skillRepository.findByIdIn(skillIds, pageable);
        return skillsPage.map(skill -> new SkillResponse(skill.getId(), skill.getName(), skill.getDescription()));
    }

    public Page<RoleResponse> getStudentRoles(Long personId, Pageable pageable) {
        Student student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        var roleIds = student.getRoles();
        if (roleIds.isEmpty()) {
            return Page.empty(pageable);
        }

        var rolesPage = roleRepository.findByIdIn(roleIds, pageable);
        return rolesPage.map(role -> new RoleResponse(role.getId(), role.getName(), role.getDescription()));
    }

}
