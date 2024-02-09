package umc.precending.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.domain.member.Member;
import umc.precending.domain.post.NewsPost;
import umc.precending.domain.post.Post;
import umc.precending.dto.post.*;
import umc.precending.exception.post.PostNotFoundException;
import umc.precending.exception.post.PostVerifyException;
import umc.precending.factory.PostFactory;
import umc.precending.repository.post.PostRepository;
import umc.precending.service.crawling.CrawlingService;
import umc.precending.service.image.ImageService;
import umc.precending.service.post.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static umc.precending.factory.CategoryFactory.*;
import static umc.precending.factory.MemberFactory.*;
import static umc.precending.factory.PostFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Post [Service Layer] -> PostService 테스트")
public class PostServiceTest {
    @Mock
    private CrawlingService crawlingService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private PostService postService;

    @Nested
    @DisplayName("사용자가 작성한 모든 게시글 조회")
    class findAllTest {
        private static final Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private static final int errorYear = 1234;
        private static final int errorMonth = 13;

        @Test
        @DisplayName("조회한 결과가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<PostResponseDto> responseData = new ArrayList<>();

            // stub
            given(postRepository.findPostsByPrecedingDate(currentMember, errorYear, errorMonth))
                    .willReturn(responseData);

            // when - then
            assertThatThrownBy(() -> postService.findAll(errorYear, errorMonth, currentMember))
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("사용자가 작성한 게시글 조회에 성공한다.")
        public void successFindAll() {
            // given
            List<PostResponseDto> responseData = getPostResponseList();

            // stub
            given(postRepository.findPostsByPrecedingDate(any(), anyInt(), anyInt()))
                    .willReturn(responseData);

            // when
            List<PostResponseDto> resultData = postService.findAll(anyInt(), anyInt(), any());

            // then
            assertThat(resultData).isEqualTo(responseData);
        }
    }

    @Nested
    @DisplayName("단일 게시글 정보 조회")
    class findOneTest {
        private static final long postId = 1;
        private static final long errorId = -1;
        
        @Test
        @DisplayName("유효하지 않은 PK값으로 요청하는 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            // stub
            doThrow(PostNotFoundException.class).when(postRepository).findById(errorId);

            // when - then
            assertThatThrownBy(() -> postService.findOne(errorId))
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("게시글 조회에 성공한다. - 일반 게시글")
        public void successFindOne_Normal() {
            // given
            Member currentMember = MEMBER_1.getPersonalMemberInstance();
            Post post = POST_VERIFIABLE.getPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            PostResponseDto responseData = postService.findOne(postId);

            // then
            assertAll(
                    () -> assertThat(responseData.getContent()).isEqualTo(post.getContent()),
                    () -> assertThat(responseData.getImages().size()).isEqualTo(post.getImageList().size()),
                    () -> assertThat(responseData.getCategories()).isEqualTo(post.getPostCategory().getCategoryList())
            );
        }

        @Test
        @DisplayName("게시글 조회에 성공한다. - 뉴스 게시글")
        public void successFindOne_News() {
            // given
            Member currentMember = MEMBER_1.getPersonalMemberInstance();
            Post post = POST_NEWS.getNewsPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            PostResponseDto responseData = postService.findOne(postId);

            // then
            assertAll(
                    () -> assertThat(responseData.getContent()).isEqualTo(post.getContent()),
                    () -> assertThat(responseData.getImages().size()).isEqualTo(post.getImageList().size()),
                    () -> assertThat(responseData.getCategories()).isEqualTo(post.getPostCategory().getCategoryList()),
                    () -> assertThat(((PostNewsResponseDto)responseData).getNewsUrl()).isEqualTo(((NewsPost)post).getNewsUrl())
            );
        }
    }

    @Nested
    @DisplayName("기업 회원이 작성한 게시글 조회")
    class getCorporatePostTest {
        @Test
        @DisplayName("조회한 결과가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<PostResponseDto> corporatePosts = new ArrayList<>();

            // stub
            given(postRepository.findPostsByCorporate()).willReturn(corporatePosts);

            // when - then
            assertThatThrownBy(() -> postService.getCorporatePost())
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("게시글 조회에 성공한다.")
        public void successGetCorporatePost() {
            // given
            List<PostResponseDto> corporatePosts = getPostResponseList();

            // stub
            given(postRepository.findPostsByCorporate()).willReturn(corporatePosts);

            // when
            List<PostResponseDto> resultData = postService.getCorporatePost();

            // then
            assertThat(resultData).isEqualTo(corporatePosts);
        }
    }

    @Nested
    @DisplayName("동아리 회원이 작성한 게시글 조회")
    class getClubPostTest {
        @Test
        @DisplayName("조회한 결과가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<PostResponseDto> clubPosts = new ArrayList<>();

            // stub
            given(postRepository.findPostsByClub()).willReturn(clubPosts);

            // when - then
            assertThatThrownBy(() -> postService.getClubPost())
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("게시글 조회에 성공한다.")
        public void successGetClubPost() {
            // given
            List<PostResponseDto> clubPosts = getPostResponseList();

            // stub
            given(postRepository.findPostsByClub()).willReturn(clubPosts);

            // when
            List<PostResponseDto> resultData = postService.getClubPost();

            // then
            assertThat(resultData).isEqualTo(clubPosts);
        }
    }

    @Nested
    @DisplayName("인증 가능한 게시글만을 조회")
    class findVerifiablePostTest {
        @Test
        @DisplayName("조회한 결과가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<PostResponseDto> verifiablePosts = new ArrayList<>();

            // stub
            given(postRepository.findVerifiablePosts()).willReturn(verifiablePosts);

            // when - then
            assertThatThrownBy(() -> postService.findVerifiablePost())
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("게시글 조회에 성공한다.")
        public void successFindVerifiablePost() {
            // given
            List<PostResponseDto> verifiablePosts = getPostResponseList();

            // stub
            given(postRepository.findVerifiablePosts()).willReturn(verifiablePosts);

            // when
            List<PostResponseDto> resultData = postService.findVerifiablePost();

            // then
            assertThat(resultData).isEqualTo(verifiablePosts);
        }
    }

    @Nested
    @DisplayName("인증 가능한 선행 기록")
    class makeValidateNormalPostTest {
        private final Member currentMember = MEMBER_1.getPersonalMemberInstance();

        @Test
        @DisplayName("선행 기록에 성공한다.")
        public void successMakeValidateNormalPost() {
            // given
            PostNormalCreateDto createDto = getNormalCreateDto();

            // stub

            // when
            postService.makeValidateNormalPost(createDto, currentMember);

            // then
            verify(postRepository).save(any());
        }
    }

    @Nested
    @DisplayName("인증 가능한 기사 게시글 기록")
    class makeValidateNewsTest {
        private final Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final String NEWS_ACCESS_URL = "NEWS_ACCESS_URL";
        private final String IMAGE_ACCESS_URL = "ACCESS_URL";

        @Test
        @DisplayName("기사 게시글 기록에 성공한다.")
        public void successMakeValidateNewsPost() {
            // given
            PostNewsCreateDto createDto = getNewsCreateDto();
            PostCrawlingResponseDto responseDto = new PostCrawlingResponseDto(NEWS_ACCESS_URL, IMAGE_ACCESS_URL);

            // stub
            given(crawlingService.crawlingData(anyString())).willReturn(responseDto);

            // when
            postService.makeValidateNews(createDto, currentMember);

            // then
            verify(postRepository).save(any());
        }
    }

    @Nested
    @DisplayName("인증 불가능한 선행 기록")
    class makeNormalPostTest {
        private final Member currentMember = MEMBER_1.getPersonalMemberInstance();

        @Test
        @DisplayName("선행 기록에 성공한다.")
        public void successMakeNormalPost() {
            // given
            PostNormalCreateDto createDto = getNormalCreateDto();

            // stub

            // when
            postService.makeNormalPost(createDto, currentMember);

            // then
            verify(postRepository).save(any());
        }
    }

    @Nested
    @DisplayName("게시글 인증")
    class checkPostValidationTest {
        private final Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final long errorId = -1;
        private final long postId = 1;

        @Test
        @DisplayName("유효하지 않은 PK값이 입력된 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            given(postRepository.findById(errorId)).willThrow(PostNotFoundException.class);

            // when - then
            assertThatThrownBy(() -> postService.checkPostValidation(errorId))
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("게시글의 상태가 유효하지 않은 경우, 오류를 반환한다.")
        public void throwExceptionByPostStatus() {
            // given
            Post post = POST_NOT_VERIFIABLE.getPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when - then
            assertThatThrownBy(() -> postService.checkPostValidation(postId))
                    .isInstanceOf(PostVerifyException.class);
        }

        @Test
        @DisplayName("게시글 인증에 성공한다.")
        public void successCheckPostValidation() {
            // given
            Post post = POST_VERIFIABLE.getPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.checkPostValidation(postId);

            // then
            verify(postRepository).findById(postId);
        }
    }

    @Nested
    @DisplayName("기사 게시글 수정")
    class editNewsPostTest {
        private static final Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private static final long errorId = -1;
        private static final long postId = 1;

        @Test
        @DisplayName("유효하지 않은 PK값이 입력된 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            PostNewsEditDto editDto = getNewsEditDto();

            // stub
            given(postRepository.findById(errorId)).willThrow(PostNotFoundException.class);

            // when - then
            assertThatThrownBy(() -> postService.editNewsPost(editDto, currentMember, errorId))
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("기사 게시글 수정에 성공한다.")
        public void successEditNewsPost() {
            // given
            PostNewsEditDto editDto = getNewsEditDto();
            Post post = POST_NEWS.getNewsPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.editNewsPost(editDto, currentMember, postId);

            // then
            verify(postRepository).findById(postId);
        }
    }

    @Nested
    @DisplayName("일반 게시글 수정")
    class editNormalPostTest {
        private static final Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private static final long errorId = -1;
        private static final long postId = 1;

        @Test
        @DisplayName("유효하지 않은 PK값이 입력된 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given
            PostEditDto editDto = getNormalEditDto();

            // stub
            given(postRepository.findById(errorId)).willThrow(PostNotFoundException.class);

            // when - then
            assertThatThrownBy(() -> postService.editNormalPost(editDto, currentMember, errorId))
                    .isInstanceOf(PostNotFoundException.class);
        }

        @Test
        @DisplayName("기사 게시글 수정에 성공한다.")
        public void successEditNormalPost() {
            // given
            PostEditDto editDto = getNormalEditDto();
            Post post = POST_VERIFIABLE.getPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.editNormalPost(editDto, currentMember, postId);

            // then
            verify(postRepository).findById(postId);
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class deletePostTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static long errorId = -1;
        private final static long postId = 1;

        @Test
        @DisplayName("유효하지 않은 PK값이 입력된 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            given(postRepository.findById(errorId)).willThrow(PostNotFoundException.class);

            // when - then
            assertThatThrownBy(() -> postService.deletePost(currentMember, errorId));
        }

        @Test
        @DisplayName("게시글 삭제에 성공한다.")
        public void successDeletePost() {
            // given
            Post post = POST_VERIFIABLE.getPostInstance(currentMember);

            // stub
            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.deletePost(currentMember, postId);

            // then
            verify(postRepository).findById(postId);
        }
    }

    private List<PostResponseDto> getPostResponseList() {
        Member currentMember = MEMBER_1.getPersonalMemberInstance();
        List<Post> posts = new ArrayList<>();

        posts.add(POST_VERIFIABLE.getPostInstance(currentMember));
        posts.add(POST_NOT_VERIFIABLE.getPostInstance(currentMember));
        posts.add(POST_NEWS.getPostInstance(currentMember));

        return posts.stream().map(PostResponseDto::new).toList();
    }

    private PostNormalCreateDto getNormalCreateDto() {
        PostFactory postFactory = POST_VERIFIABLE;
        List<MultipartFile> files = getFiles();
        List<String> categories = getCategories();

        return new PostNormalCreateDto(postFactory.getContent(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), files, categories);
    }

    private PostNewsCreateDto getNewsCreateDto() {
        PostFactory postFactory = POST_NEWS;
        List<String> categories = getCategories();

        return new PostNewsCreateDto(postFactory.getNewsUrl(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), categories);
    }

    private PostEditDto getNormalEditDto() {
        PostFactory postFactory = POST_NOT_VERIFIABLE;
        List<MultipartFile> files = getFiles();
        List<String> categories = getCategories();

        return new PostEditDto(postFactory.getContent(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), categories, files);
    }

    private PostNewsEditDto getNewsEditDto() {
        PostFactory postFactory = POST_NEWS;
        List<String> categories = getCategories();

        return new PostNewsEditDto(postFactory.getNewsUrl(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), categories);
    }

    private List<MultipartFile> getFiles() {
        List<MultipartFile> files = new ArrayList<>();

        files.add(new MockMultipartFile("file.jpeg", "IMAGE_NAME_1.jpeg", "multipart/form-data", new byte[]{}));
        files.add(new MockMultipartFile("file.jpeg", "IMAGE_NAME_2.jpeg", "multipart/form-data", new byte[]{}));
        files.add(new MockMultipartFile("file.jpeg", "IMAGE_NAME_3.jpeg", "multipart/form-data", new byte[]{}));

        return files;
    }

    private List<String> getCategories() {
        List<String> categories = new ArrayList<>();

        categories.add(CATEGORY_VALUE_1.getCategoryName());
        categories.add(CATEGORY_VALUE_2.getCategoryName());

        return categories;
    }
}