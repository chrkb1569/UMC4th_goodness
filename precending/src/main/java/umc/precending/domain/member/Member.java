package umc.precending.domain.member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.base.BaseEntity;
import umc.precending.domain.image.MemberImage;

import java.util.ArrayList;
import java.util.List;

import static umc.precending.util.MessageProvider.MESSAGE_DEFAULT_INTRODUCTION;

@Entity
@Getter
@Table(name = "MEMBER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Member extends BaseEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "name", nullable = false)
    protected String name; // 개인, 동아리, 기업의 이름

    @Column(name = "username", nullable = false)
    protected String username; // 사용자의 아이디

    @Column(name = "password", nullable = false)
    protected String password; // 비밀번호

    @Column(name = "email", nullable = false, unique = true)
    protected String email; // 이메일

    @Lob
    @Column(name = "introduction", nullable = false)
    protected String introduction; // 프로필 화면에서 노출시킬 소개글

    @Enumerated(value = EnumType.STRING)
    protected Authority authority; // 사용자가 어떠한 회원인지를 명시(ex) 개인 회원, 동아리 회원, 기업 회원 등)

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    protected List<MemberImage> images = new ArrayList<>();

    protected Member(String name, String password, String email) {
        this.name = name;
        this.username = email;
        this.password = password;
        this.email = email;
        this.introduction = MESSAGE_DEFAULT_INTRODUCTION.getMessage();
    }

    public void editImage(List<MemberImage> images) {
        this.images.clear();
        saveImage(images);
    }

    public void saveImage(List<MemberImage> images) {
        for(MemberImage image : images) {
            image.initMember(this);
            this.images.add(image);
        }
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void editName(String name) {
        this.name = name;
    }

    public void editIntroduction(String introduction) {
        this.introduction = introduction;
    }
}