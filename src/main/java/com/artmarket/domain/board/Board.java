package com.artmarket.domain.board;

import com.artmarket.domain.users.Users;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob //일반적인 데이터베이스에서 저장하는 길이인 255개 이상의 문자를 저장하고 싶을 때 지정한다.
    private String content;

    @ColumnDefault("0")
    private int count;

    @ManyToOne(fetch = FetchType.EAGER)//즉시로딩
    @JoinColumn(name = "userId")
    private Users user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    //주인(pk)이 아니다
    private List<Reply> reply;

    private Timestamp createDate;

}
