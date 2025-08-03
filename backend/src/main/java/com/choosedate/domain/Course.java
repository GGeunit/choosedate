package com.choosedate.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // createAt을 현재 시간으로 등록하기 위함 
// 코스
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 코스 이름(ex. "주말 데이트 코스")

    @Column(updatable = false) // UPDATE 쿼리에서는 변경 X
    @CreatedDate
    private LocalDateTime createAt; // 생성 시간

    @ManyToOne
    @JoinColumn(name = "user_id") // default(User의 PK를 참조)
    private User user;

    // mappedBy = "course": 상대편(CoursePlace) 필드인 course가 주인임
    // course 필드(주인)가 실제 외래키 (FK)를 관리함
    // Course 클래스에는 coursePlaces 필드가 있지만 Entity로 인해 생성된 course 테이블에는 이 속성이 존재하지 않음
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursePlace> coursePlaces = new ArrayList<>();
}
