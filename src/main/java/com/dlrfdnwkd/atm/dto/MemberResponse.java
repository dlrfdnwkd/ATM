package com.dlrfdnwkd.atm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberResponse {

    private final String id;
    private final String name;
}
