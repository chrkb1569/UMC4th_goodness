package umc.precending.domain.cheer;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import umc.precending.domain.base.BaseEntity;
import umc.precending.domain.member.Member;

@Entity
@Getter
@Table(name = "CHEER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cheer extends BaseEntity {
    @Id
    @Column(name = "cheer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_member")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member personalMember;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_member")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member organizationMember;

    public Cheer(Member personalMember, Member organizationMember) {
        this.personalMember = personalMember;
        this.organizationMember = organizationMember;
    }
}