package umc.precending.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.domain.category.Category;
import umc.precending.domain.post.NewsPost;
import umc.precending.domain.post.Post;
import umc.precending.domain.image.PostImage;
import umc.precending.domain.member.Member;
import umc.precending.dto.post.*;
import umc.precending.exception.category.CategoryOutOfRangeException;
import umc.precending.exception.post.PostAuthException;
import umc.precending.exception.post.PostNotFoundException;
import umc.precending.exception.post.PostVerifyException;
import umc.precending.repository.post.PostRepository;
import umc.precending.service.crawling.CrawlingService;
import umc.precending.service.image.ImageService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static umc.precending.domain.category.Category.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CrawlingService crawlingService;
    private final PostRepository postRepository;
    private final ImageService imageService;

    // 사용자가 작성한 전체 게시글 조회 - 선행 연월 기준
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll(int year, int month, Member member) {
        List<PostResponseDto> responseData = postRepository.findPostsByPrecedingDate(member, year, month);
        if(responseData.isEmpty()) throw new PostNotFoundException();

        return responseData;
    }

    // 단일 게시글 보기
    @Transactional(readOnly = true)
    public PostResponseDto findOne(Long id) {
        Post findPost = getPost(id);

        return getPostResponseData(findPost);
    }

    // 기업 회원이 작성한 게시글 조회 - 최신순
    @Transactional(readOnly = true)
    public List<PostResponseDto> getCorporatePost() {
        List<PostResponseDto> corporatePosts = postRepository.findPostsByCorporate();
        if(corporatePosts.isEmpty()) throw new PostNotFoundException();

        return corporatePosts;
    }

    // 동아리 회원이 작성한 게시글 조회 - 최신순
    @Transactional(readOnly = true)
    public List<PostResponseDto> getClubPost() {
        List<PostResponseDto> clubPosts = postRepository.findPostsByClub();
        if(clubPosts.isEmpty()) throw new PostNotFoundException();

        return clubPosts;
    }

    // 인증 가능한 게시글만을 조회하는 로직
    @Transactional
    public List<PostResponseDto> findVerifiablePost() {
        List<PostResponseDto> postList = postRepository.findVerifiablePosts();
        if(postList.isEmpty()) throw new PostNotFoundException();

        return postList;
    }

    // 인증이 가능한 선행 기록 - 텍스트 + 사진
    @Transactional
    public void makeValidateNormalPost(PostNormalCreateDto createDto, Member member) {
        List<PostImage> images = createDto.getFiles().stream().map(PostImage::new).toList();
        List<Category> categories = getCategories(createDto.getCategories());

        Post post = getValidateNormalPost(createDto, member, categories, images);

        addImages(createDto.getFiles(), images);

        postRepository.save(post);
    }

    // 인증이 가능한 선행 기록 - 뉴스
    @Transactional
    public void makeValidateNews(PostNewsCreateDto createDto, Member member) {
        PostCrawlingResponseDto responseDto = crawlingService.crawlingData(createDto.getNewsUrl());
        List<Category> categories = getCategories(createDto.getCategories());
        List<PostImage> images = getPostImages(responseDto);

        Post post = getNewsPost(createDto, responseDto, member, categories, images);

        postRepository.save(post);
    }

    // 인증이 불가능한 선행 기록 - 텍스트 + 사진
    @Transactional
    public void makeNormalPost(PostNormalCreateDto createDto, Member member) {
        List<PostImage> images = createDto.getFiles().stream().map(PostImage::new).toList();
        List<Category> categories = getCategories(createDto.getCategories());

        Post post = getNormalPost(createDto, member, categories, images);
        addImages(createDto.getFiles(), images);

        postRepository.save(post);
    }

    // 인증 가능한 게시글을 관리자가 판단하여 인증여부를 결정하는 로직
    @Transactional
    public void checkPostValidation(Long id) {
        Post findPost = getPost(id);
        checkValidation(findPost);
        findPost.verifyPost();
    }

    // 뉴스 게시글 수정
    @Transactional
    public void editNewsPost(PostNewsEditDto editDto, Member member, Long id) {
        Post findPost = getPost(id, member);

        editNewsPost((NewsPost) findPost, editDto);
    }

    // 일반 게시글 수정
    @Transactional
    public void editNormalPost(PostEditDto editDto, Member member, Long id) {
        Post findPost = getPost(id, member);

        editPost(findPost, editDto);
    }

    // 선행 게시글 삭제
    @Transactional
    public void deletePost(Member member, Long id) {
        Post findPost = getPost(id, member);
        postRepository.delete(findPost);
    }

    // 조회한 게시글 타입에 따라서 반환
    private PostResponseDto getPostResponseData(Post post) {
        if(post instanceof NewsPost) return new PostNewsResponseDto(post);
        return new PostResponseDto(post);
    }

    // 사용자로부터 입력받은 String 형식의 카테고리를 Category 객체로 변환
    private List<Category> getCategories(List<String> categories) {
        if(categories.size() > MAX_RANGE || categories.size() < MIN_RANGE) throw new CategoryOutOfRangeException();

        return categories.stream()
                .map(Category::getCategoryByName)
                .collect(Collectors.toList());
    }

    // 사용자에게 입력 받은 값을 바탕으로 인증 가능한 일반 게시글을 생성
    private Post getValidateNormalPost(PostNormalCreateDto createDto, Member member,
                                       List<Category> categories, List<PostImage> images) {
        return new Post(createDto.getYear(), createDto.getMonth(), createDto.getDay(), createDto.getContent(),
                member.getUsername(), true, categories, images);
    }

    // 연관 관계를 맺는 메서드
    private void addImages(List<MultipartFile> files, List<PostImage> images) {
        IntStream.range(0, images.size()).forEach(index -> {
            MultipartFile file = files.get(index);
            PostImage image = images.get(index);
            String accessUrl = imageService.saveImage(file, image.getStoredName());
            image.setAccessUrl(accessUrl);
        });
    }

    private List<PostImage> getPostImages(PostCrawlingResponseDto responseDto) {
        return List.of(new PostImage(responseDto.getAccessUrl() + ".jpeg", responseDto.getAccessUrl()));
    }

    private Post getNewsPost(PostNewsCreateDto createDto, PostCrawlingResponseDto responseDto,
                             Member member, List<Category> categories, List<PostImage> images) {
        return new NewsPost(member.getUsername(), createDto.getNewsUrl(), responseDto.getContent(),
                createDto.getYear(), createDto.getMonth(), createDto.getDay(), categories, images);
    }

    private Post getNormalPost(PostNormalCreateDto createDto, Member member,
                               List<Category> categories, List<PostImage> images) {
        return new Post(createDto.getYear(), createDto.getMonth(), createDto.getDay(), createDto.getContent(),
                member.getUsername(), false, categories, images);
    }

    private Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    private Post getPost(Long id, Member member) {
        Post findPost = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if(!checkValidation(member, findPost)) throw new PostAuthException();

        return findPost;
    }

    private boolean checkValidation(Member member, Post post) {
        return member.getUsername().equals(post.getWriter());
    }

    private void checkValidation(Post post) {
        if(!post.isVerifiable() || post.isVerified()) throw new PostVerifyException();
    }

    private void editPost(Post post, PostEditDto editDto) {
        List<MultipartFile> files = editDto.getFiles();
        List<Category> categories = getCategories(editDto.getCategories());

        if(files.isEmpty()) {
            List<PostImage> images = editDto.getFiles().stream().map(PostImage::new).toList();
            post.editInfo(editDto.getYear(), editDto.getMonth(), editDto.getDay(), editDto.getContent(), categories, images);
            return;
        }

        post.editInfo(editDto.getYear(), editDto.getMonth(), editDto.getDay(), editDto.getContent(), categories);
    }

    private void editNewsPost(NewsPost post, PostNewsEditDto editDto) {
        List<Category> categories = getCategories(editDto.getCategories());

        if(editDto.getNewsUrl().equals(post.getNewsUrl())) {
            post.editPost(editDto.getYear(), editDto.getMonth(), editDto.getDay(), categories);
            return;
        }

        PostCrawlingResponseDto responseDto = crawlingService.crawlingData(editDto.getNewsUrl());

        post.editPost(editDto.getYear(), editDto.getMonth(), editDto.getDay(),
                responseDto.getAccessUrl(), responseDto.getContent(), categories);
    }
}
