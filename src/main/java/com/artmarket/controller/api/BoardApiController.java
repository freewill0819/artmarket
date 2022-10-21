package com.artmarket.controller.api;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.board.Board;
import com.artmarket.dto.ReplySaveRequestDto;
import com.artmarket.dto.ResponseDto;
import com.artmarket.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/api/board")
    public ResponseDto<Integer> saveBoard(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        boardService.saveBoard(board, principalDetail.getUsers());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.deleteBoard(id, principal);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> updateForm(@PathVariable Long id, @RequestBody Board board) {
        boardService.updateForm(id, board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

//    @PostMapping("/api/board/{boardId}/reply")
//    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
//        boardService.replySave(replySaveRequestDto);
//
//        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//    }

    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {

        boardService.replySave(replySaveRequestDto);

        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable Long replyId){
//        boardService.replyDelete(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
