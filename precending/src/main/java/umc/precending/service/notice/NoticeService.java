package umc.precending.service.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.precending.domain.notice.Notice;
import umc.precending.dto.notice.NoticeDetailedResponseDto;
import umc.precending.dto.notice.NoticeRequestDto;
import umc.precending.dto.notice.NoticeResponseDto;
import umc.precending.exception.notice.NoticeNotFoundException;
import umc.precending.repository.notice.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getNotices() {
        List<Notice> responseList = noticeRepository.findAll();

        if(responseList.isEmpty()) throw new NoticeNotFoundException();

        return responseList.stream().map(NoticeResponseDto::toDto).toList();
    }

    @Transactional(readOnly = true)
    public NoticeDetailedResponseDto getNotice(Long noticeId) {
        Notice findNotice = findNoticeById(noticeId);
        findNotice.updateViewCount();

        return new NoticeDetailedResponseDto(findNotice);
    }

    @Transactional
    public void createNotice(NoticeRequestDto requestDto) {
        Notice notice = getNoticeInstance(requestDto.getTitle(), requestDto.getContent());

        noticeRepository.save(notice);
    }

    @Transactional
    public void editNotice(NoticeRequestDto requestDto, Long noticeId) {
        Notice findNotice = findNoticeById(noticeId);

        findNotice.editNotice(requestDto.getTitle(), requestDto.getContent());
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice findNotice = findNoticeById(noticeId);

        noticeRepository.delete(findNotice);
    }

    private Notice findNoticeById(Long id) {
        return noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
    }

    private Notice getNoticeInstance(String title, String content) {
        return new Notice(title, content);
    }
}
