package umc.precending.domain.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Person extends Member{
    @Column(name = "phone", nullable = false, unique = true)
    private String phone; // 개인 회원의 핸드폰 번호

    @Column(name = "phoneValidation", nullable = false)
    private boolean phoneValidation; // 개인 회원의 핸드폰 인증 여부

    //내가 추가한 것
    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private List<Person_Corporate> personCorporates=new ArrayList<>();

    //내가 추가한 것:연관관계 편의 메서드
    public void addMyCorporate(Corporate corporate){
        Person_Corporate personCorporate=Person_Corporate.createPerson_Corporate(corporate,5);
        this.personCorporates.add(personCorporate);
        personCorporate.setPerson(this);
    }
    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private List<Person_Club> personClubs=new ArrayList<>();

    //내가 추가한 것:연관관계 편의 메서드
    public void addMyClub(Club club){
        Person_Club personClub=Person_Club.createPerson_Club(club,5);
        this.personClubs.add(personClub);
        personClub.setPerson(this);
    }


    public Person(String name, String birth, String password,
                  String email, String phone) {
        this.name = name;
        this.username = email;
        this.birth = birth;
        this.password = password;
        this.email = email;
        this.emailValidation = false;
        this.introduction = "";
        this.phone = phone;
        this.phoneValidation = false;
        this.authority = Authority.ROLE_PERSON;
    }
}
