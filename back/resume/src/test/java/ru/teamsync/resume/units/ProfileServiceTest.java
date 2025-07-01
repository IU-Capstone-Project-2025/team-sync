package ru.teamsync.resume.units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.teamsync.resume.dto.response.ProfileResponse;
import ru.teamsync.resume.entity.Person;
import ru.teamsync.resume.entity.Student;
import ru.teamsync.resume.mapper.PersonMapper;
import ru.teamsync.resume.mapper.StudentMapper;
import ru.teamsync.resume.repository.PersonRepository;
import ru.teamsync.resume.repository.StudentRepository;
import ru.teamsync.resume.service.ProfileService;
import ru.teamsync.resume.service.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class ProfileServiceTest {

    private ProfileService profileService;

    private PersonRepository personRepository;
    private StudentRepository studentRepository;
    private PersonMapper personMapper;
    private StudentMapper studentMapper;

    @BeforeEach
    void setup() {
        personRepository = Mockito.mock(PersonRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        personMapper = Mockito.mock(PersonMapper.class);
        studentMapper = Mockito.mock(StudentMapper.class);

        profileService = new ProfileService(personRepository, null, studentRepository, null, null, personMapper, studentMapper, null);
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
        when(studentMapper.toResponse(student)).thenReturn(null);

        ProfileResponse result = profileService.getProfile(1L);

        assert result != null;
    }

}
