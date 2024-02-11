package umc.precending.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.precending.domain.cheer.Cheer;
import umc.precending.domain.member.Member;
import umc.precending.dto.cheer.CheerResponseDto;
import umc.precending.exception.cheer.CheerListEmptyException;
import umc.precending.exception.cheer.CheerMemberNotFoundException;
import umc.precending.exception.cheer.CheerNotFoundException;
import umc.precending.exception.member.MemberNotFoundException;
import umc.precending.exception.member.MemberRoleException;
import umc.precending.repository.cheer.CheerRepository;
import umc.precending.service.cheer.CheerService;
import umc.precending.service.member.MemberFindService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static umc.precending.domain.member.Authority.*;
import static umc.precending.factory.CheerFactory.*;
import static umc.precending.factory.MemberFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cheer [Service Layer] -> CheerService 테스트")
public class CheerServiceTest {
    @Mock
    private MemberFindService memberFindService;

    @Mock
    private CheerRepository cheerRepository;

    @InjectMocks
    private CheerService cheerService;

    @Nested
    @DisplayName("현재 응원 중인 기업 조회")
    class getCheeringCorporateTest {
        private static final Member personalMember = MEMBER_1.getPersonalMemberInstance();
        private static final Member corporateMember = MEMBER_1.getCorporateMemberInstance();

        @Test
        @DisplayName("현재 사용자가 개인 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidRole() {
            // given

            // stub

            // when - then
            assertThatThrownBy(() -> cheerService.getCheeringCorporate(corporateMember))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("조건에 부합하는 데이터가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<String> corporates = new ArrayList<>();

            // stub
            given(cheerRepository.findCheeringMemberList(personalMember, ROLE_CORPORATE))
                    .willReturn(corporates);

            // when - then
            assertThatThrownBy(() -> cheerService.getCheeringCorporate(personalMember))
                    .isInstanceOf(CheerMemberNotFoundException.class);
        }

        @Test
        @DisplayName("현재 응원 중인 기업을 조회하는 데에 성공한다.")
        public void successGetCheeringCorporate() {
            // given
            List<String> corporates = getResponseList();

            // stub
            given(cheerRepository.findCheeringMemberList(personalMember, ROLE_CORPORATE))
                    .willReturn(corporates);

            // when
            cheerService.getCheeringCorporate(personalMember);

            // then
            verify(cheerRepository).findCheeringMemberList(personalMember, ROLE_CORPORATE);
        }
    }

    @Nested
    @DisplayName("현재 응원 중인 동아리 조회")
    class getCheeringClubTest {
        private static final Member personalMember = MEMBER_1.getPersonalMemberInstance();
        private static final Member clubMember = MEMBER_1.getClubMemberInstance();

        @Test
        @DisplayName("현재 사용자가 개인 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidRole() {
            // given

            // stub

            // when - then
            assertThatThrownBy(() -> cheerService.getCheeringClub(clubMember))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("조건에 부합하는 데이터가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<String> clubs = new ArrayList<>();

            // stub
            given(cheerRepository.findCheeringMemberList(personalMember, ROLE_CLUB))
                    .willReturn(clubs);

            // when - then
            assertThatThrownBy(() -> cheerService.getCheeringClub(personalMember))
                    .isInstanceOf(CheerMemberNotFoundException.class);
        }

        @Test
        @DisplayName("현재 응원 중인 동아리를 조회하는 데에 성공한다.")
        public void successGetCheeringClub() {
            // given
            List<String> clubs = getResponseList();

            // stub
            given(cheerRepository.findCheeringMemberList(personalMember, ROLE_CLUB))
                    .willReturn(clubs);

            // when
            cheerService.getCheeringClub(personalMember);

            // then
            verify(cheerRepository).findCheeringMemberList(personalMember, ROLE_CLUB);
        }
    }

    @Nested
    @DisplayName("가장 많은 응원을 받고 있는 기업 조회")
    class getMostCorporateMemberTest {
        @Test
        @DisplayName("조건에 부합하는 데이터가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<CheerResponseDto> responseData = new ArrayList<>();

            // stub
            given(cheerRepository.findTopCheeringMember(ROLE_CORPORATE))
                    .willReturn(responseData);

            // when - then
            assertThatThrownBy(() -> cheerService.getMostCorporateMember(any()))
                    .isInstanceOf(CheerListEmptyException.class);
        }

        @Test
        @DisplayName("가장 많은 응원을 받고 있는 기업을 조회하는 데에 성공한다.")
        public void successGetMostCorporateMember() {
            // given
            List<CheerResponseDto> responseData = getCheerResponseList();

            // stub
            given(cheerRepository.findTopCheeringMember(ROLE_CORPORATE))
                    .willReturn(responseData);

            // when
            cheerService.getMostCorporateMember(any());

            // then
            verify(cheerRepository).findTopCheeringMember(ROLE_CORPORATE);
        }
    }

    @Nested
    @DisplayName("가장 많은 응원을 받고 있는 동아리 조회")
    class getMostClubMemberTest {
        @Test
        @DisplayName("조건에 부합하는 데이터가 존재하지 않는 경우, 오류를 반환한다.")
        public void throwExceptionByEmptyList() {
            // given
            List<CheerResponseDto> responseData = new ArrayList<>();

            // stub
            given(cheerRepository.findTopCheeringMember(ROLE_CLUB))
                    .willReturn(responseData);

            // when - then
            assertThatThrownBy(() -> cheerService.getMostClubMember(any()))
                    .isInstanceOf(CheerListEmptyException.class);
        }

        @Test
        @DisplayName("가장 많은 응원을 받고 있는 동아리를 조회하는 데에 성공한다.")
        public void successGetMostClubMember() {
            // given
            List<CheerResponseDto> responseData = getCheerResponseList();

            // stub
            given(cheerRepository.findTopCheeringMember(ROLE_CLUB))
                    .willReturn(responseData);

            // when
            cheerService.getMostClubMember(any());

            // then
            verify(cheerRepository).findTopCheeringMember(ROLE_CLUB);
        }
    }

    @Nested
    @DisplayName("기업 회원 응원")
    class cheerCorporateMemberTest {
        private final static Member personalMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member corporateMember = MEMBER_1.getCorporateMemberInstance();
        private final static long corporateId = 1;
        private final static long errorId = -1;

        @Test
        @DisplayName("현재 사용자가 개인 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidRole() {
            // given

            // stub

            // when - then
            assertThatThrownBy(() -> cheerService.cheerCorporateMember(corporateMember, corporateId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("유효하지 않은 PK로 요청하였을 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            willThrow(new MemberNotFoundException())
                    .given(memberFindService)
                    .findMemberById(errorId);

            // when - then
            assertThatThrownBy(() -> cheerService.cheerCorporateMember(personalMember, errorId));
        }

        @Test
        @DisplayName("응원하는 대상이 기업 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByOrganizationRole() {
            // given

            // stub
            given(memberFindService.findMemberById(corporateId)).willReturn(personalMember);

            // when - then
            assertThatThrownBy(() -> cheerService.cheerCorporateMember(personalMember, corporateId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("기업 회원을 응원하는데에 성공한다.")
        public void successCheerCorporateMember() {
            // given

            // stub
            given(memberFindService.findMemberById(corporateId)).willReturn(corporateMember);

            // when
            cheerService.cheerCorporateMember(personalMember, corporateId);

            // then
            verify(cheerRepository).save(any());
        }
    }

    @Nested
    @DisplayName("동아리 회원 응원")
    class cheerClubMemberTest {
        private final static Member personalMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member clubMember = MEMBER_1.getClubMemberInstance();
        private final static long clubId = 1;
        private final static long errorId = -1;

        @Test
        @DisplayName("현재 사용자가 개인 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidRole() {
            // given

            // stub

            // when - then
            assertThatThrownBy(() -> cheerService.cheerCorporateMember(clubMember, clubId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("유효하지 않은 PK로 요청하였을 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            willThrow(new MemberNotFoundException())
                    .given(memberFindService)
                    .findMemberById(errorId);

            // when - then
            assertThatThrownBy(() -> cheerService.cheerClubMember(personalMember, errorId));
        }

        @Test
        @DisplayName("응원하는 대상이 동아리 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByOrganizationRole() {
            // given

            // stub
            given(memberFindService.findMemberById(clubId)).willReturn(personalMember);

            // when - then
            assertThatThrownBy(() -> cheerService.cheerClubMember(personalMember, clubId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("동아리 회원을 응원하는데에 성공한다.")
        public void successCheerClubMember() {
            // given

            // stub
            given(memberFindService.findMemberById(clubId)).willReturn(clubMember);

            // when
            cheerService.cheerClubMember(personalMember, clubId);

            // then
            verify(cheerRepository).save(any());
        }
    }

    @Nested
    @DisplayName("기업 응원 취소")
    class disposeCorporateCheerTest {
        private final static Member personalMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member corporateMember = MEMBER_2.getCorporateMemberInstance();
        private final static long cheerId = 1;
        private final static long errorId = -1;

        @Test
        @DisplayName("현재 사용자가 개인 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidRole() {
            // given

            // stub

            // when - then
            assertThatThrownBy(() -> cheerService.disposeCorporateCheer(corporateMember, cheerId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("유효하지 않은 PK로 요청하였을 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            willThrow(new CheerNotFoundException())
                    .given(cheerRepository)
                    .findById(errorId);

            // when - then
            assertThatThrownBy(() -> cheerService.disposeCorporateCheer(personalMember, errorId))
                    .isInstanceOf(CheerNotFoundException.class);
        }

        @Test
        @DisplayName("사용자 정보와 응원 정보가 일치하지 않을 경우 오류를 반환한다.")
        public void throwExceptionByInvalidMember() {
            // given
            Cheer cheer = getCheerInstance(corporateMember, corporateMember);

            // stub
            given(cheerRepository.findById(cheerId)).willReturn(Optional.of(cheer));

            // when
            assertThatThrownBy(() -> cheerService.disposeCorporateCheer(personalMember, cheerId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("단체 정보와 응원 정보가 일치하지 않을 경우 오류를 반환한다.")
        public void throwExceptionByOrganizationRole() {
            // given
            Cheer cheer = getCheerInstance(personalMember, personalMember);

            // stub
            given(cheerRepository.findById(cheerId)).willReturn(Optional.of(cheer));

            // when - then
            assertThatThrownBy(() -> cheerService.disposeCorporateCheer(personalMember, cheerId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("기업 응원 취소에 성공한다.")
        public void successDisposeCorporateCheer() {
            // given
            Cheer cheer = getCheerInstance(personalMember, corporateMember);

            // stub
            given(cheerRepository.findById(cheerId)).willReturn(Optional.of(cheer));

            // when
            cheerService.disposeCorporateCheer(personalMember, cheerId);

            // then
            verify(cheerRepository).delete(any());
        }
    }

    @Nested
    @DisplayName("동아리 응원 취소")
    class disposeClubCheerTest {
        private final static Member personalMember = MEMBER_1.getPersonalMemberInstance();
        private final static Member clubMember = MEMBER_2.getClubMemberInstance();
        private final static long cheerId = 1;
        private final static long errorId = -1;

        @Test
        @DisplayName("현재 사용자가 개인 회원이 아닌 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidRole() {
            // given

            // stub

            // when - then
            assertThatThrownBy(() -> cheerService.disposeCorporateCheer(clubMember, cheerId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("유효하지 않은 PK로 요청하였을 경우, 오류를 반환한다.")
        public void throwExceptionByInvalidId() {
            // given

            // stub
            willThrow(new CheerNotFoundException())
                    .given(cheerRepository)
                    .findById(errorId);

            // when - then
            assertThatThrownBy(() -> cheerService.disposeClubCheer(personalMember, errorId))
                    .isInstanceOf(CheerNotFoundException.class);
        }

        @Test
        @DisplayName("사용자 정보와 응원 정보가 일치하지 않을 경우 오류를 반환한다.")
        public void throwExceptionByInvalidMember() {
            // given
            Cheer cheer = getCheerInstance(clubMember, clubMember);

            // stub
            given(cheerRepository.findById(cheerId)).willReturn(Optional.of(cheer));

            // when
            assertThatThrownBy(() -> cheerService.disposeClubCheer(personalMember, cheerId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("단체 정보와 응원 정보가 일치하지 않을 경우 오류를 반환한다.")
        public void throwExceptionByOrganizationRole() {
            // given
            Cheer cheer = getCheerInstance(personalMember, personalMember);

            // stub
            given(cheerRepository.findById(cheerId)).willReturn(Optional.of(cheer));

            // when - then
            assertThatThrownBy(() -> cheerService.disposeClubCheer(personalMember, cheerId))
                    .isInstanceOf(MemberRoleException.class);
        }

        @Test
        @DisplayName("동아리 응원 취소에 성공한다.")
        public void successDisposeClubCheer() {
            // given
            Cheer cheer = getCheerInstance(personalMember, clubMember);

            // stub
            given(cheerRepository.findById(cheerId)).willReturn(Optional.of(cheer));

            // when
            cheerService.disposeClubCheer(personalMember, cheerId);

            // then
            verify(cheerRepository).delete(any());
        }
    }

    private List<String> getResponseList() {
        List<String> responseData = new ArrayList<>();

        responseData.add("TEST_1");
        responseData.add("TEST_2");
        responseData.add("TEST_3");

        return responseData;
    }

    private List<CheerResponseDto> getCheerResponseList() {
        List<CheerResponseDto> responseData = new ArrayList<>();

        responseData.add(new CheerResponseDto("NAME_1", 10));
        responseData.add(new CheerResponseDto("NAME_2", 20));
        responseData.add(new CheerResponseDto("NAME_3", 30));

        return responseData;
    }
}