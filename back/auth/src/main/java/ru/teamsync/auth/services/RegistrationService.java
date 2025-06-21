package ru.teamsync.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.teamsync.auth.client.ResumeClient;
import ru.teamsync.auth.client.ResumeClientException;
import ru.teamsync.auth.client.dto.PersonCreationRequest;
import ru.teamsync.auth.client.dto.professor.ProfessorCreationRequest;
import ru.teamsync.auth.client.dto.student.StudentCreationRequest;
import ru.teamsync.auth.config.security.userdetails.Role;
import ru.teamsync.auth.controllers.request.RegisterProfessorRequest;
import ru.teamsync.auth.controllers.request.RegisterStudentRequest;
import ru.teamsync.auth.dto.Person;
import ru.teamsync.auth.model.SecurityUser;
import ru.teamsync.auth.model.SecurityUserRepository;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ResumeClient resumeClient;
    private final SecurityUserRepository securityUserRepository;
    private final JwtService jwtService;
    private final ResumeClientMapper resumeClientMapper;

    @Transactional
    public String registerStudentAndGetJwt(Jwt entraJwt, RegisterStudentRequest registerStudentRequest) {
        var person = buildPerson(entraJwt);
        StudentCreationRequest studentCreationRequest = resumeClientMapper.toStudentCreationRequest(person, registerStudentRequest);

        var response = resumeClient.createStudent(studentCreationRequest);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Integer internalId = response.getBody().data().id();

            SecurityUser securityUser = new SecurityUser();
            securityUser.setEmail(person.getEmail());
            securityUser.setExternalUserId(entraJwt.getClaimAsString("oid"));
            securityUser.setInternalUserId(internalId);
            securityUser.setRole(Role.STUDENT);
            securityUserRepository.save(securityUser);

            return jwtService.generateToken(person.getEmail());
        } else {
            String errorMessage = response.getBody() == null ? "Failed to create professor, no response body" : response.getBody().error().toString();
            throw new ResumeClientException(errorMessage);
        }
    }

    @Transactional
    public String registerProfessorAndGetJwt(Jwt entraJwt, RegisterProfessorRequest registerProfessorRequest) {
        var person = buildPerson(entraJwt);
        ProfessorCreationRequest professorCreationRequest = resumeClientMapper.toProfessorCreationRequest(person, registerProfessorRequest);
        var response = resumeClient.createProfessor(professorCreationRequest);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Integer internalId = response.getBody().data().id();

            SecurityUser securityUser = new SecurityUser();
            securityUser.setEmail(person.getEmail());
            securityUser.setExternalUserId(entraJwt.getClaimAsString("oid"));
            securityUser.setInternalUserId(internalId);
            securityUser.setRole(Role.PROFESSOR);

            securityUserRepository.save(securityUser);

            return jwtService.generateToken(person.getEmail());
        } else {
            String errorMessage = response.getBody() == null ? "Failed to create professor, no response body" : response.getBody().error().toString();
            throw new ResumeClientException(errorMessage);
        }
    }

    private PersonCreationRequest buildPerson(Jwt entraJwt) {
        String[] nameSurname = entraJwt.getClaimAsString("name").split(" ");
        String name = nameSurname[0];
        String surname = nameSurname.length > 1 ? nameSurname[1] : "";
        String email = entraJwt.getClaimAsString("preferred_username");

        return new PersonCreationRequest(name, surname, email);
    }

}
