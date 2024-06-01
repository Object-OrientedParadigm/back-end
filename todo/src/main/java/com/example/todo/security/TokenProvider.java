package com.example.todo.security;

import com.example.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

//@Slf4j
//@Service
//public class TokenProvider {
//
//    private static final String SECRET_KEY="NMA8JPctFuna59f5";
//
//    public String create(UserEntity userEntity){
//        Date expireDate=Date.from(
//                Instant.now().plus(1, ChronoUnit.DAYS));
//
//        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
//                .setSubject(userEntity.getId())
//                .setIssuer("todo app")
//                .setIssuedAt(new Date())
//                .setExpiration(expireDate)
//                .compact();
//    }
//
//    public String validateAndGetUserId(String token){
//        Claims claims=Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
//}


@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY="NMA8JPctFuna59f5";

    public String create(UserEntity userEntity){
        log.info("Creating token for user: {}", userEntity.getId());
        Date expireDate=Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .setSubject(userEntity.getId())
                .setIssuer("todo app")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();

        log.info("Generated Token: {}", token);
        return token;
    }

    public String validateAndGetUserId(String token){
        log.info("Validating token: {}", token);

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();

        log.info("Validated User ID: {}", claims.getSubject());
        return claims.getSubject();
    }
}
