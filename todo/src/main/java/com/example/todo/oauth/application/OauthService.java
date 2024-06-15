package com.example.todo.oauth.application;

import com.example.todo.oauth.OauthMember;
import com.example.todo.oauth.domain.OauthMemberRepository;
import com.example.todo.oauth.domain.OauthServerType;
import com.example.todo.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.todo.oauth.domain.client.OauthMemberClientComposite;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

