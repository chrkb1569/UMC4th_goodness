package umc.precending.domain;

import org.junit.jupiter.api.*;
import umc.precending.domain.category.Category;
import umc.precending.domain.image.PostImage;
import umc.precending.domain.member.Member;
import umc.precending.domain.post.NewsPost;
import umc.precending.domain.post.Post;
import umc.precending.factory.CategoryFactory;
import umc.precending.factory.ImageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.PostFactory.*;

@DisplayName("Post 도메인 테스트")
public class PostTest {
    private Member member;
    private Post post;

    @Nested
    @DisplayName("Post 도메인 생성 테스트")
    class createDomainTest {
        private final static int EMPTY_LIST_SIZE = 0;
        @Test
        @DisplayName("Post 도메인 생성을 테스트한다.")
        public void createPostDomainTest() {
            // given
            member = MEMBER_1.getPersonalMemberInstance();

            // when
            post = POST_NOT_VERIFIABLE.getPostInstance(member);

            // then
            assertAll(
                    () -> assertThat(post.getId()).isNull(),
                    () -> assertThat(post.getWriter()).isEqualTo(member.getUsername()),
                    () -> assertThat(post.getPrecedingDate().getYear()).isEqualTo(POST_NOT_VERIFIABLE.getYear()),
                    () -> assertThat(post.getPrecedingDate().getMonth()).isEqualTo(POST_NOT_VERIFIABLE.getMonth()),
                    () -> assertThat(post.getPrecedingDate().getDate()).isEqualTo(POST_NOT_VERIFIABLE.getDate()),
                    () -> assertThat(post.getPostCategory().getCategoryList().size()).isNotEqualTo(EMPTY_LIST_SIZE),
                    () -> assertThat(post.getImageList().size()).isNotEqualTo(EMPTY_LIST_SIZE),
                    () -> assertThat(post.isVerifiable()).isEqualTo(POST_NOT_VERIFIABLE.isVerifiable())
            );
        }

        @Test
        @DisplayName("NewsPost 도메인 생성을 테스트한다.")
        public void createNewsPostDomainTest() {
            // given
            member = MEMBER_1.getPersonalMemberInstance();

            // when
            post = POST_NOT_VERIFIABLE.getNewsPostInstance(member);

            // then
            assertAll(
                    () -> assertThat(post.getId()).isNull(),
                    () -> assertThat(post.getWriter()).isEqualTo(member.getUsername()),
                    () -> assertThat(post.getPrecedingDate().getYear()).isEqualTo(POST_NOT_VERIFIABLE.getYear()),
                    () -> assertThat(post.getPrecedingDate().getMonth()).isEqualTo(POST_NOT_VERIFIABLE.getMonth()),
                    () -> assertThat(post.getPrecedingDate().getDate()).isEqualTo(POST_NOT_VERIFIABLE.getDate()),
                    () -> assertThat(post.getPostCategory().getCategoryList().size()).isNotEqualTo(EMPTY_LIST_SIZE),
                    () -> assertThat(post.getImageList().size()).isNotEqualTo(EMPTY_LIST_SIZE),
                    () -> assertThat(post.isVerifiable()).isTrue(),
                    () -> assertThat(((NewsPost)post).getNewsUrl()).isEqualTo(POST_NOT_VERIFIABLE.getNewsUrl())
            );
        }
    }

    @Nested
    @DisplayName("editInfo() 메서드 테스트")
    class editInfoTest {
        private final static List<Category> otherCategories = CategoryFactory.getRandomCategoryList();
        private final static int otherYear = 1234;
        private final static int otherMonth = 2;
        private final static int otherDate = 31;
        private final static String otherContent = "Other Content";

        @Test
        @DisplayName("게시글의 정보만 수정한다.")
        public void editInfoTestWithoutImage() {
            // given
            member = MEMBER_1.getPersonalMemberInstance();
            post = POST_VERIFIABLE.getPostInstance(member);

            // when
            post.editInfo(otherYear, otherMonth, otherDate, otherContent, otherCategories);

            // then
            assertAll(
                    () -> assertThat(post.getPrecedingDate().getYear()).isEqualTo(otherYear),
                    () -> assertThat(post.getPrecedingDate().getMonth()).isEqualTo(otherMonth),
                    () -> assertThat(post.getPrecedingDate().getDate()).isEqualTo(otherDate),
                    () -> assertThat(post.getContent()).isEqualTo(otherContent),
                    () -> assertThat(post.getPostCategory().getCategoryList())
                            .isEqualTo(otherCategories.stream().map(Category::getCategoryName).toList())
            );
        }

        @Test
        @DisplayName("게시글의 이미지까지 수정한다.")
        public void editInfoTestWithImage() {
            // given
            List<PostImage> images = getPostImages();
            member = MEMBER_1.getPersonalMemberInstance();
            post = POST_VERIFIABLE.getPostInstance(member);

            // when
            post.editInfo(otherYear, otherMonth, otherDate, otherContent, otherCategories, images);

            // then
            assertAll(
                    () -> assertThat(post.getPrecedingDate().getYear()).isEqualTo(otherYear),
                    () -> assertThat(post.getPrecedingDate().getMonth()).isEqualTo(otherMonth),
                    () -> assertThat(post.getPrecedingDate().getDate()).isEqualTo(otherDate),
                    () -> assertThat(post.getContent()).isEqualTo(otherContent),
                    () -> assertThat(post.getPostCategory().getCategoryList())
                            .isEqualTo(otherCategories.stream().map(Category::getCategoryName).toList()),
                    () -> assertThat(post.getImageList().size()).isEqualTo(images.size()),
                    () -> assertThat(post.getImageList()).isEqualTo(images)
            );
        }
    }

    private List<PostImage> getPostImages() {
        List<PostImage> images = new ArrayList<>();

        images.add(ImageFactory.IMAGE_1.getPostImage());
        images.add(ImageFactory.IMAGE_2.getPostImage());

        return images;
    }
}