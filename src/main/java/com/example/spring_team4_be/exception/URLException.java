package com.example.spring_team4_be.exception;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLException {

    public static boolean URLException(String url){

        try{
            new URL(url).toURI();
            return true;
        }catch (URISyntaxException | MalformedURLException exception){
            throw new IllegalArgumentException("imgUrl 이 유효하지 않습니다");
        }

    }
}