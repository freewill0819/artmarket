package com.artmarket.controller.api;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.dto.ResponseDto;
import com.artmarket.service.impl.InquiryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inquiry")
public class InquiryApiController {

    private final InquiryServiceImpl inquiryService;

    @PostMapping("/save")
    public ResponseDto<Integer> saveInquiry(@RequestBody Inquiry inquiry,
                                            @AuthenticationPrincipal PrincipalDetail principal) {

        inquiryService.saveInquiry(inquiry, principal.getUser());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/saveFile")
    public ResponseDto<Integer> saveInquiry(@RequestParam MultipartFile file,
                                            @RequestParam String title,
                                            @RequestParam String content,
                                            @AuthenticationPrincipal PrincipalDetail principal) throws IOException {
        inquiryService.saveInquiryFile(title, content, file, principal.getUser());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/update/{id}")
    public ResponseDto<Integer> updateInquiry(@PathVariable Long id,
                                              @RequestBody Inquiry inquiry) {
        inquiryService.updateInquiry(id, inquiry);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/updateFile/{id}")
    public ResponseDto<Integer> updateInquiry(@PathVariable Long id,
                                              @RequestParam MultipartFile file,
                                              @RequestParam String title,
                                              @RequestParam String content) throws IOException {
        inquiryService.updateInquiryFile(id, title, content, file);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto<Integer> deleteInquiry(@PathVariable Long id,
                                              @AuthenticationPrincipal PrincipalDetail principalDetail) {
        inquiryService.deleteInquiry(id, principalDetail);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PutMapping("/answer/{id}")
    public ResponseDto<Integer> answerInquiry(@PathVariable Long id,
                                              @RequestBody Inquiry inquiry) {
        inquiryService.answerUpdate(id, inquiry.getAnswer());
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
