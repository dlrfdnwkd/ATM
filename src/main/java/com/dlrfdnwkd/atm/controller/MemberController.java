package com.dlrfdnwkd.atm.controller;

import com.dlrfdnwkd.atm.dto.MemberRequest;
import com.dlrfdnwkd.atm.dto.MemberResponse;
import com.dlrfdnwkd.atm.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.dlrfdnwkd.atm.constants.MemberConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public ResponseEntity<MemberResponse> addMember(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MemberRequest memberRequest) {

        final MemberResponse memberResponse = memberService.addMember(memberRequest.getId(),memberRequest.getPassword(),memberRequest.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberResponse);
    }

}
