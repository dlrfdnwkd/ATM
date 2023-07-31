package com.dlrfdnwkd.atm.controller;

import com.dlrfdnwkd.atm.common.GlobalExceptionHandler;
import com.dlrfdnwkd.atm.dto.MemberRequest;
import com.dlrfdnwkd.atm.dto.MemberResponse;
import com.dlrfdnwkd.atm.enums.MemberErrorResult;
import com.dlrfdnwkd.atm.exception.MemberException;
import com.dlrfdnwkd.atm.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.dlrfdnwkd.atm.constants.MemberConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
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
                        .content(gson.toJson(memberRequest("id","9512","홍길동")))
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
                        .content(gson.toJson(memberRequest(null, "9512","홍길동")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidMemberAddParameter")
    public void 멤버등록실패_잘못된파라미터(final String id, final String password, final String name) throws Exception {
        //given
        final String url = "/api/v1/members";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"9512")
                        .content(gson.toJson(memberRequest(id,password,name)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMemberAddParameter() {
        return Stream.of(
                Arguments.of(null,"9512","홍길동"),
                Arguments.of("","9512","홍길동"),
                Arguments.of(" ","9512","홍길동")
        );
    }

    @DisplayName("멤버등록실패_MemberServicec에러")
    @Test
    public void 멤버등록실패_MemberService에서에러Throw() throws Exception {
        //given
        final String url = "/api/v1/members";
        memberService.addMember("id","9512","홍길동");
        doThrow(new MemberException(MemberErrorResult.DUPLICATED_MEMBER_REGISTER))
                .when(memberService)
                .addMember("id","9512","홍길동");

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(memberRequest("id","9512","홍길동")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("멤버등록성공")
    @Test
    public void 멤버등록성공() throws Exception {
        //given
        final String url = "/api/v1/members";
        final MemberResponse memberResponse = MemberResponse.builder()
                .id("id")
                .name("홍길동").build();

        doReturn(memberResponse).when(memberService).addMember("id","9512","홍길동");

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(memberRequest("id","9512","홍길동")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        final MemberResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MemberResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("홍길동");
    }

    private MemberRequest memberRequest(final String id, final String password,final String name) {
        return MemberRequest.builder()
                .id(id)
                .password(password)
                .name(name)
                .build();
    }
}
