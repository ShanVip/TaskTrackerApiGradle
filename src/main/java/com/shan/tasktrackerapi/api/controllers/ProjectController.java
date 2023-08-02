package com.shan.tasktrackerapi.api.controllers;

import com.shan.tasktrackerapi.api.dto.ProjectDto;
import com.shan.tasktrackerapi.api.exceptions.BadRequestExceptions;
import com.shan.tasktrackerapi.api.factories.ProjectDtoFactory;
import com.shan.tasktrackerapi.store.entities.ProjectEntity;
import com.shan.tasktrackerapi.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Transactional
public class ProjectController {

    ProjectRepository projectRepository;
    ProjectDtoFactory projectDtoFactory;

    @PostMapping
    @RequestMapping("/api/projects/{project_id}/tasks")
    public ProjectDto createProject(@RequestParam String name){

        projectRepository
                .findByName(name)
                .ifPresent(project ->{
                    throw new BadRequestExceptions("Project \"%s\" already exists.", name);
                });




        return ProjectDtoFactory.makeProjectDto();
    }

}
