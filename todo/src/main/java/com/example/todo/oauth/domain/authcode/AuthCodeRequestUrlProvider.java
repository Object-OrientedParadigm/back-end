package com.example.todo.oauth.domain.authcode;

import com.example.todo.oauth.domain.OauthServerType;

public interface AuthCodeRequestUrlProvider { //AuthCode를 발급할 URL을 제공하는 기능. 다른 서비스도 추가 할 예정이기에 인터페이스로
    OauthServerType supportServer(); //어떤 OauthServerType을 지원할 수 있는지를 나타냄
    String provide(); //URL을 생성해 반환, 해당 주소로 리다이렉트
}
