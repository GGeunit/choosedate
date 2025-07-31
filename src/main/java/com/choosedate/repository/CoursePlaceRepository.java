package com.choosedate.repository;

import com.choosedate.domain.CoursePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePlaceRepository extends JpaRepository<CoursePlace, Long> {
}
