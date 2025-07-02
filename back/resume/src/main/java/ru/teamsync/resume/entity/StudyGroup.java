package ru.teamsync.resume.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
<<<<<<< HEAD
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> 9dd1c4e15c55aafabb3e1a55a2d1c22550c92687

@Data
@Entity
<<<<<<< HEAD
@Getter
@Setter
@NoArgsConstructor
=======
@Builder
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> 9dd1c4e15c55aafabb3e1a55a2d1c22550c92687
public class StudyGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
