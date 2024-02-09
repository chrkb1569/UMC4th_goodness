package umc.precending.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.category.Category;
import umc.precending.domain.category.PostCategory;
import umc.precending.domain.image.PostImage;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "NEWS_POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsPost extends Post {
    @Column(name = "newsUrl", nullable = false)
    private String newsUrl; // 뉴스에 접근하기 위한 뉴스 링크

    public NewsPost(String writer, String newsUrl, String content,
                    int year, int month, int date,
                    List<Category> categoryList, List<PostImage> imageList) {
        super(year, month, date, content, writer, true, categoryList, imageList);
        this.newsUrl = newsUrl;
    }

    public void editPost(int year, int month, int date, String newsUrl, String content, List<Category> categories) {
        editPrecedingDate(year, month, date);
        updateNews(newsUrl, content);
        this.postCategory = new PostCategory(categories);
    }

    public void editPost(int year, int month, int date, List<Category> categories) {
        editPrecedingDate(year, month, date);
        this.postCategory = new PostCategory(categories);
    }

    private void updateNews(String newsUrl, String content) {
        this.newsUrl = newsUrl;
        this.content = content;
    }
}