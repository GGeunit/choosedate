package com.choosedate.repository;

import com.choosedate.domain.Course;
import com.choosedate.domain.CoursePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursePlaceRepository extends JpaRepository<CoursePlace, Long> {
    List<CoursePlace> findByCourseOrderBySequenceAsc(Course course);
}
