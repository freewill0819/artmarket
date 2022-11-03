package com.artmarket.controller;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.inquiry.Confirm;
import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.domain.users.User;
import com.artmarket.dto.MessageDto;
import com.artmarket.service.impl.InquiryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryServiceImpl inquiry;

    /**
     * 고객 문의 메인
     */
    @GetMapping()
    public String inquiryMain(Model model,
                              @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
                              @AuthenticationPrincipal PrincipalDetail principal) {
        // 현재 로그인 한 계정의 문의 사항만
        model.addAttribute("inquiryList", inquiry.inquiryList(pageable, principal));
        return "/inquiry/main";
    }

    /**
     * 고객 문의 메인 - confirm 별
     */
    @GetMapping("/confirm/{confirm}")
    public String inquiryMainConfirm(@PathVariable Confirm confirm,
                                     @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
                                     @AuthenticationPrincipal PrincipalDetail principalDetail,
                                     Model model) {
        // 선택 confirm 별 문의사항 보기
        model.addAttribute("inquiryList", inquiry.inquiryListConfirm(pageable, confirm, principalDetail));
        return "/inquiry/main";
    }

    /**
     * 문의글 쓰기
     */
    @GetMapping("/writing")
    public String writing() {
        return "/inquiry/writing";
    }

    /**
     * 문의 상세 조회
     */
    @GetMapping("/{id}")
    public String BoardDetail(
            @PathVariable Long id, Model model,
            @AuthenticationPrincipal PrincipalDetail principalDetail
    ) {
        Inquiry detail = inquiry.detail(id);
        User user = principalDetail.getUser();

        Boolean auth = inquiry.checkAuth(detail, user);

        // 본인 문의 사항 or 관리자만 확인 가능
        if(auth || inquiry.checkAdmin(user)) {
            model.addAttribute("inquiry", detail);
            model.addAttribute("X", Confirm.X);
            model.addAttribute("principalDetail", principalDetail);
            return "inquiry/detail";
        } else {
            return noAuth(model);
        }
    }

    /**
     * 권한 없음
     */
    private static String noAuth(Model model) {
        model.addAttribute("data",new MessageDto("권한이 없습니다.", "/inquiry"));
        return "/layout/message";
    }

    /**
     * 문의 사항 수정
     */
    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        Inquiry detail = inquiry.detail(id);
        User user = principalDetail.getUser();

        // 본인만 수정 가능
        if(inquiry.checkAuth(detail,user)) {
            model.addAttribute("inquiry", inquiry.detail(id));
            return "/inquiry/update";
        } else {
            return noAuth(model);
        }
    }
}
