package ru.teamsync.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.teamsync.auth.client.ResumeClient;
import ru.teamsync.auth.client.ResumeClientException;
import ru.teamsync.auth.client.dto.PersonCreationRequest;
import ru.teamsync.auth.client.dto.student.StudentCreationRequest;
import ru.teamsync.auth.client.dto.student.StudentCreationResponse;
import ru.teamsync.auth.config.security.userdetails.Role;
import ru.teamsync.auth.controllers.RegisterStudentRequest;
import ru.teamsync.auth.model.SecurityUser;
import ru.teamsync.auth.model.SecurityUserRepository;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ResumeClient resumeClient;
    private final SecurityUserRepository securityUserRepository;
    private final JwtService jwtService;

    @Transactional
    public String registerStudentAndGetJwt(Jwt entraJwt, RegisterStudentRequest registerStudentRequest) {
        String[] nameSurname = entraJwt.getClaimAsString("name").split(" ");
        String name = nameSurname[0];
        String surname = nameSurname.length > 1 ? nameSurname[1] : "";
        String email = entraJwt.getClaimAsString("preferred_username");

        PersonCreationRequest personCreationRequest = new PersonCreationRequest(name, surname, email);
        StudentCreationRequest studentCreationRequest = StudentCreationRequest.builder()
                .person(personCreationRequest)
                .studyGroup(registerStudentRequest.studyGroup())
                .description(registerStudentRequest.description())
                .githubAlias(registerStudentRequest.githubAlias())
                .tgAlias(registerStudentRequest.tgAlias())
                .build();
        var response = resumeClient.createStudent(studentCreationRequest);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Integer internalId = response.getBody().data().id();

            SecurityUser securityUser = new SecurityUser();
            securityUser.setEmail(email);
            securityUser.setExternalUserId(entraJwt.getClaimAsString("oid"));
            securityUser.setInternalUserId(internalId);
            securityUser.setRole(Role.STUDENT);
            securityUserRepository.save(securityUser);

            return jwtService.generateToken(email);
        } else {
            if (response.getBody() != null) {
                throw new ResumeClientException(response.getBody().error());
            } else {
                throw new ResumeClientException("Failed to create student, no response body");
            }
        }
    }

}
