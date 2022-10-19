package com.artmarket.service;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.board.Board;
import com.artmarket.domain.users.Users;
import com.artmarket.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void saveBoard(Board board, Users users) {
        board.setCount(0);
        board.setUser(users);
        boardRepository.save(board);
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board BoardDetail(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 ID에 글을 찾을 수 없습니다: " + id);

        });
    }

    @Transactional
    public void deleteBoard(Long id, PrincipalDetail principal) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글을 찾지 못했습니다");
        });

        if (board.getUser().getId() != principal.getUsers().getId()) {
            throw new IllegalArgumentException("해당 글을 삭제할 권한이 없습니다");
        }

        boardRepository.deleteById(id);
        log.info("게시글 id={}", id);
    }

    @Transactional
    public void updateForm(Long id, Board requestBoard) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글이 없습니다");
        });

        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }
}
