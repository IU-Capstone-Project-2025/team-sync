package ru.teamsync.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class InternalExternalUserIdKey {

    @Column(name = "internal_user_id", nullable = false)
    private Integer internalUserId;

    @Column(name = "external_user_id", nullable = false)
    private String externalUserId;



}
