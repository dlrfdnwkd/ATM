package com.dlrfdnwkd.atm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MemberRequest {

    @NotBlank
    private final String id;
    @NotBlank
    private final String password;
    @NotBlank
    private final String name;
}
