package umc.precending.domain.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.base.BaseEntity;
import umc.precending.domain.category.Category;
import umc.precending.domain.category.PostCategory;
import umc.precending.domain.image.PostImage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "POST")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    protected Long id;

    @Lob
    @Column(name = "content", nullable = false)
    protected String content;

    @Column(name = "writer", nullable = false)
    protected String writer; // 게시글 작성자

    @Column(name = "verifiable", nullable = false)
    protected boolean verifiable; // 인증 가능 여부

    @Column(name = "isVerified", nullable = false)
    protected boolean isVerified; // 인증 여부 - 관리자가 게시글을 인증하였는지를 확인

    @Embedded
    protected PrecedingDate precedingDate; // 선행을 수행한 날짜

    @Embedded
    protected PostCategory postCategory; // 게시글이 속한 카테고리

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "post")
    protected List<PostImage> imageList = new ArrayList<>(); // 게시글에 첨부되는 이미지

    public Post(int year, int month, int date, String content, String writer,
                   boolean verifiable, List<Category> categoryList, List<PostImage> imageList) {
        this.content = content;
        this.writer = writer;
        this.verifiable = verifiable;
        this.isVerified = false;
        this.precedingDate = new PrecedingDate(year, month, date);
        this.postCategory = new PostCategory(categoryList);
        addImages(imageList);
    }

    public void verifyPost() {
        this.isVerified = true;
    }

    public void editInfo(int year, int month, int date, String content, List<Category> categoryList, List<PostImage> images) {
        this.content = content;
        this.postCategory = new PostCategory(categoryList);
        editPrecedingDate(year, month, date);
        editImages(images);
    }

    public void editInfo(int year, int month, int date, String content, List<Category> categoryList) {
        this.content = content;
        this.postCategory = new PostCategory(categoryList);
        editPrecedingDate(year, month, date);
    }

    public void editImages(List<PostImage> images) {
        imageList.removeAll(this.imageList);
        addImages(images);
    }

    protected void editPrecedingDate(int year, int month, int date) {
        this.precedingDate = new PrecedingDate(year, month, date);
    }

    protected void addImages(List<PostImage> images) {
        for(PostImage image : images) {
            image.initImage(this);
            this.imageList.add(image);
        }
    }
}