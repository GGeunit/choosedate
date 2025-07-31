package com.choosedate.service;

import com.choosedate.domain.dto.CoursePreviewRequestDto;
import com.choosedate.domain.dto.CoursePreviewResponseDto;
import com.choosedate.domain.dto.CourseResponseDto;
import com.choosedate.domain.dto.CourseSaveRequestDto;

import java.util.List;

public interface CourseService {
    List<CoursePreviewResponseDto> previewCourse(CoursePreviewRequestDto requestDto);
    void saveCourse(CourseSaveRequestDto request);
    List<CourseResponseDto> getUserCourses();
    CourseResponseDto getCourse(Long courseId);
}
