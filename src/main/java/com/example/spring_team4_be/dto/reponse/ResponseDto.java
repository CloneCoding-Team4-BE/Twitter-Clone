package com.example.spring_team4_be.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> { //T가 아니고 A/B어떤거든 상관없음 제네릭임을 선언
    private boolean success;
    private T data; //제네릭의 변수 선언방법
    private Error error;

    @AllArgsConstructor
    @Getter
    public static class Error {
        private String code;
        private String message;
    }


    public static <T> ResponseDto<T> success(T data) {

        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(false, null, new Error(code, message));
    }
}