package com.artmarket.domain.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReplyTest {

    @Test
    public void toStringTest(){
       Reply reply = Reply.builder()
                .id(1L)
                .user(null)
                .board(null)
                .content("안녕")
                .build();

        System.out.println(reply);
    }

}