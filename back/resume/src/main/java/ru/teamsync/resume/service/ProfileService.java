package ru.teamsync.resume.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ru.teamsync.resume.dto.StudyGroupNotFoundException;
import ru.teamsync.resume.dto.request.ProfessorCreationRequest;
import ru.teamsync.resume.dto.request.StudentCreationRequest;
import ru.teamsync.resume.dto.request.UpdateProfessorProfileRequest;
import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.ProfessorCreationResponse;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.dto.response.RoleResponse;
import ru.teamsync.resume.dto.response.SkillResponse;
import ru.teamsync.resume.dto.response.StudentCreationResponse;
import ru.teamsync.resume.entity.Person;
import ru.teamsync.resume.entity.Professor;
import ru.teamsync.resume.entity.Student;
import ru.teamsync.resume.entity.StudyGroup;
import ru.teamsync.resume.mapper.PersonMapper;
import ru.teamsync.resume.mapper.ProfessorMapper;
import ru.teamsync.resume.mapper.StudentMapper;
import ru.teamsync.resume.repository.PersonRepository;
import ru.teamsync.resume.repository.ProfessorRepository;
import ru.teamsync.resume.repository.RoleRepository;
import ru.teamsync.resume.repository.SkillRepository;
import ru.teamsync.resume.repository.StudentRepository;
import ru.teamsync.resume.repository.StudyGroupRepository;
import ru.teamsync.resume.service.exception.NotFoundException;
import ru.teamsync.resume.service.exception.PersonNotFoundException;
import ru.teamsync.resume.service.exception.ProfileUpdateAccessDeniedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    private final StudyGroupRepository studyGroupRepository;

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

        throw new NotFoundException("No profile (student/professor) found for person id " + personId);
    }

    public void updateStudentProfile(Long personId, UpdateStudentProfileRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        if (!personId.equals(currentUserId)) {
            throw ProfileUpdateAccessDeniedException.withIds(currentUserId, personId);
        }
        var student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        studentMapper.updateStudent(request, student);
        studentRepository.save(student);
    }

    public void updateProfessorProfile(Long personId, UpdateProfessorProfileRequest request, Long currentUserId) throws NotFoundException, AccessDeniedException {
        if (!personId.equals(currentUserId)) {
            throw new AccessDeniedException("You can only modify your own profile");
        }
        var professor = professorRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        professorMapper.updateProfessor(request, professor);
        professorRepository.save(professor);
    }

    public Page<SkillResponse> getStudentSkills(Long personId, Pageable pageable) throws NotFoundException {
        Student student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        var skillIds = student.getSkills();
        if (skillIds.isEmpty()) {
            return Page.empty(pageable);
        }

        var skillsPage = skillRepository.findByIdIn(skillIds, pageable);
        return skillsPage.map(skill -> new SkillResponse(skill.getId(), skill.getName(), skill.getDescription()));
    }

    public Page<RoleResponse> getStudentRoles(Long personId, Pageable pageable) throws NotFoundException {
        Student student = studentRepository.findByPersonId(personId)
            .orElseThrow(() -> PersonNotFoundException.withId(personId));

        var roleIds = student.getRoles();
        if (roleIds.isEmpty()) {
            return Page.empty(pageable);
        }

        var rolesPage = roleRepository.findByIdIn(roleIds, pageable);
        return rolesPage.map(role -> new RoleResponse(role.getId(), role.getName(), role.getDescription()));
    }

    @Transactional
    public StudentCreationResponse createStudentProfile(StudentCreationRequest request) {
        Person person = Person.builder()
                .name(request.getPerson().getName())
                .surname(request.getPerson().getSurname())
                .email(request.getPerson().getEmail())
                .build();

        personRepository.save(person);
        log.info("Incoming studyGroup: {}", request.getStudyGroup());

        StudyGroup studyGroup = studyGroupRepository.findByName(request.getStudyGroup())
                .orElseThrow(() -> new StudyGroupNotFoundException(request.getStudyGroup()));

        Student student = Student.builder()
                .person(person)
                .studyGroup(studyGroup)
                .description(request.getDescription())
                .githubAlias(request.getGithubAlias())
                .tgAlias(request.getTgAlias())
                .build();

        studentRepository.save(student);

        return new StudentCreationResponse(student.getId());
    }

    @Transactional
    public ProfessorCreationResponse createProfessorProfile(ProfessorCreationRequest request) {
        Person person = Person.builder()
                .name(request.getPerson().getName())
                .surname(request.getPerson().getSurname())
                .email(request.getPerson().getEmail())
                .build();

        personRepository.save(person);

        Professor professor = Professor.builder()
            .person(person)
            .tgAlias(request.getTgAlias())
            .build();

        professorRepository.save(professor);

        return new ProfessorCreationResponse(professor.getId());
    }

}
