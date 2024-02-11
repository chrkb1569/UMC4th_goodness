package umc.precending.factory;

import umc.precending.domain.cheer.Cheer;
import umc.precending.domain.member.Member;

public class CheerFactory {
    public static Cheer getCheerInstance(Member personalMember, Member organizationMember) {
        return new Cheer(personalMember, organizationMember);
    }
}
