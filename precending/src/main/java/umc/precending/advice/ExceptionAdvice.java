package umc.precending.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.precending.exception.answer.AnswerNotFoundException;
import umc.precending.exception.answer.AnswerNotMatchedWriterException;
import umc.precending.exception.category.CategoryOutOfRangeException;
import umc.precending.exception.cheer.CheerListEmptyException;
import umc.precending.exception.cheer.CheerMemberNotFoundException;
import umc.precending.exception.cheer.CheerNotFoundException;
import umc.precending.exception.member.MemberRoleException;
import umc.precending.exception.category.CategoryNotFoundException;
import umc.precending.exception.image.ImageNotSupportedException;
import umc.precending.exception.member.MemberDuplicateException;
import umc.precending.exception.member.MemberLoginFailureException;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.exception.notice.NoticeNotFoundException;
import umc.precending.exception.post.PostAuthException;
import umc.precending.exception.post.PostNewsNotSupportedException;
import umc.precending.exception.post.PostNotFoundException;
import umc.precending.exception.post.PostVerifyException;
import umc.precending.exception.question.QuestionNotFoundException;
import umc.precending.exception.question.QuestionNotMatchedWriterException;
import umc.precending.exception.token.TokenNotCorrectException;
import umc.precending.response.Response;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return Response.failure(404, "조건에 부합하는 회원을 찾지 못하였습니다.");
    }

    @ExceptionHandler(MemberDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberDuplicateException() {
        return Response.failure(409, "입력한 정보로 가입된 사용자가 존재합니다.");
    }

    @ExceptionHandler(MemberLoginFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response memberLoginFailureException() {
        return Response.failure(400, "아이디 혹은 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
    }

    @ExceptionHandler(MemberRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response memberRoleException(MemberRoleException e) {
        return Response.failure(400, e.getMessage());
    }

    @ExceptionHandler(TokenNotCorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response tokenNotCorrectException() {
        return Response.failure(400, "저장된 토큰의 정보와 일치하지 않습니다.");
    }

    @ExceptionHandler(ImageNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response imageNotSupportedException(ImageNotSupportedException e) {
        return Response.failure(400, e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return Response.failure(404, "카테고리를 찾을 수 없습니다.");
    }

    @ExceptionHandler(CategoryOutOfRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response categoryOutOfRangeException() {
        return Response.failure(400, "입력할 수 있는 카테고리는 최소 1개, 최대 2개입니다.");
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response postNotFoundException() {
        return Response.failure(404, "조건에 부합하는 게시글을 찾지 못하였습니다.");
    }

    @ExceptionHandler(PostAuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response postAuthException() {
        return Response.failure(400, "사용자가 게시글 작성자가 일치하지 않습니다.");
    }

    @ExceptionHandler(PostVerifyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response postVerifyException() {
        return Response.failure(400, "인증이 불가능한 게시글이거나, 이미 인증된 게시글입니다.");
    }

    @ExceptionHandler(PostNewsNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response postNewsNotSupportedException() {
        return Response.failure(400, "회원의 정보를 다시 한 번 확인해주세요.");
    }

    @ExceptionHandler(CheerMemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cheerMemberNotFoundException() {
        return Response.failure(404, "현재 사용자가 응원하고 있는 대상이 존재하지 않습니다.");
    }

    @ExceptionHandler(CheerListEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cheerListEmptyException() {
        return Response.failure(404, "현재 조건에 부합하는 대상이 존재하지 않습니다.");
    }

    @ExceptionHandler(CheerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response cheerNotFoundException() {
        return Response.failure(404, "PK값과 일치하는 데이터가 존재하지 않습니다.");
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response noticeNotFoundException() {
        return Response.failure(404, "공지 사항을 찾을 수 없습니다.");
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response questionNotFoundException() {
        return Response.failure(404, "문의 사항을 찾을 수 없습니다.");
    }

    @ExceptionHandler(QuestionNotMatchedWriterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response questionNotMatchedWriterException() {
        return Response.failure(400, "문의 사항의 작성자와 현재 사용자의 정보가 일치하지 않습니다.");
    }

    @ExceptionHandler(AnswerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response answerNotFoundException() {
        return Response.failure(404, "일치하는 답변을 찾을 수 없습니다.");
    }

    @ExceptionHandler(AnswerNotMatchedWriterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response answerNotMatchedWriterException() {
        return Response.failure(400, "답변 작성자와 현재 사용자의 정보가 일치하지 않습니다.");
    }
}