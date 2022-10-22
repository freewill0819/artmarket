package com.artmarket.repository;

import com.artmarket.domain.board.Reply;
import com.artmarket.dto.ReplySaveRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {


    @Modifying
    @Query(value = "INSERT INTO reply(user_id, board_id, content, create_date)VALUES(?1, ?2, ?3, now())", nativeQuery=true)
    int mSave(Long userId, Long boardId, String content);
}
