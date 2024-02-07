package umc.precending.domain.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.domain.post.Post;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends Image {
    @JoinColumn(name = "post_id")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    public PostImage(MultipartFile file) {
        this.originalName = file.getOriginalFilename();
        this.storedName = generateStoreName(originalName);
        this.accessUrl = "";
    }

    public PostImage(String originalName, String accessUrl) {
        this.originalName = originalName;
        this.storedName = generateStoreName(originalName);
        this.accessUrl = accessUrl;
    }

    public void initImage(Post post) {
        if(this.post == null) this.post = post;
    }
}