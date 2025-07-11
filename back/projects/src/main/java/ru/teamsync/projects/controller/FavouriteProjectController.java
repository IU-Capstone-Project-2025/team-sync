package ru.teamsync.projects.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.FavouriteProjectRequest;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.FavouriteProjectResponse;
import ru.teamsync.projects.service.FavouriteProjectService;
import ru.teamsync.projects.service.SecurityContextService;

@RestController
@RequestMapping("/favourite")
@RequiredArgsConstructor
public class FavouriteProjectController {

    private final FavouriteProjectService favouriteProjectService;
    private final SecurityContextService securityContextService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addFavouriteProject(@RequestBody FavouriteProjectRequest request) {
        Long personId = securityContextService.getCurrentUserId();
        favouriteProjectService.addFavouriteProject(personId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(null));
    }

    @GetMapping("/my")
    public BaseResponse<Page<FavouriteProjectResponse>> getFavouriteProjectsByPersonId(Pageable pageable) {
        Long personId = securityContextService.getCurrentUserId();
        return BaseResponse.of(favouriteProjectService.getFavouritesProjectsByPersonId(personId, pageable));
    }

    @DeleteMapping("/{favouriteProjectId}")
    public ResponseEntity<BaseResponse<Void>> deleteFavourite(@PathVariable Long favouriteProjectId) {
        Long personId = securityContextService.getCurrentUserId();
        favouriteProjectService.deleteFavouriteProject(favouriteProjectId, personId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}
