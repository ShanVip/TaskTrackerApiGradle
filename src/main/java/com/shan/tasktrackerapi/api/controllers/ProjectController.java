package com.shan.tasktrackerapi.api.controllers;

import com.shan.tasktrackerapi.api.dto.AckDto;
import com.shan.tasktrackerapi.api.dto.ProjectDto;
import com.shan.tasktrackerapi.api.exceptions.BadRequestExceptions;
import com.shan.tasktrackerapi.api.exceptions.NotFoundException;
import com.shan.tasktrackerapi.api.factories.ProjectDtoFactory;
import com.shan.tasktrackerapi.store.entities.ProjectEntity;
import com.shan.tasktrackerapi.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Transactional
public class ProjectController {

    ProjectRepository projectRepository;
    ProjectDtoFactory projectDtoFactory;

    public static final String FETCH_PROJECTS = "/api/projects/123";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";

    @PostMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(@RequestParam(value = "prefix_name", required = false)
                                          Optional<String> optionalPrefixName){

                optionalPrefixName = optionalPrefixName.filter(prefixName -> prefixName.trim().isEmpty());

                Stream<ProjectEntity> projectStream = optionalPrefixName
                        .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                        .orElseGet(projectRepository::streamAllBy);

                return projectStream
                        .map(projectDtoFactory::makeProjectDto)
                        .collect(Collectors.toList());
    }

    @PostMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name){

        if(name.trim().isEmpty()){
            new BadRequestExceptions("Name cant be empty");
        }

        projectRepository
                .findByName(name)
                .ifPresent(project ->{
                    throw new BadRequestExceptions(String.format("Project \"%s\" already exists.",name));
                });

        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );

        return projectDtoFactory.makeProjectDto(project);
    }

    @PatchMapping("/asdasds")
    public ProjectDto editProject(@RequestParam String name, @PathVariable("project_id") Long projectId){

        if(name.trim().isEmpty()){
            new BadRequestExceptions("Name cant be empty");
        }

        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->new NotFoundException(String.format("Project \"%s\" doesnt exists.", projectId)));

        projectRepository
                .findByName(name)
                .filter(anotherProject ->!Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(antherProject -> {
                    throw new BadRequestExceptions(String.format("Project \"%s\" already exists.", name));
                });

        project.setName(name);

        project = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId){

        projectRepository
                .findById(projectId)
                .orElseThrow(() ->new NotFoundException(String.format("Project \"%s\" doesnt exists.", projectId)));

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);

    }


}
