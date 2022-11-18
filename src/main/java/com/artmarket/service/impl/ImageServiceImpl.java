package com.artmarket.service.impl;

import com.artmarket.domain.inquiry.Inquiry;
import com.artmarket.domain.users.User;
import com.artmarket.repository.ImageRepository;
import com.artmarket.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public String saveFile(MultipartFile file, User user, String path) throws IOException {
        // 이미지 저장 경로 설정
        String userid = String.valueOf(user.getId());
        Path filePath = Paths.get(path,userid);
        if (Files.notExists(filePath)) {
            Files.createDirectories(filePath);
        }

        // 확장자
        String ext = findExt(Objects.requireNonNull(file.getOriginalFilename()));

        // 파일 이름 랜덤 생성
        String date = LocalDateTime.now().toString().replaceAll("[:.-]","");
        UUID uuid = UUID.randomUUID();
        String saveName = date + uuid + "." + ext;

        Path targetPath = Paths.get(filePath.toString(), saveName);

        // 이미지 저장
        file.transferTo(targetPath);

        /*
        Window 에서 이미지 경로 불러올 때 "\" 일 때 오류 발생, "/"로 변경
        Mac, Linux 에서는 미확인
         */
        return targetPath.toString().replace("\\","/");
    }

    @Override
    public void deleteImg(Inquiry inquiry){
        try {
            File file = new File(inquiry.getImg());
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 확장자 추출
     */
    private String findExt(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
