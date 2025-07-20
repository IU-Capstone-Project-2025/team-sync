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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.teamsync.projects.dto.request.FavouriteProjectRequest;
import ru.teamsync.projects.dto.response.BaseResponse;
import ru.teamsync.projects.dto.response.FavouriteProjectResponse;
import ru.teamsync.projects.service.FavouriteProjectService;
import ru.teamsync.projects.service.security.SecurityContextService;

@RestController
@RequestMapping("/favourite")
@RequiredArgsConstructor
@Tag(name = "Favourite Projects", description = "Manage user's list of favourite projects")
public class FavouriteProjectController {

    private final FavouriteProjectService favouriteProjectService;
    private final SecurityContextService securityContextService;

    @Operation(
            summary = "Add a project to favourites",
            description = "Adds the specified project to the current user's list of favourite projects."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project successfully added to favourites"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addFavouriteProject(@RequestBody FavouriteProjectRequest request) {
        Long personId = securityContextService.getCurrentUserId();
        favouriteProjectService.addFavouriteProject(personId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(null));
    }

    @Operation(
            summary = "Get user's favourite projects",
            description = "Returns a paginated list of projects marked as favourite by the current user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved favourite projects")
    })
    @GetMapping("/my")
    public BaseResponse<Page<FavouriteProjectResponse>> getFavouriteProjectsByPersonId(Pageable pageable) {
        Long personId = securityContextService.getCurrentUserId();
        return BaseResponse.of(favouriteProjectService.getFavouritesProjectsByPersonId(personId, pageable));
    }

    @Operation(
            summary = "Remove a project from favourites",
            description = "Removes the specified favourite project from the current user's list."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favourite project successfully removed"),
            @ApiResponse(responseCode = "404", description = "Favourite project not found")
    })
    @DeleteMapping("/{favouriteProjectId}")
    public ResponseEntity<BaseResponse<Void>> deleteFavourite(@PathVariable Long favouriteProjectId) {
        Long personId = securityContextService.getCurrentUserId();
        favouriteProjectService.deleteFavouriteProject(favouriteProjectId, personId);
        return ResponseEntity.ok(BaseResponse.of(null));
    }
}
