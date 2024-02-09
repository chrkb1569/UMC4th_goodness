package umc.precending.factory;

import umc.precending.domain.category.Category;
import umc.precending.domain.image.PostImage;
import umc.precending.domain.member.Member;
import umc.precending.domain.post.NewsPost;
import umc.precending.domain.post.Post;

import java.util.ArrayList;
import java.util.List;

public enum PostFactory {
    POST_VERIFIABLE("Post Content_1", true, 9999, 12, 31, "NEWS_URL_1"),
    POST_NOT_VERIFIABLE("Post Content_2",  false, 9999, 12, 31, "NEWS_URL_2"),
    POST_NEWS("Post Content_3", true, 9999, 12, 31, "NEWS_URL_3")
    ;
    private String content;
    private boolean verifiable;
    private int year;
    private int month;
    private int date;

    private String newsUrl;

    PostFactory(String content, boolean verifiable, int year, int month, int date, String newsUrl) {
        this.content = content;
        this.verifiable = verifiable;
        this.year = year;
        this.month = month;
        this.date = date;
        this.newsUrl = newsUrl;
    }

    public Post getPostInstance(Member member) {
        List<PostImage> images = getImageList();
        List<Category> categories = getPostCategories();

        return new Post(year, month, date, content, member.getUsername(), verifiable, categories, images);
    }

    public NewsPost getNewsPostInstance(Member member) {
        List<PostImage> images = getImageList();
        List<Category> categories = getPostCategories();

        return new NewsPost(member.getUsername(), newsUrl, content, year, month, date, categories, images);
    }

    private List<PostImage> getImageList() {
        List<PostImage> images = new ArrayList<>();

        images.add(ImageFactory.IMAGE_1.getPostImage());
        images.add(ImageFactory.IMAGE_2.getPostImage());

        return images;
    }

    private List<Category> getPostCategories() {
        return CategoryFactory.getRandomCategoryList();
    }

    public String getContent() {
        return content;
    }

    public boolean isVerifiable() {
        return verifiable;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}