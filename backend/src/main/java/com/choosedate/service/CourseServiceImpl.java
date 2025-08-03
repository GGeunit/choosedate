package com.choosedate.service;

import com.choosedate.domain.Course;
import com.choosedate.domain.CoursePlace;
import com.choosedate.domain.Place;
import com.choosedate.domain.User;
import com.choosedate.domain.dto.*;
import com.choosedate.repository.CourseRepository;
import com.choosedate.repository.PlaceRepository;
import com.choosedate.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    // 코스 미리보기
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

    // 코스 저장
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

    // 사용자 코스 목록 조회
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

    // 단일 코스 상세조회
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

    // 코스 제목/순서 수정
    @Override
    public void updateCourse(Long courseId, CourseUpdateRequestDto requestDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));

        // 제목 수정
        course.setTitle(requestDto.getTitle());

        // 기존 CoursePlace 삭제
        course.getCoursePlaces().clear();

        // 새로은 CoursePlace 리스트 생성
        List<CoursePlace> updatedPlaces = requestDto.getPlaces().stream().map(dto -> {
            Place place = placeRepository.findById(dto.getPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("장소가 존재하지 않습니다."));

            return CoursePlace.builder()
                    .course(course)
                    .place(place)
                    .sequence(dto.getSequence())
                    .build();
        }).toList();

        course.getCoursePlaces().addAll(updatedPlaces);
        courseRepository.save(course);
    }

    // 코스 삭제
    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));
        courseRepository.delete(course);
    }

    // 코스 위도/경로 리스트 반환
    @Override
    public List<CourseLocationDto> getCourseLocations(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스가 존재하지 않습니다."));

        return course.getCoursePlaces().stream()
                .sorted(Comparator.comparingInt(CoursePlace::getSequence))
                .map(cp -> {
                    Place p = cp.getPlace();
                    return CourseLocationDto.builder()
                            .latitude(p.getLatitude())
                            .longitude(p.getLongitude())
                            .build();
                }).collect(Collectors.toList());
    }

}