package com.choosedate.service;

import com.choosedate.domain.dto.*;

import java.util.List;

public interface CourseService {
    List<CoursePreviewResponseDto> previewCourse(CoursePreviewRequestDto requestDto);
    void saveCourse(CourseSaveRequestDto request);
    List<CourseResponseDto> getUserCourses();
    CourseResponseDto getCourse(Long courseId);
    void updateCourse(Long courseId, CourseUpdateRequestDto requestDto);
    void deleteCourse(Long courseId);
    List<CourseLocationDto> getCourseLocations(Long courseId);
}
