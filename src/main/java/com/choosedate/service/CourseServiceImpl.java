package com.choosedate.service;

import com.choosedate.domain.Course;
import com.choosedate.domain.CoursePlace;
import com.choosedate.domain.Place;
import com.choosedate.domain.User;
import com.choosedate.domain.dto.*;
import com.choosedate.repository.CourseRepository;
import com.choosedate.repository.PlaceRepository;
import com.choosedate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }

    @Override
    public List<CoursePreviewResponseDto> previewCourse(CoursePreviewRequestDto requestDto) {

        List<Place> places = placeRepository.findAllById(requestDto.getPlaceIds());

        return places.stream().map(place -> CoursePreviewResponseDto.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void saveCourse(CourseSaveRequestDto request) {
        User user = getCurrentUser();

        Course course = Course.builder()
                .title(request.getTitle())
                .user(user)
                .build();

        List<CoursePlace> places = new ArrayList<>();
        int order = 1;
        for(Long placeId : request.getPlaceIds()) {
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new IllegalArgumentException("장소를 찾을 수 없습니다."));

            places.add(CoursePlace.builder()
                    .course(course)
                    .place(place)
                    .sequence(order++)
                    .build());
        }
        course.setCoursePlaces(places);
        courseRepository.save(course);
    }

    @Override
    public List<CourseResponseDto> getUserCourses() {
        User user = getCurrentUser();

        List<Course> courses = courseRepository.findByUser(user);

        return courses.stream().map(course ->
                        CourseResponseDto.builder()
                                .id(course.getId())
                                .title(course.getTitle())
                                .places(course.getCoursePlaces().stream().map(cp ->
                                                PlaceResponseDto.builder()
                                                        .id(cp.getId())
                                                        .name(cp.getPlace().getName())
                                                        .address(cp.getPlace().getAddress())
                                                        .latitude(cp.getPlace().getLatitude())
                                                        .longitude(cp.getPlace().getLongitude())
                                                        .build()
                                        ).collect(Collectors.toList()))
                                .build()
                ).collect(Collectors.toList());
    }

    @Override
    public CourseResponseDto getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스가 존재하지 않습니다."));

        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .places(course.getCoursePlaces().stream()
                        // 각 CoursePlace 객체의 sequence 값을 기준으로 정렬
                        // Comparator: 정렬 기준을 정의하는 객체
                        // CoursePlace::getSequence: 각 요소의 sequence 값을 비교하여 오름차순 정렬 수행
                        .sorted(Comparator.comparing(CoursePlace::getSequence))
                        .map(cp -> PlaceResponseDto.builder()
                                .id(cp.getId())
                                .name(cp.getPlace().getName())
                                .address(cp.getPlace().getAddress())
                                .latitude(cp.getPlace().getLatitude())
                                .longitude(cp.getPlace().getLongitude())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}