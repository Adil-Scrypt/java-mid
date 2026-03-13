package com.example.universitysystem.controller;

import com.example.universitysystem.model.Course;
import com.example.universitysystem.model.Lesson;
import com.example.universitysystem.repository.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        // убедимся, что Lessons привязаны к курсу
        if (course.getLessons() != null && !course.getLessons().isEmpty()) {
            List<Lesson> incoming = new ArrayList<>(course.getLessons());
            course.getLessons().clear();
            incoming.forEach(course::addLesson);
        }
        return courseRepository.save(course);
    }

    @PostMapping("/{courseId}/lessons")
    public Course addLessons(
            @PathVariable Long courseId,
            @RequestBody List<Lesson> lessons
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        lessons.forEach(course::addLesson);
        return courseRepository.save(course);
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }
}
