package umc.precending.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.controller.post.PostController;
import umc.precending.domain.member.Member;
import umc.precending.dto.post.*;
import umc.precending.factory.PostFactory;
import umc.precending.service.member.MemberFindService;
import umc.precending.service.post.PostService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static umc.precending.factory.CategoryFactory.*;
import static umc.precending.factory.MemberFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Post [Controller Layer] -> PostController 테스트")
public class PostControllerTest {
    @Mock
    private MemberFindService memberFindService;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void initTest() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .build();
    }

    @Nested
    @DisplayName("전체 게시글 조회 API [GET /api/posts/{year}/{month}]")
    class findAllTest {
        private final static Member currentMember = MEMBER_1.getPersonalMemberInstance();
        private final static String BASE_URL = "/api/posts/{year}/{month}";
        private final static int year = 12;
        private final static int month = 31;

        @Test
        @DisplayName("전체 게시글 조회에 성공한다.")
        public void successFindAll() throws Exception {
            // given
            List<PostResponseDto> responseData = new ArrayList<>();

            // stub
            given(memberFindService.findCurrentMember()).willReturn(currentMember);
            given(postService.findAll(year, month, currentMember)).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL, year, month))
                    .andExpect(status().isOk());

            // then
            verify(memberFindService).findCurrentMember();
            verify(postService).findAll(year, month, currentMember);
        }
    }

    @Nested
    @DisplayName("단일 게시글 조회 API [GET /api/posts/{id}]")
    class findOneTest {
        private final static String BASE_URL = "/api/posts/{id}";
        private final static long postId = 1;

        @Test
        @DisplayName("단일 게시글 조회에 성공한다.")
        public void successFindOne() throws Exception {
            // given
            PostResponseDto postResponseDto = new PostResponseDto();

            // stub
            given(postService.findOne(postId)).willReturn(postResponseDto);

            // when
            mockMvc.perform(get(BASE_URL, postId))
                    .andExpect(status().isOk());

            // then
            verify(postService).findOne(postId);
        }
    }

    @Nested
    @DisplayName("기업 게시글 조회 API [GET /api/posts/corporate]")
    class findCorporatePostTest {
        private final static String BASE_URL = "/api/posts/corporate";

        @Test
        @DisplayName("기업 게시글 조회에 성공한다.")
        public void successFindCorporatePost() throws Exception {
            // given
            List<PostResponseDto> responseData = new ArrayList<>();

            // stub
            given(postService.getCorporatePost()).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk());

            // then
            verify(postService).getCorporatePost();
        }
    }

    @Nested
    @DisplayName("동아리 게시글 조회 API [GET /api/posts/club]")
    class findClubPostTest {
        private final static String BASE_URL = "/api/posts/club";

        @Test
        @DisplayName("동아리 게시글 조회에 성공한다.")
        public void successFindClubPost() throws Exception {
            // given
            List<PostResponseDto> responseData = new ArrayList<>();

            // stub
            given(postService.getClubPost()).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk());

            // then
            verify(postService).getClubPost();
        }
    }

    @Nested
    @DisplayName("인증 가능한 게시글 조회 API [GET /api/admin/posts]")
    class findValidPostsTest {
        private final static String BASE_URL = "/api/admin/posts";

        @Test
        @DisplayName("인증 가능한 게시글 조회에 성공한다.")
        public void successFindValidPosts() throws Exception {
            // given
            List<PostResponseDto> responseData = new ArrayList<>();

            // stub
            given(postService.findVerifiablePost()).willReturn(responseData);

            // when
            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk());

            // then
            verify(postService).findVerifiablePost();
        }
    }

    @Nested
    @DisplayName("인증 가능한 일반 게시글 생성 API [POST /api/posts/valid/normal]")
    class makeValidatePostTest {
        private final static String BASE_URL = "/api/posts/valid/normal";

        @Test
        @DisplayName("인증 가능한 일반 게시글 생성에 성공한다.")
        public void successMakeValidatePost() throws Exception {
            // given
            PostNormalCreateDto createDto = getNormalCreateDto();

            // stub
            willDoNothing().given(postService).makeValidateNormalPost(any(), any());

            // when
            mockMvc.perform(
                    multipart(HttpMethod.POST, BASE_URL)
                            .file("files", createDto.getFiles().get(0).getBytes())
                            .file("files", createDto.getFiles().get(1).getBytes())
                            .file("files", createDto.getFiles().get(2).getBytes())
                            .param("content", createDto.getContent())
                            .param("year", String.valueOf(createDto.getYear()))
                            .param("month", String.valueOf(createDto.getMonth()))
                            .param("day", String.valueOf(createDto.getDay()))
                            .param("categories", String.valueOf(createDto.getCategories()))
                            .accept(MediaType.MULTIPART_FORM_DATA)
            ).andExpect(status().isCreated());

            // then
            verify(postService).makeValidateNormalPost(any(), any());
        }
    }

    @Nested
    @DisplayName("인증 가능한 뉴스 게시글 생성 API [POST /api/posts/valid/news]")
    class makeValidateNewsTest {
        private final static String BASE_URL = "/api/posts/valid/news";

        @Test
        @DisplayName("인증 가능한 일반 게시글 생성에 성공한다.")
        public void successMakeValidateNews() throws Exception {
            // given
            PostNewsCreateDto createDto = getNewsCreateDto();

            // stub
            willDoNothing().given(postService).makeValidateNews(any(), any());

            // when
            mockMvc.perform(
                    multipart(HttpMethod.POST, BASE_URL)
                            .param("newsUrl", createDto.getNewsUrl())
                            .param("year", String.valueOf(createDto.getYear()))
                            .param("month", String.valueOf(createDto.getMonth()))
                            .param("day", String.valueOf(createDto.getDay()))
                            .param("categories", String.valueOf(createDto.getCategories()))
                            .accept(MediaType.MULTIPART_FORM_DATA)
            ).andExpect(status().isCreated());

            // then
            verify(postService).makeValidateNews(any(), any());
        }
    }

    @Nested
    @DisplayName("인증 불가능한 일반 게시글 생성 API [POST /api/posts/invalid/normal]")
    class makeNormalPostTest {
        private final static String BASE_URL = "/api/posts/invalid/normal";

        @Test
        @DisplayName("인증 가능한 일반 게시글 생성에 성공한다.")
        public void successMakeNormalPost() throws Exception {
            // given
            PostNormalCreateDto createDto = getNormalCreateDto();

            // stub
            willDoNothing().given(postService).makeNormalPost(any(), any());

            // when
            mockMvc.perform(
                    multipart(HttpMethod.POST, BASE_URL)
                            .file("files", createDto.getFiles().get(0).getBytes())
                            .file("files", createDto.getFiles().get(1).getBytes())
                            .file("files", createDto.getFiles().get(2).getBytes())
                            .param("content", createDto.getContent())
                            .param("year", String.valueOf(createDto.getYear()))
                            .param("month", String.valueOf(createDto.getMonth()))
                            .param("day", String.valueOf(createDto.getDay()))
                            .param("categories", String.valueOf(createDto.getCategories()))
                            .accept(MediaType.MULTIPART_FORM_DATA)
            ).andExpect(status().isCreated());

            // then
            verify(postService).makeNormalPost(any(), any());
        }
    }

    @Nested
    @DisplayName("게시글 인증 API [PUT /api/admin/posts/valid/{id}]")
    class checkPostValidationTest {
        private static final String BASE_URL = "/api/admin/posts/valid/{id}";
        private static final long postId = 1;

        @Test
        @DisplayName("게시글 인증에 성공한다.")
        public void successCheckPostValidation() throws Exception {
            // given

            // stub
            willDoNothing().given(postService).checkPostValidation(postId);

            // when
            mockMvc.perform(put(BASE_URL, postId))
                    .andExpect(status().isOk());

            // then
            verify(postService).checkPostValidation(postId);
        }
    }

    @Nested
    @DisplayName("일반 게시글 수정 API [PUT /api/posts/normal/{id}]")
    class editNormalPostTest {
        private final static String BASE_URL = "/api/posts/normal/{id}";
        private final static long postId = 1;

        @Test
        @DisplayName("일반 게시글 수정에 성공한다.")
        public void successEditNormalPost() throws Exception {
            // given
            PostEditDto editDto = getNormalEditDto();

            // stub
            willDoNothing().given(postService).editNormalPost(any(), any(), anyLong());

            // when
            mockMvc.perform(multipart(HttpMethod.PUT, BASE_URL, postId)
                            .file("files", editDto.getFiles().get(0).getBytes())
                            .file("files", editDto.getFiles().get(1).getBytes())
                            .file("files", editDto.getFiles().get(2).getBytes())
                            .param("content", editDto.getContent())
                            .param("year", String.valueOf(editDto.getYear()))
                            .param("month", String.valueOf(editDto.getMonth()))
                            .param("day", String.valueOf(editDto.getDay()))
                            .param("categories", String.valueOf(editDto.getCategories()))
                    ).andExpect(status().isOk());

            // then
            verify(postService).editNormalPost(any(), any(), anyLong());
        }
    }

    @Nested
    @DisplayName("뉴스 게시글 수정 API [PUT /api/posts/news/{id}]")
    class editNewsPostTest {
        private final static String BASE_URL = "/api/posts/news/{id}";
        private final static long postId = 1;

        @Test
        @DisplayName("뉴스 게시글 수정에 성공한다.")
        public void successEditNewsPost() throws Exception {
            // given
            PostNewsEditDto editDto = getNewsEditDto();

            // stub
            willDoNothing().given(postService).editNewsPost(any(), any(), anyLong());

            // when
            mockMvc.perform(multipart(HttpMethod.PUT, BASE_URL, postId)
                            .param("newsUrl", editDto.getNewsUrl())
                            .param("year", String.valueOf(editDto.getYear()))
                            .param("month", String.valueOf(editDto.getMonth()))
                            .param("day", String.valueOf(editDto.getDay()))
                            .param("categories", String.valueOf(editDto.getCategories()))
                    ).andExpect(status().isOk());

            // then
            verify(postService).editNewsPost(any(), any(), anyLong());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 API [DELETE /api/posts/{id}]")
    class deletePostTest {
        private final static String BASE_URL = "/api/posts/{id}";
        private final static long postId = 1;

        @Test
        @DisplayName("게시글 삭제에 성공한다.")
        public void successDeletePost() throws Exception {
            // given

            // stub
            willDoNothing().given(postService).deletePost(any(), anyLong());

            // when
            mockMvc.perform(delete(BASE_URL, postId)).andExpect(status().isOk());

            // then
            verify(postService).deletePost(any(), anyLong());
        }
    }

    private PostNormalCreateDto getNormalCreateDto() {
        PostFactory postFactory = PostFactory.POST_VERIFIABLE;
        List<MultipartFile> files = getFiles();
        List<String> categories = getCategories();

        return new PostNormalCreateDto(postFactory.getContent(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), files, categories);
    }

    private PostNewsCreateDto getNewsCreateDto() {
        PostFactory postFactory = PostFactory.POST_NEWS;
        List<String> categories = getCategories();

        return new PostNewsCreateDto(postFactory.getNewsUrl(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), categories);
    }

    private PostEditDto getNormalEditDto() {
        PostFactory postFactory = PostFactory.POST_VERIFIABLE;
        List<MultipartFile> files = getFiles();
        List<String> categories = getCategories();

        return new PostEditDto(postFactory.getContent(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), categories, files);
    }

    private PostNewsEditDto getNewsEditDto() {
        PostFactory postFactory = PostFactory.POST_NEWS;
        List<String> categories = getCategories();

        return new PostNewsEditDto(postFactory.getNewsUrl(), postFactory.getYear(),
                postFactory.getMonth(), postFactory.getDate(), categories);
    }

    private List<MultipartFile> getFiles() {
        List<MultipartFile> files = new ArrayList<>();

        files.add(new MockMultipartFile("IMAGE_1", "TEST1.jpeg", "multipart/form-data", "IMAGE_1".getBytes()));
        files.add(new MockMultipartFile("IMAGE_2", "TEST2.jpeg", "multipart/form-data", "IMAGE_2".getBytes()));
        files.add(new MockMultipartFile("IMAGE_3", "TEST3.jpeg", "multipart/form-data", "IMAGE_3".getBytes()));

        return files;
    }

    private List<String> getCategories() {
        List<String> categories = new ArrayList<>();

        categories.add(CATEGORY_VALUE_1.getCategoryName());
        categories.add(CATEGORY_VALUE_2.getCategoryName());

        return categories;
    }
}
