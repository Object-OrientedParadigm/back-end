package com.example.todo.oauth.domain;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.todo.oauth.domain.OauthServerType;

@Component
public class StringToOauthServerTypeConverter implements Converter<String, OauthServerType> {
    @Override
    public OauthServerType convert(String source) {
        try {
            return OauthServerType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // 적절한 예외 처리 또는 로깅
        }
    }
}
