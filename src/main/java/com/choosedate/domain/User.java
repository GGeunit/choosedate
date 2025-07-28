package com.choosedate.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // default: @Column(name = "name")
    // 언제 @Column을 사용하는가?
    // → 컬럼 이름이 필드명과 다를 때, 길이 제한이 필요할 때, 제약조건을 줄 때, 칼럼 타입을 직접 지정할 때 등
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String role; // ROLE_USER, ROLE_ADMIN

    @Column(length = 500)
    private String refreshToken;
}
