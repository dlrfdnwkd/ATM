package com.dlrfdnwkd.atm.service;

import com.dlrfdnwkd.atm.dto.MemberResponse;
import com.dlrfdnwkd.atm.entity.Member;
import com.dlrfdnwkd.atm.enums.MemberErrorResult;
import com.dlrfdnwkd.atm.exception.MemberException;
import com.dlrfdnwkd.atm.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private final String id = "id";
    private final String password = "9512";
    private final String name = "홍길동";

    @InjectMocks
    private MemberService target;
    @Mock
    private MemberRepository memberRepository;

    @Test
    public void 멤버등록실패_이미존재함() {
        //given
        doReturn(Member.builder().build()).when(memberRepository).findById(id);

        //when
        final MemberException result = assertThrows(MemberException.class, () -> target.addMember(id,password,name));

        //then
        assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.DUPLICATED_MEMBER_REGISTER);
    }
    @Test
    public void 멤버등록성공() {
        //given
        doReturn(null).when(memberRepository).findById(id);
        doReturn(member()).when(memberRepository).save(any(Member.class));

        //when
        final MemberResponse result = target.addMember(id,password,name);

        //then
        assertThat(result.getId()).isNotNull();

        //verify
        verify(memberRepository, times(1)).findById(id);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    private Member member() {
        return Member.builder()
                .id(id)
                .password(password)
                .name(name)
                .build();
    }
}
