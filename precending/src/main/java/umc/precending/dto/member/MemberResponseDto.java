package umc.precending.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.image.MemberImage;
import umc.precending.dto.image.ImageDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private String name;
    private String introduction;
    private List<ImageDto> imageList = new ArrayList<>();

    @QueryProjection
    public MemberResponseDto(String name, String introduction, List<MemberImage> imageList) {
        this.name = name;
        this.introduction = introduction;
        this.imageList = imageList.stream().map(ImageDto::toDto).toList();
    }
}