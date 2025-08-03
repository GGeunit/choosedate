package com.choosedate.controller;

import com.choosedate.domain.dto.*;
import com.choosedate.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseContoller {

    private final CourseService courseService;

    // 코스 미리보기
    @PostMapping("/preview")
    public ResponseEntity<List<CoursePreviewResponseDto>> previewCourse(@RequestBody CoursePreviewRequestDto requestDto) {
        List<CoursePreviewResponseDto> preview = courseService.previewCourse(requestDto);
        return ResponseEntity.ok(preview);
    }

    // 코스 저장
    @PostMapping("/save")
    public ResponseEntity<Void> saveCourse(@RequestBody CourseSaveRequestDto request) {
        courseService.saveCourse(request);
        return ResponseEntity.ok().build();
    }

    // 사용자 코스 목록 조회
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getUserCourses() {
        return ResponseEntity.ok(courseService.getUserCourses());
    }
    
    // 단일 코스 상세 조회
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    // 코스 제목/순서 수정
    @PutMapping("/{courseId}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long courseId, @RequestBody CourseUpdateRequestDto requestDto) {
        courseService.updateCourse(courseId, requestDto);
        return ResponseEntity.ok().build();
    }

    // 코스 삭제
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }

    // 코스 위도/경로 리스트 반환
    @GetMapping("/{courseId}/locations")
    public ResponseEntity<List<CourseLocationDto>> getCourseLocations(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseLocations(courseId));
    }
}
