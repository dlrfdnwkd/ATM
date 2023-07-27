package com.dlrfdnwkd.atm.controller;

import com.dlrfdnwkd.atm.common.GlobalExceptionHandler;
import com.dlrfdnwkd.atm.dto.MemberRequest;
import com.dlrfdnwkd.atm.enums.MemberErrorResult;
import com.dlrfdnwkd.atm.exception.MemberException;
import com.dlrfdnwkd.atm.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;

import static com.dlrfdnwkd.atm.constants.MemberConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    private MemberController target;
    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void inti() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void mockMvc가Null이아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void 멤버등록실패_사용자식별값이헤더에없음() throws Exception {
        //given
        final String url = "/api/v1/members";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberRequest("id","홍길동")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버등록실패_아이디가null() throws Exception {
        //given
        final String url = "/api/v1/members";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(memberRequest(null, "9512")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버등록실패_MemberService에서에러Throw() throws Exception {
        //given
        final String url = "/api/v1/members";
        lenient().doThrow(new MemberException(MemberErrorResult.DUPLICATED_MEMBER_REGISTER))
                .when(memberService)
                .addMember("id","9512","홍길동");

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(memberRequest("id","9512")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private MemberRequest memberRequest(final String id, final String password) {
        return MemberRequest.builder()
                .id(id)
                .password(password)
                .build();
    }
}
