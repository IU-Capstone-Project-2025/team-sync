package ru.teamsync.auth.model;

import jakarta.persistence.*;
import lombok.*;
import ru.teamsync.auth.config.security.userdetails.Role;

@Entity
@Table(name = "user", schema = "security")
@Getter
@Setter
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SecurityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "internal_user_id", unique = true)
    Integer internalUserId;

    @Column(name = "profile_id", unique = true)
    Integer profileId;

    @Column(name = "external_user_id", unique = true)
    String externalUserId;

    @Column(name = "email")
    String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Role role;

}
