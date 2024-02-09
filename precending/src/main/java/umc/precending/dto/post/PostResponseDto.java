package umc.precending.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.category.PostCategory;
import umc.precending.domain.image.PostImage;
import umc.precending.domain.post.Post;
import umc.precending.dto.image.ImageDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String content;
    private LocalDateTime firstCreatedDate;
    private LocalDateTime lastModifiedDate;
    private boolean verifiable;
    private boolean isVerified;
    private List<ImageDto> images = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.firstCreatedDate = post.getFirstCreatedDate();
        this.lastModifiedDate = post.getLastModifiedDate();
        this.verifiable = post.isVerifiable();
        this.isVerified = post.isVerified();
        this.images = post.getImageList().stream()
                .map(ImageDto::toDto).collect(Collectors.toList());
        this.categories = List.copyOf(post.getPostCategory().getCategoryList());
    }

    @QueryProjection
    public PostResponseDto(long id, String content, LocalDateTime firstCreatedDate, LocalDateTime lastModifiedDate,
                           boolean verifiable, boolean isVerified,
                           List<PostImage> imageList, PostCategory postCategory) {
        this.id = id;
        this.content = content;
        this.firstCreatedDate = firstCreatedDate;
        this.lastModifiedDate = lastModifiedDate;
        this.verifiable = verifiable;
        this.isVerified = isVerified;
        this.images = imageList.stream().map(ImageDto::toDto).collect(Collectors.toList());
        this.categories = List.copyOf(postCategory.getCategoryList());
    }
}