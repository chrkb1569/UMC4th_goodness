package umc.precending.factory;

import umc.precending.domain.member.*;

public enum MemberFactory {
    MEMBER_1("MEMBER_1", "password_1", "test1@test.com", "123-45-6789", "test", "test"),
    MEMBER_2("MEMBER_1", "password_2", "test2@test.com", "234-56-7890", "test", "test"),
    MEMBER_3("MEMBER_1", "password_3", "test3@test.com", "345-67-8910", "test", "test")
    ;

    private final String name;
    private final String password;
    private final String email;
    private final String registrationNumber;
    private final String school;
    private final String address;

    MemberFactory(String name, String password, String email, String registrationNumber, String school, String address) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.school = school;
        this.address = address;
    }

    public Member getPersonalMemberInstance() {
        return new Person(name, password, email);
    }

    public Member getCorporateMemberInstance() {
        return new Corporate(name, password, email, registrationNumber);
    }

    public Member getClubMemberInstance() {
        return new Club(name, password, email, school, address);
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public String getSchool() {
        return this.school;
    }

    public String getAddress() {
        return this.address;
    }
}