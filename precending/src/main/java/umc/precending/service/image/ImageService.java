package umc.precending.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String saveImage(MultipartFile file, String storedName); // 이미지 파일을 새롭게 저장하는 로직, 저장시 접근 URL 반환
}