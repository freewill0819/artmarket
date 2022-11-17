package com.artmarket.service;

import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.domain.users.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    /**
     * 이미지 저장
     */
    String saveFile(MultipartFile file, User user, String path) throws IOException;

    /**
     * 이미지 삭제
     */
    void deleteImg(Inquiry inquiry);
}
