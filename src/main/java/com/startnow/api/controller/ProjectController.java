package com.startnow.api.controller;

import com.startnow.api.dto.CreateProjectDto;
import com.startnow.api.dto.ProjectResponseDto;
import com.startnow.api.model.Project;
import com.startnow.api.model.User;
import com.startnow.api.repository.ProjectRepository;
import com.startnow.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestBody CreateProjectDto createProjectDto,
            @AuthenticationPrincipal UserDetails userDetails) {


        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project newProject = new Project();
        newProject.setHtmlContent(createProjectDto.getHtmlContent());
        newProject.setCssContent(createProjectDto.getCssContent());
        newProject.setJsContent(createProjectDto.getJsContent());
        newProject.setUser(currentUser);

        Project savedProject = projectRepository.save(newProject);

        ProjectResponseDto responseDto = new ProjectResponseDto(
                savedProject.getId(),
                savedProject.getHtmlContent(),
                savedProject.getCssContent(),
                savedProject.getJsContent(),
                savedProject.getUser().getId()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getMyProjects(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<ProjectResponseDto> projects = projectRepository.findByUserId(currentUser.getId()).stream()
                .map(project -> new ProjectResponseDto(
                        project.getId(),
                        project.getHtmlContent(),
                        project.getCssContent(),
                        project.getJsContent(),
                        project.getUser().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(projects);
    }
}
