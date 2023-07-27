package com.dlrfdnwkd.atm.reppository;

import com.dlrfdnwkd.atm.entity.Member;
import com.dlrfdnwkd.atm.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void registerMember() {
        //given
        final Member member = Member.builder()
                .id("id")
                .password("9512")
                .name("홍길동")
                .build();

        //when
        final Member result = memberRepository.save(member);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPassword()).isNotNull();
        assertThat(result.getName()).isNotNull();
    }
}
