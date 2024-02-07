package umc.precending.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import umc.precending.domain.image.MemberImage;
import umc.precending.domain.member.*;
import umc.precending.factory.MemberFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static umc.precending.factory.ImageFactory.*;
import static umc.precending.factory.MemberFactory.*;

@DisplayName("Member 도메인 테스트")
public class MemberTest {
    private Member member;

    @Nested
    @DisplayName("Member Domain 객체를 생성한다.")
    class getMemberInstance {
        private final int INITIAL_SCORE = 0;

        @Test
        @DisplayName("개인 회원을 생성한다.")
        public void createPersonInstance() {
            // given
            MemberFactory memberFactory = MEMBER_1;

            // when
            member = memberFactory.getPersonalMemberInstance();

            // then
            assertAll(
                    () -> assertThat(member instanceof Person).isTrue(),
                    () -> assertThat(member.getName()).isEqualTo(memberFactory.getName()),
                    () -> assertThat(member.getPassword()).isEqualTo(memberFactory.getPassword()),
                    () -> assertThat(member.getEmail()).isEqualTo(memberFactory.getEmail()),
                    () -> assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_PERSON),
                    () -> assertThat(((Person)member).getCheerList()).isNotNull()
            );
        }

        @Test
        @DisplayName("기업 회원을 생성한다.")
        public void createCorporateInstance() {
            // given
            MemberFactory memberFactory = MEMBER_1;

            // when
            member = memberFactory.getCorporateMemberInstance();

            // then
            assertAll(
                    () -> assertThat(member instanceof Corporate).isTrue(),
                    () -> assertThat(member.getName()).isEqualTo(memberFactory.getName()),
                    () -> assertThat(member.getPassword()).isEqualTo(memberFactory.getPassword()),
                    () -> assertThat(member.getEmail()).isEqualTo(memberFactory.getEmail()),
                    () -> assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_CORPORATE),
                    () -> assertThat(((Corporate)member).getRegistrationNumber()).isEqualTo(memberFactory.getRegistrationNumber()),
                    () -> assertThat(((Corporate)member).getScore()).isEqualTo(INITIAL_SCORE)
            );
        }

        @Test
        @DisplayName("동아리 회원을 생성한다.")
        public void createClubInstance() {
            // given
            MemberFactory memberFactory = MEMBER_1;

            // when
            member = memberFactory.getClubMemberInstance();

            // then
            assertAll(
                    () -> assertThat(member instanceof Club).isTrue(),
                    () -> assertThat(member.getName()).isEqualTo(memberFactory.getName()),
                    () -> assertThat(member.getPassword()).isEqualTo(memberFactory.getPassword()),
                    () -> assertThat(member.getEmail()).isEqualTo(memberFactory.getEmail()),
                    () -> assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_CLUB),
                    () -> assertThat(((Club)member).getSchool()).isEqualTo(memberFactory.getSchool()),
                    () -> assertThat(((Club)member).getAddress()).isEqualTo(memberFactory.getAddress()),
                    () -> assertThat(((Club)member).getScore()).isEqualTo(INITIAL_SCORE)
            );
        }
    }

    @Test
    @DisplayName("saveImage() 메서드를 테스트한다.")
    public void saveImageTest() {
        // given
        List<MemberImage> images = new ArrayList<>();

        MemberImage memberImage1 = IMAGE_1.getMemberImage();
        MemberImage memberImage2 = IMAGE_2.getMemberImage();
        MemberImage memberImage3 = IMAGE_3.getMemberImage();

        images.add(memberImage1);
        images.add(memberImage2);
        images.add(memberImage3);

        member = MEMBER_1.getPersonalMemberInstance();

        // when
        member.saveImage(images);

        // then
        assertThat(member.getImages().size()).isEqualTo(images.size());
    }
}