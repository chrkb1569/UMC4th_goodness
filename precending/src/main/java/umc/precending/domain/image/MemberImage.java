package umc.precending.domain.image;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;
import umc.precending.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "memberImage")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage extends Image {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public MemberImage(MultipartFile file) {
        this.originalName = file.getOriginalFilename();
        this.storedName = generateStoreName(originalName);
        this.accessUrl = "";
    }

    // Member에 대한 참조를 설정하는 메서드
    public void initMember(Member member) {
        if(this.member == null) this.member = member;
    }
}
