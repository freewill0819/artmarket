package com.artmarket.service.impl;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.inquiry.Confirm;
import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.domain.users.RoleType;
import com.artmarket.domain.users.User;
import com.artmarket.repository.InquiryRepository;
import com.artmarket.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ImageServiceImpl imageService;
    private final String imgPath = "C:/artmarket/image/inquiry/";

//    @Override
//    public Page<Inquiry> inquiryList() {
//        return inquiryRepository.findAll(PageRequest.of(0,5, Sort.by("id").descending()));
//    }

    @Override
    public Page<Inquiry> inquiryList(Pageable pageable, @AuthenticationPrincipal PrincipalDetail principal) {

        User user = principal.getUser();

        // 관리자는 모든 문의를 볼 수 있다
        if(checkAdmin(user)) {
            return inquiryRepository.findAll(pageable);
        }

        return inquiryRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Inquiry> inquiryListConfirm(Pageable pageable, Confirm confirm, @AuthenticationPrincipal PrincipalDetail principal) {

        User user = principal.getUser();

        if(checkAdmin(user)) {
            return inquiryRepository.findByConfirm(confirm, pageable);
        }

        return inquiryRepository.findByUserAndConfirm(user, confirm, pageable);
    }

    @Override
    public void saveInquiry(Inquiry inquiry, User user) {
        inquiry.setConfirm(Confirm.X);
        inquiry.setUser(user);
        inquiryRepository.save(inquiry);
    }

    @Override
    public void saveInquiryFile(String title, String content,MultipartFile file, User user) throws IOException {
        // 이미지 저장
        String fileSource = imageService.saveFile(file, user, imgPath);

        // insert
        Inquiry inquiry = new Inquiry();
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setImg(fileSource);
        saveInquiry(inquiry,user);
    }

    @Override
    public Inquiry detail(Long id) {
        return inquiryRepository.findById(id).orElseThrow();
    }

    @Override
    public Boolean checkAuth(Inquiry inquiry, User user) {
        return user.getId().equals(inquiry.getUser().getId());
    }

    @Override
    public Boolean checkAdmin(User user) {
        return user.getRole().equals(RoleType.ADMIN);
    }

    @Override
    public void updateInquiry(Long id, Inquiry editInquiry) {
        Inquiry inquiry = detail(id);

        // delete 면 사진 삭제
        if(editInquiry.getImg().equals("delete")) {
            imageService.deleteImg(inquiry);
            inquiry.setImg(null);
        }
        inquiry.setTitle(editInquiry.getTitle());
        inquiry.setContent(editInquiry.getContent());
        inquiry.setConfirm(Confirm.X);
        inquiryRepository.save(inquiry);
    }

    @Override
    public void updateInquiryFile(Long id, String title, String content, MultipartFile file) throws IOException {
        Inquiry inquiry = detail(id);

        // 기존 이미지 삭제
        if (inquiry.getImg() != null) {
            imageService.deleteImg(inquiry);
        }

        // 새로운 이미지 저장
        String fileSource = imageService.saveFile(file, inquiry.getUser(),imgPath);

        // update
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setConfirm(Confirm.X);
        inquiry.setImg(fileSource);
        inquiryRepository.save(inquiry);
    }

    @Override
    public void deleteInquiry(Long id, PrincipalDetail principalDetail) {
        Inquiry inquiry = detail(id);
        User user = principalDetail.getUser();

        // 권한 확인
        if (checkAuth(detail(id), user) || checkAdmin(user)) {
            if (inquiry.getImg() != null) {
                imageService.deleteImg(inquiry);
            }
            inquiryRepository.deleteById(id);
        }
    }

    @Override
    public void answerUpdate(Long id, String answer) {
        Inquiry inquiry = detail(id);
        inquiry.setAnswer(answer);
        inquiry.setConfirm(Confirm.O);
        inquiryRepository.save(inquiry);
    }

}
