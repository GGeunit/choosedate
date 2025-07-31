package com.choosedate.controller;

import com.choosedate.domain.dto.CoursePreviewRequestDto;
import com.choosedate.domain.dto.CoursePreviewResponseDto;
import com.choosedate.domain.dto.CourseResponseDto;
import com.choosedate.domain.dto.CourseSaveRequestDto;
import com.choosedate.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseContoller {

    private final CourseService courseService;

    @PostMapping("/preview")
    public ResponseEntity<List<CoursePreviewResponseDto>> previewCourse(@RequestBody CoursePreviewRequestDto requestDto) {
        List<CoursePreviewResponseDto> preview = courseService.previewCourse(requestDto);
        return ResponseEntity.ok(preview);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveCourse(@RequestBody CourseSaveRequestDto request) {
        courseService.saveCourse(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getUserCourses() {
        return ResponseEntity.ok(courseService.getUserCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }
}
