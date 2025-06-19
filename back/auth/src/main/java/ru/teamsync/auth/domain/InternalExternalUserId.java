package ru.teamsync.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "internal_external_user_id")
@Getter
@Setter
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class InternalExternalUserId {

    @EmbeddedId
    private InternalExternalUserIdKey id;

    @Column(name="created_at", nullable = false)
    private OffsetDateTime createdAt;

}
