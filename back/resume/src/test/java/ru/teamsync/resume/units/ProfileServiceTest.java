package ru.teamsync.resume.units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.teamsync.resume.client.EmbedderClient;
import ru.teamsync.resume.dto.request.UpdateStudentProfileRequest;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.entity.Person;
import ru.teamsync.resume.entity.Professor;
import ru.teamsync.resume.entity.Student;
import ru.teamsync.resume.mapper.PersonMapper;
import ru.teamsync.resume.mapper.ProfileMapper;
import ru.teamsync.resume.repository.PersonRepository;
import ru.teamsync.resume.repository.ProfessorRepository;
import ru.teamsync.resume.repository.RoleRepository;
import ru.teamsync.resume.repository.SkillRepository;
import ru.teamsync.resume.repository.StudentRepository;
import ru.teamsync.resume.repository.StudyGroupRepository;
import ru.teamsync.resume.service.ProfileService;
import ru.teamsync.resume.service.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class ProfileServiceTest {

    private ProfileService profileService;

    private PersonRepository personRepository;
    private ProfessorRepository professorRepository;
    private StudentRepository studentRepository;
    private SkillRepository skillRepository;
    private RoleRepository roleRepository;
    private StudyGroupRepository studyGroupRepository;
    private EmbedderClient embedderClient;

    private PersonMapper personMapper;
    private ProfileMapper profileMapper;

    @BeforeEach
    void setup() {
        personRepository = Mockito.mock(PersonRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        professorRepository = Mockito.mock(ProfessorRepository.class);
        skillRepository = Mockito.mock(SkillRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        studyGroupRepository = Mockito.mock(StudyGroupRepository.class);
        embedderClient = Mockito.mock(EmbedderClient.class);

        personMapper = Mockito.mock(PersonMapper.class);
        profileMapper = Mockito.mock(ProfileMapper.class);


        profileService = new ProfileService(
                personRepository,
                professorRepository,
                studentRepository,
                skillRepository,
                roleRepository,
                studyGroupRepository,
                embedderClient,
                personMapper,
                profileMapper
        );
    }

    @Test
    void should_throwNotFoundException_when_personDoesNotExist() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> profileService.getProfile(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void should_returnProfileResponse_when_studentExist() throws Exception {
        var person = new Person();
        person.setId(1L);

        var student = new Student();
        student.setId(2L);
        student.setPerson(person);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(studentRepository.findByPersonId(1L)).thenReturn(Optional.of(student));

        when(personMapper.toResponse(person)).thenReturn(null);
        when(profileMapper.toResponse(student)).thenReturn(null);

        ProfileResponse result = profileService.getProfile(1L);

        assert result != null;
    }

    @Test
    void should_returnProfileResponse_when_professorExist() throws NotFoundException {
        var person = new Person();
        person.setId(1L);

        var professor = new Professor();
        professor.setId(2L);
        professor.setPerson(person);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(professorRepository.findByPersonId(1L)).thenReturn(Optional.of(professor));

        when(personMapper.toResponse(person)).thenReturn(null);
        when(profileMapper.toResponse(professor)).thenReturn(null);

        ProfileResponse result = profileService.getProfile(1L);

        assert result != null;
    }

    @Test
    void should_throwNotFoundException_when_personWithoutRole() {
        var person = new Person();
        person.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(studentRepository.findByPersonId(1L)).thenReturn(Optional.empty());
        when(professorRepository.findByPersonId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getProfile(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void should_throwNotFoundException_when_updatingStudentProfile_andPersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        var request = new UpdateStudentProfileRequest(
                null, null, null, null, List.of(), List.of()
        );

        assertThatThrownBy(() -> profileService.updateStudentProfile(1L, request, 1L))
                .isInstanceOf(NotFoundException.class);
    }

}
