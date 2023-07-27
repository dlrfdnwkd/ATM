package com.dlrfdnwkd.atm.exception;

import com.dlrfdnwkd.atm.enums.MemberErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberException extends RuntimeException{

    private final MemberErrorResult errorResult;

}
