package com.artmarket.domain.board;

import com.artmarket.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "image")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 경로
    @Column(nullable = false)
    private String path;

    // 판매글 게시 코드
    @ManyToOne()
    @JoinColumn()
    private Board board;
}
