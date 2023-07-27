package com.dlrfdnwkd.atm.service;

import com.dlrfdnwkd.atm.dto.MemberResponse;
import com.dlrfdnwkd.atm.entity.Member;
import com.dlrfdnwkd.atm.enums.MemberErrorResult;
import com.dlrfdnwkd.atm.exception.MemberException;
import com.dlrfdnwkd.atm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public MemberResponse addMember(final String id, final String password, final String name) {
        final Member result = memberRepository.findById(id);
        if(result != null) {
            throw new MemberException(MemberErrorResult.DUPLICATED_MEMBER_REGISTER);
        }

        final Member member = Member.builder()
                .id(id)
                .password(password)
                .name(name)
                .build();

        final Member savedMember = memberRepository.save(member);

        return MemberResponse.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .build();
    }
}
