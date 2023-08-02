package com.shan.tasktrackerapi.api.factories;

import com.shan.tasktrackerapi.api.dto.ProjectDto;
import com.shan.tasktrackerapi.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity){
        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdateAt())
                .build();
    }

}
