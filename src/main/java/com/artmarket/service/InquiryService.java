package com.artmarket.service;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.inquiry.Confirm;
import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface InquiryService {

    /**
     * 문의 사항 페이징
     */
    Page<Inquiry> inquiryList(Pageable pageable, @AuthenticationPrincipal PrincipalDetail principal);

    /**
     * 문의 사항 페이징 - confirm 별
     */
    Page<Inquiry> inquiryListConfirm(Pageable pageable, Confirm confirm, @AuthenticationPrincipal PrincipalDetail principal);


    /**
     * 문의 사항 저장
     */
    void saveInquiry(Inquiry inquiry, User user);

    /**
     * 이미지 포함 문의 사항 저장
     * 저장 경로 : C:/artmarket/image/inquiry/{유저ID}
     */
    void saveInquiryFile(String title, String content, MultipartFile file, User user) throws IOException;

    /**
     * 고객 문의 정보 반환
     */
    Inquiry detail(Long id);

    /**
     * 권한 확인
     * inquiry 유저 id 와 user id가 동일하면 true
     */
    Boolean checkAuth(Inquiry inquiry, User user);

    /**
     * 롤 타입 확인
     * 관리자 : true
     * 유저 : false
     */
    Boolean checkAdmin(User user);

    /**
     * 문의 사항 수정
     */
    void updateInquiry(Long id, Inquiry inquiry);

    /**
     * 이미지 포함 문의 사항 수정
     */
    void updateInquiryFile(Long id, String title, String content, MultipartFile file) throws IOException;

    /**
     * 문의 사항 삭제
     * 권한 확인 후 삭제
     */
    void deleteInquiry(Long id, PrincipalDetail principalDetail);

    /**
     * 답변 달기
     */
    void answerUpdate(Long id, String answer);

}
