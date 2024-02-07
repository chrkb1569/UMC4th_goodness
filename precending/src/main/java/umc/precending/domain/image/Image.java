package umc.precending.domain.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.exception.image.ImageNotSupportedException;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@Table(name = "IMAGE")
@DiscriminatorColumn
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    protected Long id;

    @Column(name = "originalName", nullable = false)
    protected String originalName; // 이미지 파일 본래의 이름

    @Column(name = "storedName", nullable = false)
    protected String storedName; // 저장소에 저장될 경우 사용하게 될 이미지 파일의 이름

    @Column(name = "accessUrl", nullable = false)
    protected String accessUrl; // 이미지 파일에 접근하기 위한 URL

    private final static String[] extensionArr = {"jpg", "jpeg", "bmp", "gif", "png"};

    // accessUrl을 설정하는 메서드
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    // 이미지 파일의 확장자를 추출하는 메서드
    public String extractExtension(String originalName) {
        int index = originalName.lastIndexOf('.');

        return originalName.substring(index + 1);
    }

    // 저장시에 활용할 새로운 이름을 생성하는 메서드
    public String generateStoreName(String originalName) {
        String extension = extractExtension(originalName);

        if(!checkValidation(extension)) throw new ImageNotSupportedException(extension + "은 지원하지 않는 확장자입니다.");

        return UUID.randomUUID() + "." + extension;
    }

    // 이미지 파일의 확장자를 통하여 유효한 이미지 파일인지 확인하는 메서드
    public boolean checkValidation(String extension) {
        return Arrays.stream(extensionArr)
                .anyMatch(value -> value.equals(extension));
    }
}
