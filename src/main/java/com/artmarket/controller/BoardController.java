package com.artmarket.controller;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.board.File;
import com.artmarket.dto.FileDto;
import com.artmarket.repository.FileRepository;
import com.artmarket.service.BoardService;
import com.artmarket.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final FileRepository fileRepository;

    private final FileService fileService;

    @GetMapping({"","/"})
    public String index(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        model.addAttribute("boardList", boardService.boardList(pageable));
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }



    @GetMapping("/auth/board/{id}")
    public String BoardDetail(
            @PathVariable Long id, Model model,
            @AuthenticationPrincipal PrincipalDetail principalDetail
            ) {
        model.addAttribute("boardDetail", boardService.BoardDetail(id));
        model.addAttribute("principalDetail", principalDetail);
        return "board/detail";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("updateBoard", boardService.BoardDetail(id));
        return "board/updateForm";
    }
}
