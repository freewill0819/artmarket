package com.artmarket.service;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.board.Board;
import com.artmarket.domain.board.File;
import com.artmarket.dto.BoardDto;
import com.artmarket.dto.FileDto;
import com.artmarket.dto.ReplySaveRequestDto;
import com.artmarket.repository.BoardRepository;
import com.artmarket.repository.FileRepository;
import com.artmarket.repository.ReplyRepository;
import com.artmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    @Transactional
    public Long saveBoard(BoardDto boardDto) {

        return boardRepository.save(boardDto.toEntity()).getId();
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

        if (board.getUser().getId() != principal.getUser().getId()) {
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


    @Transactional
    public void replySave(ReplySaveRequestDto replySaveRequestDto) {

        int result = replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());

        System.out.println("BoardService = " + result);
    }

    @Transactional
    public void replyDelete(Long replyId) {

        replyRepository.deleteById(replyId);

    }

    public void saveBoard(Board board, PrincipalDetail principalDetail) {
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
    @Transactional
    public BoardDto getPost(Long id) {
        Board board = boardRepository.findById(id).get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .fileId(board.getFileId())
                .build();
        return boardDto;
    }
}
