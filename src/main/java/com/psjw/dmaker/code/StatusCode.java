package com.psjw.dmaker.code;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusCode {
    EMPLOYED("고용"),
    RETIRED("퇴직");

    private final String message;
}
