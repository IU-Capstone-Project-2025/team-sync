package ru.teamsync.projects.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.FavouriteProjectRequest;
import ru.teamsync.projects.dto.response.FavouriteProjectResponse;
import ru.teamsync.projects.entity.FavouriteProject;
import ru.teamsync.projects.entity.Project;
import ru.teamsync.projects.mapper.FavouriteProjectMapper;
import ru.teamsync.projects.repository.FavouriteProjectRepository;
import ru.teamsync.projects.repository.ProjectRepository;
import ru.teamsync.projects.service.exception.ProjectNotFoundException;

@Service
@RequiredArgsConstructor
public class FavouriteProjectService {

    private final FavouriteProjectRepository favouriteProjectRepository;
    private final ProjectRepository projectRepository;

    private final FavouriteProjectMapper favouriteProjectMapper;

    public void addFavouriteProject(Long personId, FavouriteProjectRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> ProjectNotFoundException.withId(request.projectId()));

        boolean exists = favouriteProjectRepository.existsByProjectIdAndPersonId(project.getId(), personId);
        if (exists) {
            throw new IllegalStateException("This project is already in favourites");
        }

        FavouriteProject favourite = new FavouriteProject();
        favourite.setProject(project);
        favourite.setPersontId(personId);
        favourite.setCreatedAt(LocalDateTime.now());

        favouriteProjectRepository.save(favourite);
    }

    public void deleteFavouriteProject(Long favouriteProjectId) {
        boolean exists = favouriteProjectRepository.existsById(favouriteProjectId);
        if (!exists) {
            throw new IllegalStateException("Favourite project not found");
        }

        favouriteProjectRepository.deleteById(favouriteProjectId);
    }

    public Page<FavouriteProjectResponse> getFavouritesProjectsByPersonId(
            Long personId,
            Pageable pageable) {

        return favouriteProjectRepository.findAllByPersonId(personId, pageable)
                .map(favouriteProjectMapper::toDto);
    }
}
