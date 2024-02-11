package umc.precending.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.image.Image;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String originalName;
    private String storedName;
    private String accessUrl;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getOriginalName(), image.getStoredName(), image.getAccessUrl());
    }
}