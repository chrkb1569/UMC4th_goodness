package umc.precending.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.post.NewsPost;
import umc.precending.domain.post.Post;

@Getter
@NoArgsConstructor
public class PostNewsResponseDto extends PostResponseDto {
    private String newsUrl;

    public PostNewsResponseDto(Post post) {
        super(post);
        this.newsUrl = ((NewsPost)post).getNewsUrl();
    }
}
