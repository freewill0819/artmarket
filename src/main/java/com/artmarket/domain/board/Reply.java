package com.artmarket.domain.board;

import com.artmarket.domain.users.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne //하나의 게시글에 여러개 답변
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne //하나의 user는 여러개 답변가능
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;

    @Override
    public String toString() {
        return "Reply [id=" + id + ", content=" + content + ", board=" + board + ", user=" + user + ", createDate="
                + createDate + "]";
    }
}
