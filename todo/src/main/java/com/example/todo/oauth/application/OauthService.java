package com.example.todo.oauth.application;
//
//import com.example.todo.oauth.OauthMember;
//import com.example.todo.oauth.domain.OauthMemberRepository;
//import com.example.todo.oauth.domain.OauthServerType;
//import com.example.todo.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
//import com.example.todo.oauth.domain.client.OauthMemberClientComposite;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class OauthService {
//
//    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
//    private final OauthMemberClientComposite oauthMemberClientComposite;
//    private final OauthMemberRepository oauthMemberRepository;
//
//    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
//        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
//    }
//
//    // 추가
//    public Long login(OauthServerType oauthServerType, String authCode) {
//        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
//        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
//                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
//        return saved.id();
//    }
//}

import com.example.todo.oauth.OauthMember;
import com.example.todo.oauth.domain.OauthMemberRepository;
import com.example.todo.oauth.domain.OauthServerType;
import com.example.todo.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.todo.oauth.domain.client.OauthMemberClientComposite;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.handler.codec.http.HttpMethod;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//@Service
//@RequiredArgsConstructor
//public class OauthService {
//
//    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
//    private final OauthMemberClientComposite oauthMemberClientComposite;
//    private final OauthMemberRepository oauthMemberRepository;
//    private static final String SECRET_KEY = "NMA8JPctFuna59f5";  // 강력한 비밀 키로 변경하세요.
//
//    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
//        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
//    }
//
//    public String login(OauthServerType oauthServerType, String authCode) {
//        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
//        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
//                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
//
//        // JWT 토큰 생성 및 반환
//        return generateJwtToken(saved.getId());
//    }
//
//    private String generateJwtToken(Long userId) {
//        long currentTimeMillis = System.currentTimeMillis();
//        Date now = new Date(currentTimeMillis);
//        Date expiryDate = new Date(currentTimeMillis + 86400000); // 24시간 후 만료
//
//        return Jwts.builder()
//                .setSubject(userId.toString())
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
//                .compact();
//    }
//
//
//}

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public String login(OauthServerType oauthServerType, String authCode) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));

        // JWT 토큰 생성 및 반환
        return generateJwtToken(saved.getId());
    }

    private String generateJwtToken(Long userId) {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expiryDate = new Date(currentTimeMillis + 86400000); // 24시간 후 만료

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }
}

