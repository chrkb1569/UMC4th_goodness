package umc.precending.domain.member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.precending.domain.cheer.Cheer;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Person extends Member {
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "personalMember", orphanRemoval = true)
    private List<Cheer> cheerList = new ArrayList<>();

    public Person(String name, String password, String email) {
        super(name, password, email);
        this.authority = Authority.ROLE_PERSON;
    }
}