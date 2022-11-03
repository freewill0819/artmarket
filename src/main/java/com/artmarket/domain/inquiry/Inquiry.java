package com.artmarket.domain.inquiry;

import com.artmarket.domain.base.BaseEntity;
import com.artmarket.domain.users.RoleType;
import com.artmarket.domain.users.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "inquiry")
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // 답변 여부
    @Enumerated(EnumType.STRING)
    private Confirm confirm;

    // 이미지 경로
    private String img;

    // 답변
    private String answer;

    // 유저 코드
    @ManyToOne()
    @JoinColumn()
    private User user;
}
