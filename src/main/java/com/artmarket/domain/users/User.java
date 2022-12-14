package com.artmarket.domain.users;

import com.artmarket.domain.board.Reply;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@DynamicInsert //insert시 null인 필드는 제외
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

//    @ColumnDefault("user")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @CreationTimestamp //시간 자동 입력
    private Timestamp createDate;

    @OneToMany(mappedBy = "user")
    private List<Reply> replyList;
}
