package com.shan.tasktrackerapi.api.controllers;

import com.shan.tasktrackerapi.api.factories.TaskDtoFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Transactional
public class TaskController {

    TaskDtoFactory taskDtoFactory;

}
