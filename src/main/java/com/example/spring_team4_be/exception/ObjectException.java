package com.example.spring_team4_be.exception;

import com.example.spring_team4_be.entity.Twit;

public class ObjectException {
    public static void  postValidate(Twit twit){
        if(twit.getId()==null || twit.getId()<=0){
            throw  new IllegalArgumentException("유효하지 않는 Post Id입니다.");
        }
    }
}