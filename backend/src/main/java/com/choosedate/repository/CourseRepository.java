package com.choosedate.repository;

import com.choosedate.domain.Course;
import com.choosedate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUser(User user);
}
